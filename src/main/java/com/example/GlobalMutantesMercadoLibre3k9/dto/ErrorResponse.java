package com.example.GlobalMutantesMercadoLibre3k9.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de error")
public class ErrorResponse {

    @Schema(description = "Timestamp del error")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP")
    private int status;

    @Schema(description = "Tipo de error")
    private String error;

    @Schema(description = "Mensaje descriptivo del error")
    private String message;

    @Schema(description = "Path donde ocurrió el error")
    private String path;
}
