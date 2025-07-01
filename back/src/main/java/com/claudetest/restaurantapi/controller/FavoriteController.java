package com.claudetest.restaurantapi.controller;

import com.claudetest.restaurantapi.dto.RestaurantResponseDto;
import com.claudetest.restaurantapi.security.UserPrincipal;
import com.claudetest.restaurantapi.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorite", description = "찜하기 관련 API")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/toggle/{restaurantId}")
    @Operation(summary = "찜하기 토글", description = "맛집을 찜하거나 찜을 해제합니다.")
    public ResponseEntity<?> toggleFavorite(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            Map<String, Object> result = favoriteService.toggleFavorite(userPrincipal.getId(), restaurantId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/check/{restaurantId}")
    @Operation(summary = "찜 상태 확인", description = "특정 맛집의 찜 상태를 확인합니다.")
    public ResponseEntity<Map<String, Boolean>> checkFavorite(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boolean isFavorited = favoriteService.isFavorited(userPrincipal.getId(), restaurantId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("isFavorited", isFavorited);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/my")
    @Operation(summary = "내 찜 목록", description = "로그인한 사용자의 찜한 맛집 목록을 조회합니다.")
    public ResponseEntity<Page<RestaurantResponseDto>> getMyFavorites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantResponseDto> favorites = favoriteService.getFavoritesByUser(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{restaurantId}")
    @Operation(summary = "찜 삭제", description = "특정 맛집의 찜을 삭제합니다.")
    public ResponseEntity<?> removeFavorite(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            favoriteService.removeFavorite(userPrincipal.getId(), restaurantId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "찜이 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/count")
    @Operation(summary = "내 찜 개수", description = "로그인한 사용자의 찜한 맛집 개수를 조회합니다.")
    public ResponseEntity<Map<String, Long>> getFavoriteCount(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long count = favoriteService.countFavoritesByUser(userPrincipal.getId());
        Map<String, Long> result = new HashMap<>();
        result.put("count", count);
        return ResponseEntity.ok(result);
    }
}