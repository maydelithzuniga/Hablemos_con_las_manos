package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.TipoMensaje;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContactoRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 120) String nombre,
        @NotBlank(message = "El email es obligatorio") @Email(message = "El email no es válido") @Size(max = 120) String email,
        @Size(max = 30) String telefono,
        @Size(max = 150) String organizacion,
        @NotNull(message = "El tipo de consulta es obligatorio") TipoMensaje tipo,
        @NotBlank(message = "El asunto es obligatorio") @Size(max = 150) String asunto,
        @NotBlank(message = "El mensaje es obligatorio") @Size(max = 3000) String mensaje) {
}
