package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.EstadoDonacion;

/** Respuesta al iniciar una donacion: el frontend redirige al donante a urlPago. */
public record DonacionCreadaDTO(String referencia, EstadoDonacion estado, String urlPago) {
}
