package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.EstadoPostulacion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CambioEstadoPostulacionRequest(
        @NotNull(message = "El estado es obligatorio") EstadoPostulacion estado,
        @Size(max = 2000) String notasInternas,
        /** Si es true se envia un correo al postulante informando el cambio. */
        boolean notificar) {
}
