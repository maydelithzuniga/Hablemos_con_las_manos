package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.DocumentoTransparencia;
import com.hablemosconlasmanos.api.entity.TipoDocumento;

public record DocumentoTransparenciaDTO(Long id, String titulo, TipoDocumento tipo, Integer anio,
                                        String archivoUrl, String descripcion) {

    public static DocumentoTransparenciaDTO de(DocumentoTransparencia d) {
        return new DocumentoTransparenciaDTO(d.getId(), d.getTitulo(), d.getTipo(), d.getAnio(),
                d.getArchivoUrl(), d.getDescripcion());
    }
}
