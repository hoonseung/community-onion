package com.onion.backend.dto.user;

import com.onion.backend.entity.UserEntity;
import java.time.LocalDateTime;

public record User(
    Long id,
    String username,
    String password,
    String email,
    LocalDateTime lastLogin,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {


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
}
