package com.onion.backend.dto.user;

public record UserSignUpRequest(
    String username,
    String password,
    String email
) {



}
