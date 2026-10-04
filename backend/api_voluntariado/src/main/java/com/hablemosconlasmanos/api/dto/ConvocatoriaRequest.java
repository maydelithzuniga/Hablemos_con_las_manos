package com.hablemosconlasmanos.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ConvocatoriaRequest(
        @NotBlank(message = "El titulo es obligatorio") @Size(max = 150) String titulo,
        @Size(max = 2000) String descripcion,
        @NotNull(message = "El programa es obligatorio") Long programaId,
        Long paisId,
        @NotNull(message = "La fecha de apertura es obligatoria") LocalDate fechaApertura,
        @NotNull(message = "La fecha de cierre es obligatoria") LocalDate fechaCierre,
        LocalDate fechaInicioVoluntariado,
        @Min(value = 1, message = "Los cupos deben ser al menos 1") Integer cupos,
        @Size(max = 1000) String perfiles,
        Boolean publicada) {
}
