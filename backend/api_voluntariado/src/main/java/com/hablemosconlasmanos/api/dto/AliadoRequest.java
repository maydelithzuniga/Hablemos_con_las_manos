package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.TipoAliado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AliadoRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 150) String nombre,
        @Size(max = 500) String logoUrl,
        @Size(max = 300) String sitioWeb,
        @NotNull(message = "El tipo es obligatorio") TipoAliado tipo,
        @Size(max = 500) String descripcion,
        Integer orden,
        Boolean activo) {
}
