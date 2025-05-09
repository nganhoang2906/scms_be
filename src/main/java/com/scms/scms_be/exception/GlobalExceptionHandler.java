package com.scms.scms_be.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
    Map<String, Object> errorBody = new HashMap<>();
    errorBody.put("status", ex.getStatus().value());
    errorBody.put("error", ex.getStatus().getReasonPhrase());
    errorBody.put("message", ex.getMessage());
    errorBody.put("timestamp", LocalDateTime.now());
    return new ResponseEntity<>(errorBody, ex.getStatus());
  }
}
