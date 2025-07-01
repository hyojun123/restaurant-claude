package com.claudetest.restaurantapi.controller;

import com.claudetest.restaurantapi.dto.admin.AdminLoginRequest;
import com.claudetest.restaurantapi.dto.auth.JwtAuthenticationResponse;
import com.claudetest.restaurantapi.entity.Admin;
import com.claudetest.restaurantapi.repository.AdminRepository;
import com.claudetest.restaurantapi.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequest request) {
        try {
            Admin admin = adminRepository.findByUsernameAndIsActiveTrue(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));

            if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
            }

            // Admin 전용 토큰 생성 (권한 정보 포함)
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + admin.getRole());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                admin.getUsername(), 
                null, 
                Collections.singletonList(authority)
            );
            String jwt = jwtTokenProvider.generateToken(authentication);

            admin.setLastLoginAt(LocalDateTime.now());
            adminRepository.save(admin);

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", jwt);
            response.put("tokenType", "Bearer");
            response.put("adminInfo", Map.of(
                    "adminId", admin.getAdminId(),
                    "username", admin.getUsername(),
                    "name", admin.getName(),
                    "role", admin.getRole()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인에 실패했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.badRequest().body("인증이 필요합니다.");
        }

        Admin admin = adminRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));

        Map<String, Object> profile = new HashMap<>();
        profile.put("adminId", admin.getAdminId());
        profile.put("username", admin.getUsername());
        profile.put("name", admin.getName());
        profile.put("role", admin.getRole());
        profile.put("lastLoginAt", admin.getLastLoginAt());

        return ResponseEntity.ok(profile);
    }
}