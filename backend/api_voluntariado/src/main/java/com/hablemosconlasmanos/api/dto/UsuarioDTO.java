package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Rol;
import com.hablemosconlasmanos.api.entity.Usuario;

public record UsuarioDTO(Long id, String nombre, String email, Rol rol, boolean activo) {

    public static UsuarioDTO de(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNombre(), u.getEmail(), u.getRol(), u.isActivo());
    }
}
