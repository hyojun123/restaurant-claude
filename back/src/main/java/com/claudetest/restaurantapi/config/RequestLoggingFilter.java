package com.claudetest.restaurantapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        long startTime = System.currentTimeMillis();
        String timestamp = LocalDateTime.now().format(formatter);
        
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // Request 로깅
        logRequest(requestWrapper, timestamp);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logResponse(responseWrapper, duration, timestamp);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, String timestamp) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String remoteAddr = getClientIpAddress(request);
        
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n");
        logMessage.append("=== API REQUEST ===").append("\n");
        logMessage.append("Timestamp: ").append(timestamp).append("\n");
        logMessage.append("URI: ").append(uri);
        if (queryString != null) {
            logMessage.append("?").append(queryString);
        }
        logMessage.append("\n");


        // Request Body 로깅 (POST, PUT, PATCH인 경우)
        if ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method)) {
            String contentType = request.getContentType();
            if (contentType != null && contentType.contains("application/json")) {
                byte[] content = request.getContentAsByteArray();
                if (content.length > 0) {
                    String body = new String(content);
                    // 비밀번호 등 민감한 정보 마스킹
                    body = maskSensitiveData(body);
                    logMessage.append("Request Body: ").append(body).append("\n");
                }
            }
        }
        
        log.info(logMessage.toString());
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration, String timestamp) {
        int status = response.getStatus();
        
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n");
        logMessage.append("=== API RESPONSE ===").append("\n");
        logMessage.append("Timestamp: ").append(timestamp).append("\n");
        logMessage.append("Status: ").append(status).append("\n");
        logMessage.append("Duration: ").append(duration).append("ms").append("\n");

        // Response Body (JSON인 경우만)
        String contentType = response.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            byte[] content = response.getContentAsByteArray();
            if (content.length > 0) {
                String body = new String(content);
                // 응답이 너무 긴 경우 제한
                if (body.length() > 1000) {
                    body = body.substring(0, 1000) + "... [TRUNCATED]";
                }
                logMessage.append("Response Body: ").append(body).append("\n");
            }
        }
        
        logMessage.append("==================").append("\n");
        
        log.info(logMessage.toString());
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    private String maskSensitiveData(String body) {
        return body.replaceAll("(\"password\"\\s*:\\s*\")[^\"]*\"", "$1[HIDDEN]\"")
                  .replaceAll("(\"currentPassword\"\\s*:\\s*\")[^\"]*\"", "$1[HIDDEN]\"")
                  .replaceAll("(\"newPassword\"\\s*:\\s*\")[^\"]*\"", "$1[HIDDEN]\"");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 정적 리소스는 로깅하지 않음
        return path.startsWith("/css") || 
               path.startsWith("/js") || 
               path.startsWith("/images") || 
               path.startsWith("/favicon") ||
               path.startsWith("/actuator/health");
    }
}