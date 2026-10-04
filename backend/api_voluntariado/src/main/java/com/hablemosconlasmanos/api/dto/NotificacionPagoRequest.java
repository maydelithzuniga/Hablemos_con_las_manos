package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.EstadoDonacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Notificacion (webhook) enviada por la pasarela de pago cuando cambia el
 * estado de un pago. Adaptar este formato al de la pasarela elegida.
 */
public record NotificacionPagoRequest(
        @NotBlank String referencia,
        @NotNull EstadoDonacion estado,
        String idTransaccion,
        String metodoPago) {
}
