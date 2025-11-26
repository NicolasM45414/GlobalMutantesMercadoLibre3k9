package com.example.GlobalMutantesMercadoLibre3k9.exception;

import com.example.GlobalMutantesMercadoLibre3k9.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException validationException,
            WebRequest incomingRequest) {

        String aggregatedErrors = validationException.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("Validation error: {}", aggregatedErrors);

        ErrorResponse errorPayload = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                aggregatedErrors,
                incomingRequest.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorPayload);
    }

    /**
     * Maneja errores de DNA inválido lanzados manualmente desde la lógica.
     * Permite que los tests verifiquen fácilmente el mensaje exacto.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDna(IllegalArgumentException argumentException) {

        log.warn("Invalid DNA: {}", argumentException.getMessage());

        ErrorResponse errorPayload = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                argumentException.getMessage(),     // <-- EXACTO como espera tu test
                "/mutant"            // opcional
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorPayload);
    }

    @ExceptionHandler(DnaHashCalculationException.class)
    public ResponseEntity<ErrorResponse> handleDnaHashCalculationException(
            DnaHashCalculationException hashException,
            WebRequest incomingRequest) {

        log.error("DNA hash calculation error: {}", hashException.getMessage(), hashException);

        ErrorResponse errorPayload = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Error calculating DNA hash: " + hashException.getMessage(),
                incomingRequest.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorPayload);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception generalException,
            WebRequest incomingRequest) {

        log.error("Unexpected error: {}", generalException.getMessage(), generalException);

        ErrorResponse errorPayload = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred: " + generalException.getMessage(),
                incomingRequest.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorPayload);
    }
}