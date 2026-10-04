package com.hablemosconlasmanos.api.dto;

import java.util.List;

/** Todo lo que necesita la portada en una sola llamada. */
public record InicioDTO(List<ProgramaDTO> programasDestacados, List<ProyectoDTO> proyectosDestacados,
                        List<ConvocatoriaDTO> convocatoriasAbiertas, List<NoticiaResumenDTO> ultimasNoticias,
                        List<TestimonioDTO> testimonios, List<AliadoDTO> aliados, ImpactoDTO impacto) {
}
