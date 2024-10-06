package com.kittynicky.tasktrackerbackend.advice;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    protected ResponseEntity<?> handlerUnauthorized(Exception ex) {
        log.error(ex.getMessage(), ex);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return buildResponseEntity(apiResponse);
    }

    @ExceptionHandler({ConstraintViolationException.class, ValidationException.class})
    protected ResponseEntity<?> handlerBadRequest(Exception ex) {
        log.error(ex.getMessage(), ex);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(apiResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<?> handlerNotFound(Exception ex) {
        log.error(ex.getMessage(), ex);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return buildResponseEntity(apiResponse);
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<?> handlerConflict(Exception ex) {
        log.error(ex.getMessage(), ex);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CONFLICT, ex.getMessage());
        return buildResponseEntity(apiResponse);
    }

    private ResponseEntity<?> buildResponseEntity(ApiResponse apiResponse) {
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
}