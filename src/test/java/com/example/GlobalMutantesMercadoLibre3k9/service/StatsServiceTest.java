package com.example.GlobalMutantesMercadoLibre3k9.service;

import com.example.GlobalMutantesMercadoLibre3k9.dto.StatsResponse;
import com.example.GlobalMutantesMercadoLibre3k9.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatsService - Tests Unitarios")
class StatsServiceTest {

    @Mock
    private DnaRecordRepository repositoryMock;

    @InjectMocks
    private StatsService serviceUnderTest;

    @Test
    @DisplayName("Debe calcular estadísticas correctamente con mutantes y humanos")
    void testGetStats_WithMutantsAndHumans() {
        when(repositoryMock.countByMutantFlag(true)).thenReturn(40L);
        when(repositoryMock.countByMutantFlag(false)).thenReturn(100L);

        StatsResponse calculatedMetrics = serviceUnderTest.getStats();

        assertEquals(40, calculatedMetrics.getCountMutantDna());
        assertEquals(100, calculatedMetrics.getCountHumanDna());
        assertEquals(0.4, calculatedMetrics.getRatio(), 0.001);
        verify(repositoryMock, times(1)).countByMutantFlag(true);
        verify(repositoryMock, times(1)).countByMutantFlag(false);
    }

    @Test
    @DisplayName("Debe calcular ratio = 1.0 cuando hay igual cantidad")
    void testGetStats_EqualCounts() {
        when(repositoryMock.countByMutantFlag(true)).thenReturn(50L);
        when(repositoryMock.countByMutantFlag(false)).thenReturn(50L);

        StatsResponse balancedMetrics = serviceUnderTest.getStats();

        assertEquals(1.0, balancedMetrics.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debe retornar ratio = 0 cuando no hay registros")
    void testGetStats_NoRecords() {
        when(repositoryMock.countByMutantFlag(true)).thenReturn(0L);
        when(repositoryMock.countByMutantFlag(false)).thenReturn(0L);

        StatsResponse emptyStats = serviceUnderTest.getStats();

        assertEquals(0, emptyStats.getCountMutantDna());
        assertEquals(0, emptyStats.getCountHumanDna());
        assertEquals(0.0, emptyStats.getRatio());
    }

    @Test
    @DisplayName("Debe manejar caso especial: solo mutantes, sin humanos")
    void testGetStats_OnlyMutants() {
        when(repositoryMock.countByMutantFlag(true)).thenReturn(40L);
        when(repositoryMock.countByMutantFlag(false)).thenReturn(0L);

        StatsResponse mutantOnlyStats = serviceUnderTest.getStats();

        assertEquals(40, mutantOnlyStats.getCountMutantDna());
        assertEquals(0, mutantOnlyStats.getCountHumanDna());
        assertEquals(40.0, mutantOnlyStats.getRatio());  // Caso especial
    }

    @Test
    @DisplayName("Debe manejar caso especial: solo humanos, sin mutantes")
    void testGetStats_OnlyHumans() {
        when(repositoryMock.countByMutantFlag(true)).thenReturn(0L);
        when(repositoryMock.countByMutantFlag(false)).thenReturn(100L);

        StatsResponse humanOnlyStats = serviceUnderTest.getStats();

        assertEquals(0, humanOnlyStats.getCountMutantDna());
        assertEquals(100, humanOnlyStats.getCountHumanDna());
        assertEquals(0.0, humanOnlyStats.getRatio());
    }

    @Test
    @DisplayName("Debe calcular ratio > 1 cuando hay más mutantes que humanos")
    void testGetStats_MoreMutantsThanHumans() {
        when(repositoryMock.countByMutantFlag(true)).thenReturn(100L);
        when(repositoryMock.countByMutantFlag(false)).thenReturn(50L);

        StatsResponse dominantMutantStats = serviceUnderTest.getStats();

        assertEquals(100, dominantMutantStats.getCountMutantDna());
        assertEquals(50, dominantMutantStats.getCountHumanDna());
        assertEquals(2.0, dominantMutantStats.getRatio(), 0.001);
    }
}