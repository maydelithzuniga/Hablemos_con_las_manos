package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.Noticia;

import java.time.LocalDateTime;

/** Version liviana de la noticia para listados (sin el contenido completo). */
public record NoticiaResumenDTO(Long id, String titulo, String slug, String resumen, String imagenUrl,
                                String categoria, String autor, boolean publicada, boolean destacada,
                                LocalDateTime fechaPublicacion) {

    public static NoticiaResumenDTO de(Noticia n) {
        return new NoticiaResumenDTO(n.getId(), n.getTitulo(), n.getSlug(), n.getResumen(), n.getImagenUrl(),
                n.getCategoria(), n.getAutor(), n.isPublicada(), n.isDestacada(), n.getFechaPublicacion());
    }
}
