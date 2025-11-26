package com.example.GlobalMutantesMercadoLibre3k9.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class MutantDetector {

    private static final int REQUIRED_MATCH_LENGTH = 4;
    private static final Set<Character> ALLOWED_DNA_CHARS = Set.of('A', 'T', 'C', 'G');

    public boolean isMutant(String[] dna) {
        if (!isValidDna(dna)) {
            log.debug("DNA inválido detectado");
            return false;
        }
        final int gridSize = dna.length;
        char[][] dnaGrid = buildCharMatrix(dna, gridSize);
        int matchingPatternsFound = 0;
        for (int currentRow = 0; currentRow < gridSize; currentRow++) {
            for (int currentCol = 0; currentCol < gridSize; currentCol++) {
                // Horizontal →
                if (currentCol <= gridSize - REQUIRED_MATCH_LENGTH) {
                    if (hasHorizontalMatch(dnaGrid, currentRow, currentCol)) {
                        matchingPatternsFound++;
                        log.debug("Secuencia horizontal encontrada en ({}, {})", currentRow, currentCol);
                        // OPTIMIZACIÓN 4: Early Termination
                        if (matchingPatternsFound > 1) {
                            log.info("Mutante detectado con {} secuencias", matchingPatternsFound);
                            return true;
                        }
                    }
                }
                // Vertical ↓
                if (currentRow <= gridSize - REQUIRED_MATCH_LENGTH) {
                    if (hasVerticalMatch(dnaGrid, currentRow, currentCol)) {
                        matchingPatternsFound++;
                        log.debug("Secuencia vertical encontrada en ({}, {})", currentRow, currentCol);
                        if (matchingPatternsFound > 1) {
                            log.info("Mutante detectado con {} secuencias", matchingPatternsFound);
                            return true;
                        }
                    }
                }
                // Diagonal Descendente ↘
                if (currentRow <= gridSize - REQUIRED_MATCH_LENGTH && currentCol <= gridSize - REQUIRED_MATCH_LENGTH) {
                    if (hasDiagonalDownMatch(dnaGrid, currentRow, currentCol)) {
                        matchingPatternsFound++;
                        log.debug("Secuencia diagonal descendente encontrada en ({}, {})", currentRow, currentCol);
                        if (matchingPatternsFound > 1) {
                            log.info("Mutante detectado con {} secuencias", matchingPatternsFound);
                            return true;
                        }
                    }
                }
                // Diagonal Ascendente ↗
                if (currentRow >= REQUIRED_MATCH_LENGTH - 1 && currentCol <= gridSize - REQUIRED_MATCH_LENGTH) {
                    if (hasDiagonalUpMatch(dnaGrid, currentRow, currentCol)) {
                        matchingPatternsFound++;
                        log.debug("Secuencia diagonal ascendente encontrada en ({}, {})", currentRow, currentCol);
                        if (matchingPatternsFound > 1) {
                            log.info("Mutante detectado con {} secuencias", matchingPatternsFound);
                            return true;
                        }
                    }
                }
            }
        }
        log.info("DNA analizado: {} secuencia(s) encontrada(s) - {}",
                matchingPatternsFound, matchingPatternsFound > 1 ? "MUTANTE" : "HUMANO");
        return false;
    }

    private boolean hasHorizontalMatch(char[][] dnaGrid, int rowIndex, int colIndex) {
        final char referenceBase = dnaGrid[rowIndex][colIndex];
        return dnaGrid[rowIndex][colIndex + 1] == referenceBase &&
                dnaGrid[rowIndex][colIndex + 2] == referenceBase &&
                dnaGrid[rowIndex][colIndex + 3] == referenceBase;
    }

    private boolean hasVerticalMatch(char[][] dnaGrid, int rowIndex, int colIndex) {
        final char referenceBase = dnaGrid[rowIndex][colIndex];
        return dnaGrid[rowIndex + 1][colIndex] == referenceBase &&
                dnaGrid[rowIndex + 2][colIndex] == referenceBase &&
                dnaGrid[rowIndex + 3][colIndex] == referenceBase;
    }

    private boolean hasDiagonalDownMatch(char[][] dnaGrid, int rowIndex, int colIndex) {
        final char referenceBase = dnaGrid[rowIndex][colIndex];
        return dnaGrid[rowIndex + 1][colIndex + 1] == referenceBase &&
                dnaGrid[rowIndex + 2][colIndex + 2] == referenceBase &&
                dnaGrid[rowIndex + 3][colIndex + 3] == referenceBase;
    }

    private boolean hasDiagonalUpMatch(char[][] dnaGrid, int rowIndex, int colIndex) {
        final char referenceBase = dnaGrid[rowIndex][colIndex];
        return dnaGrid[rowIndex - 1][colIndex + 1] == referenceBase &&
                dnaGrid[rowIndex - 2][colIndex + 2] == referenceBase &&
                dnaGrid[rowIndex - 3][colIndex + 3] == referenceBase;
    }

    private char[][] buildCharMatrix(String[] dna, int gridSize) {
        char[][] dnaGrid = new char[gridSize][];
        for (int idx = 0; idx < gridSize; idx++) {
            dnaGrid[idx] = dna[idx].toCharArray();
        }
        return dnaGrid;
    }

    private boolean isValidDna(String[] dna) {
        // Null o vacío
        if (dna == null || dna.length == 0) {
            return false;
        }

        final int dimension = dna.length;

        // Tamaño mínimo 4x4
        if (dimension < REQUIRED_MATCH_LENGTH) {
            return false;
        }

        for (String sequenceRow : dna) {
            // Fila null
            if (sequenceRow == null) {
                return false;
            }

            // No es cuadrada (sequenceRow.length != dimension)
            if (sequenceRow.length() != dimension) {
                return false;
            }

            // Caracteres inválidos
            for (char nucleotide : sequenceRow.toCharArray()) {
                if (!ALLOWED_DNA_CHARS.contains(nucleotide)) {
                    return false;
                }
            }
        }
        return true;
    }
}