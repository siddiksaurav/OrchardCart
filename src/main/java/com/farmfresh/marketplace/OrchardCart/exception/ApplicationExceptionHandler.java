package com.farmfresh.marketplace.OrchardCart.exception;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUserException(ElementNotFoundException exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(),request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ElementAlreadyExistException.class)
    public ResponseEntity<ErrorDetails> handleUserException(ElementAlreadyExistException exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(),request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(ConstraintViolationException constraintViolationException) {
        String errorMessage = constraintViolationException.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));

        ErrorDetails errorDetails = new ErrorDetails(errorMessage, "constraint validation error", LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        ErrorDetails errorDetails = new ErrorDetails(methodArgumentNotValidException.getFieldError().getDefaultMessage(),"validation error",LocalDateTime.now());
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException e) {
        ErrorDetails errorDetails = new ErrorDetails("You are not authorized to perform this action", HttpStatus.FORBIDDEN.getReasonPhrase(), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleOtherExceptions(Exception e, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), request.getDescription(false),LocalDateTime.now());
        return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
