package com.hablemosconlasmanos.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El email es obligatorio") @Email String email,
        @NotBlank(message = "La contrasena es obligatoria") String password) {
}
