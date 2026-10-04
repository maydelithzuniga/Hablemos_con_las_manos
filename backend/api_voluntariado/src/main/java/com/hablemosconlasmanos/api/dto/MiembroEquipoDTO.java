package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.AreaEquipo;
import com.hablemosconlasmanos.api.entity.MiembroEquipo;

public record MiembroEquipoDTO(Long id, String nombre, String cargo, AreaEquipo area, String fotoUrl,
                               String biografia, String linkedinUrl, Integer orden, boolean activo) {

    public static MiembroEquipoDTO de(MiembroEquipo m) {
        return new MiembroEquipoDTO(m.getId(), m.getNombre(), m.getCargo(), m.getArea(), m.getFotoUrl(),
                m.getBiografia(), m.getLinkedinUrl(), m.getOrden(), m.isActivo());
    }
}
