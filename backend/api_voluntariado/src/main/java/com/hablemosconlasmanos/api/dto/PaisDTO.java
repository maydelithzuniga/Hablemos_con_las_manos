package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Pais;

public record PaisDTO(Long id, String nombre, String codigo, String descripcion, String imagenUrl, boolean activo) {

    public static PaisDTO de(Pais p) {
        return new PaisDTO(p.getId(), p.getNombre(), p.getCodigo(), p.getDescripcion(), p.getImagenUrl(), p.isActivo());
    }
}
