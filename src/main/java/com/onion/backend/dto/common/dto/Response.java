package com.onion.backend.dto.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onion.backend.dto.common.exception.ErrorType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Response<T>(
    Integer code,
    String message,
    T data
) {


    public static <T> Response<T> success(T data) {
        return new Response<>(1, "success", data);
    }

    public static <T> Response<T> fail(ErrorType type) {
        return new Response<>(type.getCode(), type.getDescription(), null);
    }

}
