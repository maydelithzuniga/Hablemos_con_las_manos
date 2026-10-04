package com.hablemosconlasmanos.api.dto;

import java.util.List;

/** Pais con sus proyectos y convocatorias abiertas (pagina de detalle de pais). */
public record PaisDetalleDTO(PaisDTO pais, List<ProyectoDTO> proyectos, List<ConvocatoriaDTO> convocatoriasAbiertas) {
}
