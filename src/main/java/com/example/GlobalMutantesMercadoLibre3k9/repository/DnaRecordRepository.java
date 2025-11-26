package com.example.GlobalMutantesMercadoLibre3k9.repository;

import com.example.GlobalMutantesMercadoLibre3k9.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {
    Optional<DnaRecord> findBySequenceHash(String sequenceHash);
    long countByMutantFlag(boolean mutantFlag);
}
