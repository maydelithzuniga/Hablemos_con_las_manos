package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Donacion;
import com.hablemosconlasmanos.api.entity.EstadoDonacion;
import com.hablemosconlasmanos.api.entity.TipoDonacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DonacionDTO(Long id, String referencia, TipoDonacion tipo, BigDecimal monto, String moneda,
                          String nombre, String email, String telefono, String documentoIdentidad, String pais,
                          String mensaje, boolean anonima, Long proyectoId, String proyectoTitulo,
                          EstadoDonacion estado, String metodoPago, String idTransaccion, LocalDateTime fechaPago,
                          LocalDateTime creadoEn) {

    public static DonacionDTO de(Donacion d) {
        return new DonacionDTO(d.getId(), d.getReferencia(), d.getTipo(), d.getMonto(), d.getMoneda(),
                d.getNombre(), d.getEmail(), d.getTelefono(), d.getDocumentoIdentidad(), d.getPais(),
                d.getMensaje(), d.isAnonima(),
                d.getProyecto() != null ? d.getProyecto().getId() : null,
                d.getProyecto() != null ? d.getProyecto().getTitulo() : null,
                d.getEstado(), d.getMetodoPago(), d.getIdTransaccion(), d.getFechaPago(), d.getCreadoEn());
    }
}
