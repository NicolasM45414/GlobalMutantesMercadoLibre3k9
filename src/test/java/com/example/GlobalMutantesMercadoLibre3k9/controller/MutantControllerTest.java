package com.example.GlobalMutantesMercadoLibre3k9.controller;

import com.example.GlobalMutantesMercadoLibre3k9.service.MutantService;
import com.example.GlobalMutantesMercadoLibre3k9.service.StatsService;
import com.example.GlobalMutantesMercadoLibre3k9.dto.StatsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MutantController.class)
@DisplayName("MutantController - Tests de Integración")
class MutantControllerTest {
    @Autowired
    private MockMvc httpRequestSimulator;
    @MockBean
    private MutantService mutantService;
    @MockBean
    private StatsService statsService;
    // ==================== POST /mutant ====================
    @Test
    @DisplayName("POST /mutant debe retornar 200 OK cuando es mutante")
    void testCheckMutant_ReturnOk_WhenIsMutant() throws Exception {
        // Arrange
        String requestBodyJson = """
            {
              "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
            }
            """;

        when(mutantService.analyzeDna(any())).thenReturn(true);

        httpRequestSimulator.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden cuando es humano")
    void testCheckMutant_ReturnForbidden_WhenIsHuman() throws Exception {
        String requestBodyJson = """
            {
              "dna": ["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]
            }
            """;

        when(mutantService.analyzeDna(any())).thenReturn(false);

        httpRequestSimulator.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request cuando DNA es null")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsNull() throws Exception {
        // Arrange
        String requestBodyJson = """
            {
              "dna": null
            }
            """;

        httpRequestSimulator.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request cuando DNA es vacío")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsEmpty() throws Exception {

        String requestBodyJson = """
            {
              "dna": []
            }
            """;


        httpRequestSimulator.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request cuando matriz no es cuadrada")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsNotSquare() throws Exception {

        String requestBodyJson = """
            {
              "dna": ["ATGC","CAG","TTAT"]
            }
            """;


        httpRequestSimulator.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request cuando hay caracteres inválidos")
    void testCheckMutant_ReturnBadRequest_WhenInvalidCharacters() throws Exception {

        String requestBodyJson = """
            {
              "dna": ["ATXC","CAGT","TTAT","AGAC"]
            }
            """;


        httpRequestSimulator.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    // ==================== GET /stats ====================

    @Test
    @DisplayName("GET /stats debe retornar 200 OK con estadísticas")
    void testGetStats_ReturnOk_WithStats() throws Exception {

        StatsResponse metricsSnapshot = new StatsResponse(40L, 100L, 0.4);
        when(statsService.getStats()).thenReturn(metricsSnapshot);


        httpRequestSimulator.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count_mutant_dna").value(40))
                .andExpect(jsonPath("$.count_human_dna").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }

    @Test
    @DisplayName("GET /stats debe retornar 200 OK cuando no hay registros")
    void testGetStats_ReturnOk_WhenNoRecords() throws Exception {

        StatsResponse emptyMetrics = new StatsResponse(0L, 0L, 0.0);
        when(statsService.getStats()).thenReturn(emptyMetrics);


        httpRequestSimulator.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(0))
                .andExpect(jsonPath("$.count_human_dna").value(0))
                .andExpect(jsonPath("$.ratio").value(0.0));
    }
}