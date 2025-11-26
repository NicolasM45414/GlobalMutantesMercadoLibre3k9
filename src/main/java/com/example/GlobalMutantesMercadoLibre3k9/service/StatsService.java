package com.example.GlobalMutantesMercadoLibre3k9.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.GlobalMutantesMercadoLibre3k9.dto.StatsResponse;
import com.example.GlobalMutantesMercadoLibre3k9.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {
    private final DnaRecordRepository repository;
    @Transactional(readOnly = true)
    public StatsResponse getStats() {
        long mutantRecordsTotal = repository.countByMutantFlag(true);
        long humanRecordsTotal = repository.countByMutantFlag(false);

        double calculatedRatio = computeRatio(mutantRecordsTotal, humanRecordsTotal);

        log.info("Stats calculadas - Mutantes: {}, Humanos: {}, Ratio: {}",
                mutantRecordsTotal, humanRecordsTotal, calculatedRatio);

        return new StatsResponse(mutantRecordsTotal, humanRecordsTotal, calculatedRatio);
    }

    private double computeRatio(long mutantCount, long humanCount) {
        if (humanCount == 0) {
            // Caso especial: no hay humanos
            return mutantCount > 0 ? (double) mutantCount : 0.0;
        }
        return (double) mutantCount / humanCount;
    }
}