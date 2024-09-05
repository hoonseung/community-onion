package com.onion.backend.dto.user;

public record UserLoginRequest(
    String username,
    String password
) {

}
