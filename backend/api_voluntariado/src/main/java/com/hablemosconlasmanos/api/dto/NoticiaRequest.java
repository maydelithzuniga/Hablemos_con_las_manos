package com.hablemosconlasmanos.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoticiaRequest(
        @NotBlank(message = "El titulo es obligatorio") @Size(max = 200) String titulo,
        @NotBlank(message = "El resumen es obligatorio") @Size(max = 500) String resumen,
        @NotBlank(message = "El contenido es obligatorio") String contenido,
        @Size(max = 500) String imagenUrl,
        @Size(max = 60) String categoria,
        @Size(max = 100) String autor,
        Long paisId,
        Boolean publicada,
        Boolean destacada) {
}
