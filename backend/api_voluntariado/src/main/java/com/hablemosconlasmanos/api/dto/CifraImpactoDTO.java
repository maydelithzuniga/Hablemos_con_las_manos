package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.CifraImpacto;

public record CifraImpactoDTO(Long id, String etiqueta, Long valor, String prefijo, String sufijo, String icono,
                              Integer orden) {

    public static CifraImpactoDTO de(CifraImpacto c) {
        return new CifraImpactoDTO(c.getId(), c.getEtiqueta(), c.getValor(), c.getPrefijo(), c.getSufijo(),
                c.getIcono(), c.getOrden());
    }
}
