package com.claudetest.restaurantapi.controller;

import com.claudetest.restaurantapi.entity.User;
import com.claudetest.restaurantapi.repository.UserRepository;
import com.claudetest.restaurantapi.repository.ReviewRepository;
import com.claudetest.restaurantapi.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) Boolean isActive) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> users;

        if (nickname != null && !nickname.isEmpty()) {
            users = userRepository.findByNicknameContainingIgnoreCase(nickname, pageable);
        } else if (isActive != null) {
            users = userRepository.findByIsActive(isActive, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("users", users.getContent().stream().map(user -> {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("email", user.getEmail());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("socialType", user.getSocialType());
            userInfo.put("isActive", user.getIsActive());
            userInfo.put("createdAt", user.getCreatedAt());
            userInfo.put("reviewCount", reviewRepository.countByUserUserId(user.getUserId()));
            userInfo.put("favoriteCount", favoriteRepository.countByUserUserId(user.getUserId()));
            return userInfo;
        }).toList());
        response.put("totalElements", users.getTotalElements());
        response.put("totalPages", users.getTotalPages());
        response.put("currentPage", users.getNumber());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            Map<String, Object> userDetail = new HashMap<>();
            userDetail.put("userId", u.getUserId());
            userDetail.put("email", u.getEmail());
            userDetail.put("nickname", u.getNickname());
            userDetail.put("profileImageUrl", u.getProfileImageUrl());
            userDetail.put("socialType", u.getSocialType());
            userDetail.put("isActive", u.getIsActive());
            userDetail.put("createdAt", u.getCreatedAt());
            userDetail.put("reviewCount", reviewRepository.countByUserUserId(u.getUserId()));
            userDetail.put("favoriteCount", favoriteRepository.countByUserUserId(u.getUserId()));
            return ResponseEntity.ok(userDetail);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> status) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setIsActive(status.get("isActive"));
            userRepository.save(u);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<?> getUserReviews(@PathVariable Long id) {
        return ResponseEntity.ok(reviewRepository.findByUserUserIdOrderByCreatedAtDesc(id));
    }

    @GetMapping("/{id}/favorites")
    public ResponseEntity<?> getUserFavorites(@PathVariable Long id) {
        return ResponseEntity.ok(favoriteRepository.findByUserIdWithRestaurant(id));
    }
}