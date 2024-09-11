package com.onion.backend.dto.common.exception;

public class TimeRateLimitException extends RuntimeException {


    public TimeRateLimitException(String message) {
        super(message);
    }
}
