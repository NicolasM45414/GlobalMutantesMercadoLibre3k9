package com.example.GlobalMutantesMercadoLibre3k9.service;

import com.example.GlobalMutantesMercadoLibre3k9.entity.DnaRecord;
import com.example.GlobalMutantesMercadoLibre3k9.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MutantService - Tests Unitarios")
class MutantServiceTest {

    @Mock
    private MutantDetector detectorMock;

    @Mock
    private DnaRecordRepository repositoryMock;

    @InjectMocks
    private MutantService serviceUnderTest;

    private String[] positiveTestSequence;
    private String[] negativeTestSequence;

    @BeforeEach
    void setUp() {
        positiveTestSequence = new String[]{
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        negativeTestSequence = new String[]{
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
    }

    @Test
    @DisplayName("Debe analizar y guardar DNA mutante cuando no existe en BD")
    void testAnalyzeDna_MutantNotInDatabase() {

        when(repositoryMock.findBySequenceHash(anyString())).thenReturn(Optional.empty());
        when(detectorMock.isMutant(positiveTestSequence)).thenReturn(true);
        when(repositoryMock.save(any(DnaRecord.class))).thenReturn(new DnaRecord());

        boolean analysisOutcome = serviceUnderTest.analyzeDna(positiveTestSequence);

        assertTrue(analysisOutcome);
        verify(repositoryMock, times(1)).findBySequenceHash(anyString());
        verify(detectorMock, times(1)).isMutant(positiveTestSequence);
        verify(repositoryMock, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe retornar resultado cacheado cuando DNA ya existe en BD")
    void testAnalyzeDna_AlreadyInDatabase() {
        DnaRecord storedRecord = new DnaRecord();
        storedRecord.setSequenceHash("abc123");
        storedRecord.setMutantFlag(true);

        when(repositoryMock.findBySequenceHash(anyString())).thenReturn(Optional.of(storedRecord));

        boolean cachedResult = serviceUnderTest.analyzeDna(positiveTestSequence);

        assertTrue(cachedResult);
        verify(repositoryMock, times(1)).findBySequenceHash(anyString());
        verify(detectorMock, never()).isMutant(any());  // No debe llamar al detector
        verify(repositoryMock, never()).save(any());  // No debe guardar
    }

    @Test
    @DisplayName("Debe analizar y guardar DNA humano cuando no existe en BD")
    void testAnalyzeDna_HumanNotInDatabase() {

        when(repositoryMock.findBySequenceHash(anyString())).thenReturn(Optional.empty());
        when(detectorMock.isMutant(negativeTestSequence)).thenReturn(false);
        when(repositoryMock.save(any(DnaRecord.class))).thenReturn(new DnaRecord());

        boolean analysisOutcome = serviceUnderTest.analyzeDna(negativeTestSequence);

        assertFalse(analysisOutcome);
        verify(repositoryMock, times(1)).findBySequenceHash(anyString());
        verify(detectorMock, times(1)).isMutant(negativeTestSequence);
        verify(repositoryMock, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe calcular hash SHA-256 correctamente")
    void testCalculateDnaHash() {
        String firstHashComputation = serviceUnderTest.calculateDnaHash(positiveTestSequence);
        String secondHashComputation = serviceUnderTest.calculateDnaHash(positiveTestSequence);

        assertNotNull(firstHashComputation);
        assertEquals(64, firstHashComputation.length());  // SHA-256 = 64 caracteres hex
        assertEquals(firstHashComputation, secondHashComputation);  // Mismo DNA → mismo hash
    }

    @Test
    @DisplayName("Debe generar hashes diferentes para DNAs diferentes")
    void testCalculateDnaHash_DifferentDnas() {
        String mutantHash = serviceUnderTest.calculateDnaHash(positiveTestSequence);
        String humanHash = serviceUnderTest.calculateDnaHash(negativeTestSequence);

        assertNotEquals(mutantHash, humanHash);
    }
}