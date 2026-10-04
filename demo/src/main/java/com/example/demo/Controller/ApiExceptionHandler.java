package com.example.demo.Controller;

import com.example.demo.Service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Turns exceptions thrown anywhere in a @RestController into a clean JSON
 * response instead of Spring's default 500 stack-trace page. Applies
 * globally — no need to reference this class anywhere else.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    // Fires when a @Valid @RequestBody fails validation (blank name, bad email, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> onValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> fieldErrors.putIfAbsent(err.getField(), err.getDefaultMessage()));

        return ResponseEntity.badRequest().body(Map.of(
                "message", "Check the highlighted fields.",
                "fieldErrors", fieldErrors));
    }

    // Fires when someone submits the contact form too many times in an hour
    @ExceptionHandler(ContactService.TooManyMessagesException.class)
    public ResponseEntity<Map<String, Object>> onRateLimit(ContactService.TooManyMessagesException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("message", ex.getMessage()));
    }
}