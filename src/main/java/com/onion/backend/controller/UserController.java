package com.onion.backend.controller;

import com.onion.backend.dto.common.dto.Response;
import com.onion.backend.dto.user.User;
import com.onion.backend.dto.user.UserLoginRequest;
import com.onion.backend.dto.user.UserLoginResponse;
import com.onion.backend.dto.user.UserSignUpRequest;
import com.onion.backend.dto.user.UserSignUpResponse;
import com.onion.backend.entity.jwt.LogoutType;
import com.onion.backend.security.jwt.JwtProvider;
import com.onion.backend.service.jwt.JwtBlackListService;
import com.onion.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final JwtBlackListService jwtBlackListService;


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
    public ResponseEntity<Response<Long>> secession(
        @PathVariable Long userId) {

        return ResponseEntity.ok(
            Response.success(userService.secession(userId))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Response<UserLoginResponse>> login(
        @RequestBody UserLoginRequest request) {

        User user = userService.login(request);

        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE,
                createResponseCookie("onion_token",
                    jwtProvider.generateToken(request.username()), 1800)
                    .toString()
            )
            .body(
                Response.success(
                    UserLoginResponse.from(user, jwtProvider.generateToken(user.getUsername())))
            );
    }

    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(
        @CookieValue("onion_token") String token,
        @RequestHeader(name = "LG-TYPE") String type) {
        jwtBlackListService.blackListRegistration(token, LogoutType.valueOf(type));

        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, createResponseCookie("onion_token", "", 0)
                .toString()
            )
            .body(Response.success(null));
    }


    private ResponseCookie createResponseCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
            .httpOnly(true)
            .path("/")
            .maxAge(maxAge)
            .build();
    }

}
