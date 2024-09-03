package com.onion.backend.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Response<T>(
    Integer code,
    String message,
    T data
) {


    public static <T> Response<T> success(T data) {
        return new Response<>(1, "success", data);
    }

    public static <T> Response<T> fail() {
        return new Response<>(0, "fail", null);
    }

}
