package com.hablemosconlasmanos.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Noticia o articulo del blog de la organizacion.
 */
@Entity
@Table(name = "noticias")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, unique = true, length = 220)
    private String slug;

    @Column(nullable = false, length = 500)
    private String resumen;

    @Lob
    @Column(nullable = false)
    private String contenido;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(length = 60)
    private String categoria;

    @Column(length = 100)
    private String autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id")
    private Pais pais;

    @Builder.Default
    @Column(nullable = false)
    private boolean publicada = false;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Builder.Default
    @Column(nullable = false)
    private boolean destacada = false;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
