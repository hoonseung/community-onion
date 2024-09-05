package com.onion.backend.dto.user;

import com.onion.backend.entity.user.UserEntity;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public record User(
    Long id,
    String username,
    String password,
    String email,
    LocalDateTime lastLogin,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) implements UserDetails {


    public static User from(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getUsername(),
            entity.getPassword(),
            entity.getEmail(),
            entity.getLastLogin(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


}
