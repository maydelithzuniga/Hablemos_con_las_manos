package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.entity.Donacion;

/**
 * Integracion con la pasarela de pago (Mercado Pago, Culqi, Niubiz, Stripe, PayPal...).
 * Para conectar una pasarela real, crear otra implementacion de esta interfaz
 * marcada con @Primary (o reemplazar {@link PasarelaPagoSimulada}).
 */
public interface PasarelaPagoService {

    /** Crea la orden/checkout en la pasarela y devuelve la URL a la que se redirige al donante. */
    String crearPago(Donacion donacion);
}
