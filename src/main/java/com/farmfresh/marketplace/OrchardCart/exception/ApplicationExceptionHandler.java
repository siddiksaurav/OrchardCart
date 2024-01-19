package com.farmfresh.marketplace.OrchardCart.exception;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.validation.FieldError;


@RestControllerAdvice
public class ApplicationExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleElementNotFoundException(ElementNotFoundException exception, WebRequest request) {
        log.error("ElementNotFoundException: {}", exception.getMessage(), exception);
        return createErrorResponse(exception.getMessage(), request.getDescription(false), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ElementAlreadyExistException.class)
    public ResponseEntity<ErrorDetails> handleElementAlreadyExistException(ElementAlreadyExistException exception, WebRequest request) {
        log.error("ElementAlreadyExistException: {}", exception.getMessage(), exception);
        return createErrorResponse(exception.getMessage(), request.getDescription(false), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(ConstraintViolationException exception) {
        String errorMessage = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));
        log.error("ConstraintViolationException: {}", errorMessage, exception);
        return createErrorResponse(errorMessage, "Constraint validation error", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        log.error("Validation error: {}", errorMessage, exception);
        return createErrorResponse(errorMessage, "Validation error", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException exception) {
        log.error("AccessDeniedException: {}", exception.getMessage(), exception);
        return createErrorResponse("You are not authorized to perform this action", HttpStatus.FORBIDDEN.getReasonPhrase(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleOtherExceptions(Exception exception, WebRequest request) {
        log.error("Unhandled exception: {}", exception.getMessage(), exception);
        return createErrorResponse(exception.getMessage(), request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDetails> createErrorResponse(String message, String details, HttpStatus status) {
        ErrorDetails errorDetails = new ErrorDetails(message, details, LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, status);
    }
}


