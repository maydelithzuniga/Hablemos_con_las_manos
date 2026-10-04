package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Modalidad;
import com.hablemosconlasmanos.api.entity.Programa;
import com.hablemosconlasmanos.api.entity.TipoPrograma;

public record ProgramaDTO(Long id, String titulo, String slug, String resumen, String descripcion,
                          TipoPrograma tipo, Modalidad modalidad, String duracion, String requisitos,
                          String beneficios, String imagenUrl, boolean destacado, boolean activo) {

    public static ProgramaDTO de(Programa p) {
        return new ProgramaDTO(p.getId(), p.getTitulo(), p.getSlug(), p.getResumen(), p.getDescripcion(),
                p.getTipo(), p.getModalidad(), p.getDuracion(), p.getRequisitos(), p.getBeneficios(),
                p.getImagenUrl(), p.isDestacado(), p.isActivo());
    }
}
