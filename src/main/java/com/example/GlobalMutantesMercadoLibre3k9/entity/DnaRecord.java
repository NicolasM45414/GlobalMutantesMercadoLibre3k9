package com.example.GlobalMutantesMercadoLibre3k9.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
@Entity
@Table(
        name = "dna_records",
        indexes = {
                @Index(name = "idx_dna_hash", columnList = "sequenceHash"),
                @Index(name = "idx_is_mutant", columnList = "mutantFlag")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class DnaRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sequenceHash", unique = true, nullable = false, length = 64)
    private String sequenceHash;
    @Column(name = "mutantFlag", nullable = false)
    private boolean mutantFlag;
    @Column(name = "analyzedAt", nullable = false)
    private LocalDateTime analyzedAt;

    public DnaRecord(String sequenceHash, boolean mutantFlag) {
        this.sequenceHash = sequenceHash;
        this.mutantFlag = mutantFlag;
        this.analyzedAt = LocalDateTime.now();
    }
}
