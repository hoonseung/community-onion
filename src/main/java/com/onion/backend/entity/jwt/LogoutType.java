package com.onion.backend.entity.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogoutType {

    SINGLE("단일 로그아웃"),

    EVERY("전부 로그아웃"),

    ;


    private final String description;


}
