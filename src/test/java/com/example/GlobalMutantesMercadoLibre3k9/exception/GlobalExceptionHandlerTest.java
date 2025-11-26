package com.example.GlobalMutantesMercadoLibre3k9.exception;

import com.example.GlobalMutantesMercadoLibre3k9.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleInvalidDna() {
        IllegalArgumentException illegalArgException = new IllegalArgumentException("DNA inválido");

        ResponseEntity<ErrorResponse> errorResponse = exceptionHandler.handleInvalidDna(illegalArgException);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        assertEquals("DNA inválido", errorResponse.getBody().getMessage());
    }
}
