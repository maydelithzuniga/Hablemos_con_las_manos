package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.TipoDonacion;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/** Formulario publico "Dona" / "Hazte socio". */
public record DonacionRequest(
        @NotNull(message = "El tipo de donación es obligatorio") TipoDonacion tipo,
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "1.00", message = "El monto mínimo es 1")
        @DecimalMax(value = "100000.00", message = "El monto excede el máximo permitido") BigDecimal monto,
        @NotBlank(message = "La moneda es obligatoria")
        @Pattern(regexp = "^[A-Za-z]{3}$", message = "La moneda debe ser un código ISO de 3 letras (PEN, USD...)") String moneda,
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 120) String nombre,
        @NotBlank(message = "El email es obligatorio") @Email(message = "El email no es válido") @Size(max = 120) String email,
        @Size(max = 30) String telefono,
        @Size(max = 30) String documentoIdentidad,
        @Size(max = 80) String pais,
        @Size(max = 500) String mensaje,
        boolean anonima,
        Long proyectoId,
        /** Si el donante tambien quiere recibir el boletin. */
        boolean suscribirBoletin) {
}
