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
 * Cifra de impacto editable que se muestra en la portada (ej. "+5.000 voluntarios").
 */
@Entity
@Table(name = "cifras_impacto")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CifraImpacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String etiqueta;

    @Column(nullable = false)
    private Long valor;

    /** Prefijo/sufijo opcional para mostrar, ej. "+" o "%". */
    @Column(length = 10)
    private String prefijo;

    @Column(length = 10)
    private String sufijo;

    @Column(length = 50)
    private String icono;

    @Builder.Default
    @Column(nullable = false)
    private Integer orden = 0;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
