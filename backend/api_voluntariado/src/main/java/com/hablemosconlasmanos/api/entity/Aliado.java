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
 * Empresa u organizacion aliada que se muestra en la seccion de alianzas.
 */
@Entity
@Table(name = "aliados")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aliado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "sitio_web", length = 300)
    private String sitioWeb;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoAliado tipo;

    @Column(length = 500)
    private String descripcion;

    @Builder.Default
    @Column(nullable = false)
    private Integer orden = 0;

    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
