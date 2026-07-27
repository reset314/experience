package com.example.experience.infrastructure.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.experience.domain.rbac.entity.RolePermission;
import com.example.experience.domain.rbac.entity.UserRole;
import com.example.experience.domain.rbac.repository.RolePermissionRepository;
import com.example.experience.domain.rbac.repository.UserRoleRepository;
import com.example.experience.domain.user.entity.User;
import com.example.experience.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities = userRoles.stream()
            .flatMap(ur -> rolePermissionRepository.findByRoleId(ur.getRole().getId()).stream())
            .map(RolePermission::getPermission)
            .distinct()
            .map(p -> new SimpleGrantedAuthority(p.getResource() + ":" + p.getAction()))
            .toList();

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPasswordHash(),
            authorities
        );
    }
}
