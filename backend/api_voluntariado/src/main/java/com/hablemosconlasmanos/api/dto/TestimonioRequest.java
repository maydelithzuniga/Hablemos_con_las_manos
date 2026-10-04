package com.hablemosconlasmanos.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Usado tanto por el formulario publico (queda pendiente de aprobacion) como por el panel. */
public record TestimonioRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 120) String nombre,
        @Size(max = 150) String rol,
        @NotBlank(message = "El testimonio es obligatorio") @Size(min = 20, max = 1000, message = "El testimonio debe tener entre 20 y 1000 caracteres") String texto,
        @Size(max = 500) String fotoUrl,
        Long programaId,
        Boolean aprobado) {
}
