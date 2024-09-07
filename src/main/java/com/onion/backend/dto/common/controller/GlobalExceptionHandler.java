package com.onion.backend.dto.common.controller;

import com.onion.backend.dto.common.dto.Response;
import com.onion.backend.dto.common.exception.ErrorType;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Response<Void>> handleEntityNotFoundException(
        EntityNotFoundException ene) {
        log.error("occurs entity not found error : {}", ene.getMessage());
        return ResponseEntity.status(404).body(
            Response.fail(ErrorType.NOT_FOUND)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Void>> handleIllegalArgumentException(
        IllegalArgumentException iae) {
        log.error("occurs illegal argument error : {}", iae.getMessage());
        return ResponseEntity.status(400).body(
            Response.fail(ErrorType.BAD_REQUEST)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleException(Exception e) {
        log.error("occurs internal exception error : {}", e.getMessage());
        return ResponseEntity.status(500).body(
            Response.fail(ErrorType.INTERNAL_SERVER_ERROR)
        );
    }
}
