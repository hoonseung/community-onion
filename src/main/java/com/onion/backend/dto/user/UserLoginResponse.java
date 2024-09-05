package com.onion.backend.dto.user;

import java.time.LocalDateTime;

public record UserLoginResponse(
    Long id,
    String username,
    String email,
    LocalDateTime lastLogin,
    String token
) {

    public static UserLoginResponse from(User user, String token) {
        return new UserLoginResponse(
            user.id(),
            user.username(),
            user.email(),
            user.lastLogin(),
            token
        );
    }
}
