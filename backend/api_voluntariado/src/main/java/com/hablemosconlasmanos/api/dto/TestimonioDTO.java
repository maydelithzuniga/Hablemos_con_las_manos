package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Testimonio;

import java.time.LocalDateTime;

public record TestimonioDTO(Long id, String nombre, String rol, String texto, String fotoUrl, Long programaId,
                            String programaTitulo, boolean aprobado, LocalDateTime creadoEn) {

    public static TestimonioDTO de(Testimonio t) {
        return new TestimonioDTO(t.getId(), t.getNombre(), t.getRol(), t.getTexto(), t.getFotoUrl(),
                t.getPrograma() != null ? t.getPrograma().getId() : null,
                t.getPrograma() != null ? t.getPrograma().getTitulo() : null,
                t.isAprobado(), t.getCreadoEn());
    }
}
