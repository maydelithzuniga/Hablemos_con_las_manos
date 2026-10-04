package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Donacion;
import com.hablemosconlasmanos.api.entity.EstadoDonacion;
import com.hablemosconlasmanos.api.entity.TipoDonacion;

import java.math.BigDecimal;

/** Vista publica del estado de una donacion (pagina de "gracias"). */
public record EstadoDonacionDTO(String referencia, TipoDonacion tipo, BigDecimal monto, String moneda,
                                EstadoDonacion estado) {

    public static EstadoDonacionDTO de(Donacion d) {
        return new EstadoDonacionDTO(d.getReferencia(), d.getTipo(), d.getMonto(), d.getMoneda(), d.getEstado());
    }
}
