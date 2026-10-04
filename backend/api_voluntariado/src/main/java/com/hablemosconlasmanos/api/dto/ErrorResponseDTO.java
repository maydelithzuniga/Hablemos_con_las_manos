package com.hablemosconlasmanos.api.dto;

import java.time.LocalDateTime;
import java.util.List;

/** Formato comun de las respuestas de error de la API. */
public record ErrorResponseDTO(
        LocalDateTime fecha,
        int estado,
        String error,
        String mensaje,
        String ruta,
        List<String> detalles) {
}
