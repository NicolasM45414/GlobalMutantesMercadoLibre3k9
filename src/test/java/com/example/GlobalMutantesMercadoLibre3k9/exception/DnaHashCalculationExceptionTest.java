package com.example.GlobalMutantesMercadoLibre3k9.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DnaHashCalculationExceptionTest {

    @Test
    void testExceptionMessage() {
        DnaHashCalculationException thrownException = new DnaHashCalculationException("Error hashing DNA");
        assertEquals("Error hashing DNA", thrownException.getMessage());
    }
}
