package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Suscriptor;

import java.time.LocalDateTime;

public record SuscriptorDTO(Long id, String email, String nombre, boolean activo, LocalDateTime creadoEn) {

    public static SuscriptorDTO de(Suscriptor s) {
        return new SuscriptorDTO(s.getId(), s.getEmail(), s.getNombre(), s.isActivo(), s.getCreadoEn());
    }
}
