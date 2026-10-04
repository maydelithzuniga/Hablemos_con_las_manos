package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.AreaEquipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MiembroEquipoRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 120) String nombre,
        @NotBlank(message = "El cargo es obligatorio") @Size(max = 120) String cargo,
        @NotNull(message = "El area es obligatoria") AreaEquipo area,
        @Size(max = 500) String fotoUrl,
        @Size(max = 1000) String biografia,
        @Size(max = 300) String linkedinUrl,
        Integer orden,
        Boolean activo) {
}
