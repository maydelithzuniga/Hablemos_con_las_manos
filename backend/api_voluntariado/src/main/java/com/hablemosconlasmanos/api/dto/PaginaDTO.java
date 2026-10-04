package com.hablemosconlasmanos.api.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/** Respuesta paginada estable (evita serializar directamente PageImpl). */
public record PaginaDTO<T>(
        List<T> contenido,
        int pagina,
        int tamanio,
        long totalElementos,
        int totalPaginas,
        boolean ultima) {

    public static <E, T> PaginaDTO<T> de(Page<E> page, Function<E, T> mapper) {
        return new PaginaDTO<>(
                page.getContent().stream().map(mapper).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }
}
