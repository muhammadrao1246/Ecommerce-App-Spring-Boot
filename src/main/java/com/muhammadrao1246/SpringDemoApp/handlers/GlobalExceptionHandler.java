package com.muhammadrao1246.SpringDemoApp.handlers;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.HashMap;

@RestControllerAdvice
// it acts global Exception handlers which catches error on dispatcher servlet - controller layer by default
public class GlobalExceptionHandler {

    // DTO FIELD EXCEPTIONS CAPTURED HERE
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleDTOValidationException(MethodArgumentNotValidException ex){
        HashMap<String, ArrayList<Object>> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            ArrayList<Object> errorList = errors.getOrDefault(error.getField(), new ArrayList<>());
            errorList.add(error.getDefaultMessage());
            errors.putIfAbsent(error.getField(), errorList);
        }

        // creating API json response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRenderer.builder()
                .message("Field Validation Error")
                .status(HttpStatus.BAD_REQUEST.name())
                .errors(errors)
                .build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found: "+ ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Don't have enough permissions: "+ex.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJWTException(JwtException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT Token: "+ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleUnauthorizedException(AuthenticationException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Failed: "+ex.getMessage());
    }

    // here we can define multiple exception handlers for different types of exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleExceptions(Exception ex){
        return ResponseEntity.status(500).body(ex.getMessage());
    }
}
