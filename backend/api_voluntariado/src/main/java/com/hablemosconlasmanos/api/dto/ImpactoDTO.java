package com.hablemosconlasmanos.api.dto;

import java.util.List;

/**
 * Cifras de impacto: las editables desde el panel y las calculadas en vivo
 * a partir de los datos del sistema.
 */
public record ImpactoDTO(List<CifraImpactoDTO> cifras, long paisesActivos, long proyectosEnCurso,
                         long proyectosTotales, long beneficiarios, long voluntariosAceptados,
                         long sociosMensuales) {
}
