package com.hablemosconlasmanos.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CifraImpactoRequest(
        @NotBlank(message = "La etiqueta es obligatoria") @Size(max = 100) String etiqueta,
        @NotNull(message = "El valor es obligatorio") @Min(0) Long valor,
        @Size(max = 10) String prefijo,
        @Size(max = 10) String sufijo,
        @Size(max = 50) String icono,
        Integer orden) {
}
