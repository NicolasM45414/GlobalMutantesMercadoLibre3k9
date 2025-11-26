package com.example.GlobalMutantesMercadoLibre3k9.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.GlobalMutantesMercadoLibre3k9.entity.DnaRecord;
import com.example.GlobalMutantesMercadoLibre3k9.exception.DnaHashCalculationException;
import com.example.GlobalMutantesMercadoLibre3k9.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MutantService {
    private final MutantDetector mutantDetector;
    private final DnaRecordRepository repository;
    @Transactional
    public boolean analyzeDna(String[] dna) {
        String computedHash = calculateDnaHash(dna);
        log.debug("DNA hash calculado: {}", computedHash);

        Optional<DnaRecord> cachedEntry = repository.findBySequenceHash(computedHash);

        if (cachedEntry.isPresent()) {
            boolean mutantStatus = cachedEntry.get().isMutantFlag();
            log.info("DNA encontrado en caché - isMutant: {}", mutantStatus);
            return mutantStatus;
        }

        log.debug("DNA no encontrado en caché, analizando...");
        boolean mutantStatus = mutantDetector.isMutant(dna);

        DnaRecord freshRecord = new DnaRecord(computedHash, mutantStatus);
        repository.save(freshRecord);
        log.info("DNA analizado y guardado - isMutant: {}", mutantStatus);

        return mutantStatus;
    }
    protected String calculateDnaHash(String[] dna) {
        try {
            MessageDigest hashGenerator = MessageDigest.getInstance("SHA-256");

            // Concatenar todas las filas
            String concatenatedSequence = String.join("", dna);

            // Calcular hash
            byte[] rawHashBytes = hashGenerator.digest(concatenatedSequence.getBytes(StandardCharsets.UTF_8));

            // Convertir a hexadecimal
            StringBuilder hexResult = new StringBuilder();
            for (byte singleByte : rawHashBytes) {
                String hexValue = Integer.toHexString(0xff & singleByte);
                if (hexValue.length() == 1) {
                    hexResult.append('0');
                }
                hexResult.append(hexValue);
            }

            return hexResult.toString();
        } catch (NoSuchAlgorithmException exception) {
            log.error("Error calculando hash SHA-256", exception);
            throw new DnaHashCalculationException("Error calculating DNA hash", exception);
        }
    }
}
