package com.claudetest.restaurantapi.service;

import com.claudetest.restaurantapi.dto.RestaurantResponseDto;
import com.claudetest.restaurantapi.entity.Favorite;
import com.claudetest.restaurantapi.entity.Restaurant;
import com.claudetest.restaurantapi.entity.User;
import com.claudetest.restaurantapi.repository.FavoriteRepository;
import com.claudetest.restaurantapi.repository.RestaurantRepository;
import com.claudetest.restaurantapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Transactional
    public Map<String, Object> toggleFavorite(Long userId, Long restaurantId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        boolean isFavorited = favoriteRepository.existsByUserUserIdAndRestaurantRestaurantId(userId, restaurantId);
        Map<String, Object> result = new HashMap<>();

        if (isFavorited) {
            // 찜 해제
            favoriteRepository.deleteByUserUserIdAndRestaurantRestaurantId(userId, restaurantId);
            result.put("isFavorited", false);
            result.put("message", "찜이 해제되었습니다.");
        } else {
            // 찜 추가
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setRestaurant(restaurant);
            favoriteRepository.save(favorite);
            result.put("isFavorited", true);
            result.put("message", "찜에 추가되었습니다.");
        }

        return result;
    }

    public boolean isFavorited(Long userId, Long restaurantId) {
        return favoriteRepository.existsByUserUserIdAndRestaurantRestaurantId(userId, restaurantId);
    }

    public Page<RestaurantResponseDto> getFavoritesByUser(Long userId, Pageable pageable) {
        return favoriteRepository.findByUserUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(favorite -> new RestaurantResponseDto(favorite.getRestaurant()));
    }

    @Transactional
    public void removeFavorite(Long userId, Long restaurantId) {
        if (!favoriteRepository.existsByUserUserIdAndRestaurantRestaurantId(userId, restaurantId)) {
            throw new RuntimeException("찜하지 않은 맛집입니다.");
        }
        favoriteRepository.deleteByUserUserIdAndRestaurantRestaurantId(userId, restaurantId);
    }

    public long countFavoritesByUser(Long userId) {
        return favoriteRepository.findByUserUserIdOrderByCreatedAtDesc(userId, Pageable.unpaged()).getTotalElements();
    }
}