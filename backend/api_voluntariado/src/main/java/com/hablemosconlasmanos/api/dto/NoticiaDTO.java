package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Noticia;

import java.time.LocalDateTime;

public record NoticiaDTO(Long id, String titulo, String slug, String resumen, String contenido, String imagenUrl,
                         String categoria, String autor, PaisDTO pais, boolean publicada, boolean destacada,
                         LocalDateTime fechaPublicacion) {

    public static NoticiaDTO de(Noticia n) {
        return new NoticiaDTO(n.getId(), n.getTitulo(), n.getSlug(), n.getResumen(), n.getContenido(),
                n.getImagenUrl(), n.getCategoria(), n.getAutor(),
                n.getPais() != null ? PaisDTO.de(n.getPais()) : null,
                n.isPublicada(), n.isDestacada(), n.getFechaPublicacion());
    }
}
