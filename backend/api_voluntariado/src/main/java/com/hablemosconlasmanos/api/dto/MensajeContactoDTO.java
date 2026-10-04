package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.MensajeContacto;
import com.hablemosconlasmanos.api.entity.TipoMensaje;

import java.time.LocalDateTime;

public record MensajeContactoDTO(Long id, String nombre, String email, String telefono, String organizacion,
                                 TipoMensaje tipo, String asunto, String mensaje, boolean leido, boolean respondido,
                                 LocalDateTime creadoEn) {

    public static MensajeContactoDTO de(MensajeContacto m) {
        return new MensajeContactoDTO(m.getId(), m.getNombre(), m.getEmail(), m.getTelefono(), m.getOrganizacion(),
                m.getTipo(), m.getAsunto(), m.getMensaje(), m.isLeido(), m.isRespondido(), m.getCreadoEn());
    }
}
