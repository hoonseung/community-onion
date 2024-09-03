package com.onion.backend.controller;

import com.onion.backend.dto.common.Response;
import com.onion.backend.dto.user.UserSignUpRequest;
import com.onion.backend.dto.user.UserSignUpResponse;
import com.onion.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<Response<UserSignUpResponse>> signup(
        @RequestBody UserSignUpRequest request) {
        return ResponseEntity.ok(
            Response.success(
                UserSignUpResponse.from(userService.signup(request))
            )
        );
    }

    @DeleteMapping("/secession/{userId}")
    public ResponseEntity<Response<Void>> secession(
        @PathVariable Long userId) {
        userService.secession(userId);
        return ResponseEntity.ok(Response.success(null));
    }
}
