package com.claudetest.restaurantapi.service;

import com.claudetest.restaurantapi.entity.Admin;
import com.claudetest.restaurantapi.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService {

    private final AdminRepository adminRepository;

    public UserDetails loadAdminByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with username: " + username));

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + admin.getRole())
        );

        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(!admin.getIsActive())
                .credentialsExpired(false)
                .disabled(!admin.getIsActive())
                .build();
    }
}