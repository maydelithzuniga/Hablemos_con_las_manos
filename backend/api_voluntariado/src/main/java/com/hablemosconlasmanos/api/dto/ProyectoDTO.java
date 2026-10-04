package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.AreaTematica;
import com.hablemosconlasmanos.api.entity.EstadoProyecto;
import com.hablemosconlasmanos.api.entity.Proyecto;

import java.time.LocalDate;

public record ProyectoDTO(Long id, String titulo, String slug, String resumen, String descripcion,
                          PaisDTO pais, Long programaId, String programaTitulo, AreaTematica area,
                          EstadoProyecto estado, String socioLocal, LocalDate fechaInicio, LocalDate fechaFin,
                          Integer beneficiarios, String imagenUrl, boolean destacado) {

    public static ProyectoDTO de(Proyecto p) {
        return new ProyectoDTO(p.getId(), p.getTitulo(), p.getSlug(), p.getResumen(), p.getDescripcion(),
                PaisDTO.de(p.getPais()),
                p.getPrograma() != null ? p.getPrograma().getId() : null,
                p.getPrograma() != null ? p.getPrograma().getTitulo() : null,
                p.getArea(), p.getEstado(), p.getSocioLocal(), p.getFechaInicio(), p.getFechaFin(),
                p.getBeneficiarios(), p.getImagenUrl(), p.isDestacado());
    }
}
