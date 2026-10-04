package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.EstadoPostulacion;
import com.hablemosconlasmanos.api.entity.Postulacion;

import java.time.LocalDateTime;

/** Vista publica (sin datos sensibles) del estado de una postulacion. */
public record SeguimientoPostulacionDTO(String codigo, String nombres, String postulacionA,
                                        EstadoPostulacion estado, LocalDateTime fechaPostulacion,
                                        LocalDateTime ultimaActualizacion) {

    public static SeguimientoPostulacionDTO de(Postulacion p) {
        return new SeguimientoPostulacionDTO(p.getCodigo(), p.getNombres(), PostulacionDTO.destino(p),
                p.getEstado(), p.getCreadoEn(), p.getActualizadoEn());
    }
}
