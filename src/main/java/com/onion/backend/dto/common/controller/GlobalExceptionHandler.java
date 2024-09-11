package com.onion.backend.dto.common.controller;

import com.onion.backend.dto.common.dto.Response;
import com.onion.backend.dto.common.exception.ErrorType;
import com.onion.backend.dto.common.exception.TimeRateLimitException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Response<Void>> handleEntityNotFoundException(
        EntityNotFoundException ex) {
        log.error("occurs entity not found error : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Response.fail(ErrorType.NOT_FOUND)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Void>> handleIllegalArgumentException(
        IllegalArgumentException ex) {
        log.error("occurs illegal argument error : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            Response.fail(ErrorType.BAD_REQUEST)
        );
    }

    @ExceptionHandler(TimeRateLimitException.class)
    public ResponseEntity<Response<Void>> handleTimeRateLimitException(
        TimeRateLimitException ex) {
        log.error("occurs time limit error : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            Response.fail(ErrorType.FORBIDDEN)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleException(Exception ex) {
        log.error("occurs internal exception error : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            Response.fail(ErrorType.INTERNAL_SERVER_ERROR)
        );
    }
}
