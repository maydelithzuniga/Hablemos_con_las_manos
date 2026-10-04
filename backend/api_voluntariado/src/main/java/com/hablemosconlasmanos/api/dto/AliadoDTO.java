package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Aliado;
import com.hablemosconlasmanos.api.entity.TipoAliado;

public record AliadoDTO(Long id, String nombre, String logoUrl, String sitioWeb, TipoAliado tipo,
                        String descripcion, Integer orden, boolean activo) {

    public static AliadoDTO de(Aliado a) {
        return new AliadoDTO(a.getId(), a.getNombre(), a.getLogoUrl(), a.getSitioWeb(), a.getTipo(),
                a.getDescripcion(), a.getOrden(), a.isActivo());
    }
}
