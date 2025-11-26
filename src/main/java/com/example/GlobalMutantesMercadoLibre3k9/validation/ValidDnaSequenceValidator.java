package com.example.GlobalMutantesMercadoLibre3k9.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final int MINIMUM_MATRIX_SIZE = 4;
    private static final Pattern VALID_NUCLEOTIDE_PATTERN = Pattern.compile("^[ATCG]+$");

    @Override
    public void initialize(ValidDnaSequence constraintAnnotation) {
        // No necesita inicialización
    }

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext validationContext) {
        // Null o vacío
        if (dna == null || dna.length == 0) {
            registerViolation(validationContext, "DNA array cannot be null or empty");
            return false;
        }

        final int matrixDimension = dna.length;

        // Tamaño mínimo
        if (matrixDimension < MINIMUM_MATRIX_SIZE) {
            registerViolation(validationContext, "DNA matrix must be at least 4x4. Current size: " + matrixDimension + "x" + matrixDimension);
            return false;
        }

        // Validar cada fila
        for (int rowIndex = 0; rowIndex < matrixDimension; rowIndex++) {
            String currentRow = dna[rowIndex];

            // Fila null
            if (currentRow == null) {
                registerViolation(validationContext, "Row " + rowIndex + " is null");
                return false;
            }

            // No es cuadrada
            if (currentRow.length() != matrixDimension) {
                registerViolation(validationContext,
                        "DNA matrix must be square NxN. Expected length: " + matrixDimension + ", got: " + currentRow.length() + " at row " + rowIndex);
                return false;
            }

            // Caracteres inválidos
            if (!VALID_NUCLEOTIDE_PATTERN.matcher(currentRow).matches()) {
                registerViolation(validationContext,
                        "Row " + rowIndex + " contains invalid characters. Only A, T, C, G are allowed. Row: " + currentRow);
                return false;
            }
        }

        return true;
    }

    private void registerViolation(ConstraintValidatorContext validationContext, String violationMessage) {
        validationContext.disableDefaultConstraintViolation();
        validationContext.buildConstraintViolationWithTemplate(violationMessage)
                .addConstraintViolation();
    }
}
