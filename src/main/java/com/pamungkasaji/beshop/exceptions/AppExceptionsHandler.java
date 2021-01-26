package com.pamungkasaji.beshop.exceptions;

import com.pamungkasaji.beshop.model.response.error.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), Instant.now());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ProductServiceException.class})
    public ResponseEntity<Object> handleProductServiceException(ProductServiceException ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), Instant.now());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {OrderServiceException.class})
    public ResponseEntity<Object> handleOrderServiceException(OrderServiceException ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), Instant.now());
        return new ResponseEntity<>(apiError, ex.getStatusCode());
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), Instant.now());
        return new ResponseEntity<>(apiError, ex.getStatusCode());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), Instant.now());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
