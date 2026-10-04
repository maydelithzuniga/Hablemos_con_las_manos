package com.hablemosconlasmanos.api.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Formulario publico "Postula / Se voluntario". Se postula a una convocatoria o, en su defecto, a un programa. */
public record PostulacionRequest(
        Long convocatoriaId,
        Long programaId,
        @NotBlank(message = "Los nombres son obligatorios") @Size(max = 80) String nombres,
        @NotBlank(message = "Los apellidos son obligatorios") @Size(max = 80) String apellidos,
        @NotBlank(message = "El email es obligatorio") @Email(message = "El email no es valido") @Size(max = 120) String email,
        @Size(max = 30) String telefono,
        @Size(max = 30) String documentoIdentidad,
        @Past(message = "La fecha de nacimiento debe ser pasada") LocalDate fechaNacimiento,
        @Size(max = 80) String paisResidencia,
        @Size(max = 80) String ciudad,
        @Size(max = 120) String profesion,
        @Size(max = 20) String nivelLenguaSenas,
        @Size(max = 2000) String experiencia,
        @NotBlank(message = "Cuentanos tu motivacion") @Size(max = 2000) String motivacion,
        @Size(max = 300) String disponibilidad,
        @Size(max = 500) String cvUrl,
        @AssertTrue(message = "Debes aceptar la politica de tratamiento de datos") boolean aceptaPoliticaDatos) {
}
