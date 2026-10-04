package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.AreaTematica;
import com.hablemosconlasmanos.api.entity.EstadoProyecto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProyectoRequest(
        @NotBlank(message = "El titulo es obligatorio") @Size(max = 150) String titulo,
        @NotBlank(message = "El resumen es obligatorio") @Size(max = 400) String resumen,
        @NotBlank(message = "La descripcion es obligatoria") String descripcion,
        @NotNull(message = "El pais es obligatorio") Long paisId,
        Long programaId,
        @NotNull(message = "El area tematica es obligatoria") AreaTematica area,
        EstadoProyecto estado,
        @Size(max = 150) String socioLocal,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        @Min(value = 0, message = "Los beneficiarios no pueden ser negativos") Integer beneficiarios,
        @Size(max = 500) String imagenUrl,
        Boolean destacado) {
}
