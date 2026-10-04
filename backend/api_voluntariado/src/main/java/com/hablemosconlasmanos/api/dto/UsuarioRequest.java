package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Alta/edicion de usuarios del panel. En edicion, password vacio = no se cambia. */
public record UsuarioRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 120) String nombre,
        @NotBlank(message = "El email es obligatorio") @Email @Size(max = 120) String email,
        @Size(min = 8, max = 72, message = "La contrasena debe tener entre 8 y 72 caracteres") String password,
        @NotNull(message = "El rol es obligatorio") Rol rol,
        Boolean activo) {
}
