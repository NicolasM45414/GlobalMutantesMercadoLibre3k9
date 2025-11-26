package com.example.GlobalMutantesMercadoLibre3k9.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("MutantDetector - Tests Unitarios")
class MutantDetectorTest {

    private MutantDetector detectorInstance;

    @BeforeEach
    void setUp() {
        detectorInstance = new MutantDetector();
    }
    // ==================== CASOS MUTANTES ====================
    @Test
    @DisplayName("Debe detectar mutante con secuencias horizontal y diagonal")
    void testMutantWithHorizontalAndDiagonalSequences() {
        String[] sequenceArray = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",  // Horizontal: CCCC
                "TCACTG"
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias verticales")
    void testMutantWithVerticalSequences() {
        String[] sequenceArray = {
                "ATGCGA",
                "ATGTGC",
                "ATATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante con múltiples secuencias horizontales")
    void testMutantWithMultipleHorizontalSequences() {
        String[] sequenceArray = {
                "AAAAGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonales ascendentes y descendentes")
    void testMutantWithBothDiagonals() {
        String[] sequenceArray = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante en matriz grande 10x10")
    void testMutantWithLargeDna() {
        String[] largeSequenceArray = {
                "ATGCGATGCA",
                "CAGTGCATGC",
                "TTATGTATGC",
                "AGAAGGATGC",
                "CCCCTAATGC",
                "TCACTGATGC",
                "ATGCGATGCA",
                "CAGTGCATGC",
                "TTATGTATGC",
                "AGAAGGATGC"
        };
        assertTrue(detectorInstance.isMutant(largeSequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante cuando toda la matriz es igual")
    void testMutantAllSameCharacter() {
        String[] uniformSequence = {
                "AAAA",
                "AAAA",
                "AAAA",
                "AAAA"
        };
        assertTrue(detectorInstance.isMutant(uniformSequence));
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonal en esquina")
    void testMutantDiagonalInCorner() {
        String[] cornerSequence = {
                "AAAA",  // Horizontal: AAAA
                "TATG",
                "GTAT",
                "CGTA"   // Diagonal: A(3,0), A(2,1), A(1,2), A(0,3)
        };
        assertTrue(detectorInstance.isMutant(cornerSequence));
    }

    // ==================== CASOS HUMANOS ====================

    @Test
    @DisplayName("No debe detectar mutante con solo una secuencia")
    void testNotMutantWithOnlyOneSequence() {
        String[] singlePatternDna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",  // Solo 1 secuencia: TTTT
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertFalse(detectorInstance.isMutant(singlePatternDna));
    }

    @Test
    @DisplayName("No debe detectar mutante sin secuencias")
    void testNotMutantWithNoSequences() {
        String[] noPatternsSequence = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(detectorInstance.isMutant(noPatternsSequence));
    }

    @Test
    @DisplayName("No debe detectar mutante en matriz 4x4 sin secuencias")
    void testNotMutantSmallDna() {
        String[] smallSequence = {
                "ATCG",
                "CGAT",
                "TACG",
                "GCTA"
        };
        assertFalse(detectorInstance.isMutant(smallSequence));
    }

    // ==================== VALIDACIONES ====================

    @Test
    @DisplayName("Debe retornar false para DNA null")
    void testNotMutantWithNullDna() {
        assertFalse(detectorInstance.isMutant(null));
    }

    @Test
    @DisplayName("Debe retornar false para array vacío")
    void testNotMutantWithEmptyDna() {
        String[] emptySequence = {};
        assertFalse(detectorInstance.isMutant(emptySequence));
    }

    @Test
    @DisplayName("Debe retornar false para matriz no cuadrada")
    void testNotMutantWithNonSquareDna() {
        String[] irregularMatrix = {
                "ATGC",
                "CAG",      // Longitud diferente
                "TTAT",
                "AGAC"
        };
        assertFalse(detectorInstance.isMutant(irregularMatrix));
    }

    @Test
    @DisplayName("Debe retornar false para caracteres inválidos")
    void testNotMutantWithInvalidCharacters() {
        String[] invalidCharsSequence = {
                "ATXC",  // X no es válido
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(detectorInstance.isMutant(invalidCharsSequence));
    }

    @Test
    @DisplayName("Debe retornar false si alguna fila es null")
    void testNotMutantWithNullRow() {
        String[] sequenceWithNullRow = {
                "ATGC",
                null,    // Fila null
                "TTAT",
                "AGAC"
        };
        assertFalse(detectorInstance.isMutant(sequenceWithNullRow));
    }

    @Test
    @DisplayName("Debe retornar false para matriz muy pequeña (menor a 4x4)")
    void testNotMutantWithTooSmallDna() {
        String[] tinyMatrix = {
                "ATG",
                "CAG",
                "TTA"
        };
        assertFalse(detectorInstance.isMutant(tinyMatrix));
    }

    @Test
    void testNotMutantWithSequenceLongerThanFour() {
        String[] extendedPatternDna = {
                "AAAAAA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detectorInstance.isMutant(extendedPatternDna));
    }

// ==================== TESTS ADICIONALES PARA COBERTURA ÓPTIMA ====================

    @Test
    @DisplayName("Debe detectar mutante con secuencia vertical en primera columna")
    void testMutantVerticalFirstColumn() {
        String[] sequenceArray = {
                "ATGCGA",
                "ATGTGC",
                "ATATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonal descendente desde posición (0,0)")
    void testMutantDiagonalFromOrigin() {
        String[] sequenceArray = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias en última fila")
    void testMutantLastRow() {
        String[] sequenceArray = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CTACTA",
                "AAAATG"  // AAAA en última fila
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonal ascendente desde última fila")
    void testMutantAscendingFromBottom() {
        String[] sequenceArray = {
                "ATGCTA",
                "CAGTAC",
                "TTAAGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencia vertical en última columna")
    void testMutantVerticalLastColumn() {
        String[] sequenceArray = {
                "ATGCGA",
                "CAGTGA",
                "TTATGA",
                "AGAAGA",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante con múltiples secuencias verticales")
    void testMutantMultipleVerticals() {
        String[] sequenceArray = {
                "ATGCGA",
                "ATGTGC",
                "ATATGT",
                "ATAACG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("No debe detectar mutante con secuencias de solo 3 caracteres")
    void testNotMutantWithOnlyThreeCharSequences() {
        String[] sequenceArray = {
                "AAATGC",
                "CAGTGC",
                "TTATGT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertFalse(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante en matriz 5x5")
    void testMutantMatrix5x5() {
        String[] sequenceArray = {
                "ATGCG",
                "CAGTG",
                "TTATG",
                "AGAAG",
                "CCCCT"
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonal ascendente completa")
    void testMutantFullAscendingDiagonal() {
        String[] sequenceArray = {
                "ATGCTA",
                "CAGTAC",
                "TTAAGT",
                "AGTAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detectorInstance.isMutant(sequenceArray));
    }

    @Test
    @DisplayName("Debe retornar false para matriz con caracteres mezclados sin patrón")
    void testNotMutantRandomMixedCharacters() {
        String[] sequenceArray = {
                "ATCGAT",
                "CGTATC",
                "TACGTA",
                "GATCGA",
                "ATCGAT",
                "CGTATC"
        };
        assertFalse(detectorInstance.isMutant(sequenceArray));
    }
}