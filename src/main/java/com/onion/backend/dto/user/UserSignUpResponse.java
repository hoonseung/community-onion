package com.onion.backend.dto.user;

import java.time.LocalDateTime;

public record UserSignUpResponse(
    Long id,
    String username,
    String email,
    LocalDateTime createdAt
) {


    public static UserSignUpResponse from(User user) {
        return new UserSignUpResponse(
            user.id(),
            user.username(),
            user.email(),
            user.createdAt()
        );
    }
}
