package com.claudetest.restaurantapi.config;

import com.claudetest.restaurantapi.entity.Admin;
import com.claudetest.restaurantapi.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner initializeAdminAccounts() {
        return args -> {
            // admin 계정 생성
            if (!adminRepository.existsByUsername("admin")) {
                Admin admin = Admin.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .name("시스템 관리자")
                        .role("SUPER_ADMIN")
                        .isActive(true)
                        .build();
                adminRepository.save(admin);
                log.info("Admin account created: admin / admin123");
            }

            // manager 계정 생성
            if (!adminRepository.existsByUsername("manager")) {
                Admin manager = Admin.builder()
                        .username("manager")
                        .password(passwordEncoder.encode("admin123"))
                        .name("일반 관리자")
                        .role("ADMIN")
                        .isActive(true)
                        .build();
                adminRepository.save(manager);
                log.info("Manager account created: manager / admin123");
            }
        };
    }
}