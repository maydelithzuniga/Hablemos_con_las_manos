package com.hablemosconlasmanos.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PaisRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 80) String nombre,
        @NotBlank(message = "El codigo es obligatorio")
        @Pattern(regexp = "^[A-Za-z]{2}$", message = "El codigo debe ser ISO de 2 letras (ej. PE)") String codigo,
        @Size(max = 1000) String descripcion,
        @Size(max = 500) String imagenUrl,
        Boolean activo) {
}
