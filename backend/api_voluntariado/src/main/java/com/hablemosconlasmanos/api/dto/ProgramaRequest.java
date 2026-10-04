package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Modalidad;
import com.hablemosconlasmanos.api.entity.TipoPrograma;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProgramaRequest(
        @NotBlank(message = "El titulo es obligatorio") @Size(max = 150) String titulo,
        @NotBlank(message = "El resumen es obligatorio") @Size(max = 400) String resumen,
        @NotBlank(message = "La descripcion es obligatoria") String descripcion,
        @NotNull(message = "El tipo es obligatorio") TipoPrograma tipo,
        @NotNull(message = "La modalidad es obligatoria") Modalidad modalidad,
        @Size(max = 80) String duracion,
        @Size(max = 2000) String requisitos,
        @Size(max = 2000) String beneficios,
        @Size(max = 500) String imagenUrl,
        Boolean destacado,
        Boolean activo) {
}
