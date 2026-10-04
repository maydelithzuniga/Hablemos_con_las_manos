package com.hablemosconlasmanos.api.dto;

import java.math.BigDecimal;
import java.util.Map;

/** Resumen para la pantalla principal del panel de administracion. */
public record DashboardDTO(Map<String, Long> postulacionesPorEstado, long mensajesSinLeer,
                           long suscriptoresActivos, long donacionesCompletadas, long donacionesPendientes,
                           Map<String, BigDecimal> recaudadoUltimos30Dias, long sociosMensuales,
                           long convocatoriasAbiertas, long testimoniosPorAprobar) {
}
