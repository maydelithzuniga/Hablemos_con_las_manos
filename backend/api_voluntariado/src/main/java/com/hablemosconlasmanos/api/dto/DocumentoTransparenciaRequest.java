package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.TipoDocumento;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DocumentoTransparenciaRequest(
        @NotBlank(message = "El titulo es obligatorio") @Size(max = 200) String titulo,
        @NotNull(message = "El tipo es obligatorio") TipoDocumento tipo,
        @NotNull(message = "El anio es obligatorio") @Min(1990) @Max(2100) Integer anio,
        @NotBlank(message = "La URL del archivo es obligatoria") @Size(max = 500) String archivoUrl,
        @Size(max = 500) String descripcion) {
}
