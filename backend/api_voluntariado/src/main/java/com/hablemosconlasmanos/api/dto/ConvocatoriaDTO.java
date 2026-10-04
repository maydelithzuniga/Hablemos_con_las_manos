package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Convocatoria;

import java.time.LocalDate;

public record ConvocatoriaDTO(Long id, String titulo, String descripcion, Long programaId, String programaTitulo,
                              PaisDTO pais, LocalDate fechaApertura, LocalDate fechaCierre,
                              LocalDate fechaInicioVoluntariado, Integer cupos, String perfiles,
                              boolean publicada, boolean abierta) {

    public static ConvocatoriaDTO de(Convocatoria c) {
        return new ConvocatoriaDTO(c.getId(), c.getTitulo(), c.getDescripcion(),
                c.getPrograma().getId(), c.getPrograma().getTitulo(),
                c.getPais() != null ? PaisDTO.de(c.getPais()) : null,
                c.getFechaApertura(), c.getFechaCierre(), c.getFechaInicioVoluntariado(), c.getCupos(),
                c.getPerfiles(), c.isPublicada(), c.estaAbierta());
    }
}
