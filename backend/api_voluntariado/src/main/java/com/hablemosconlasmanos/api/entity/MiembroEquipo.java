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
 * Integrante del equipo, directorio o consejo asesor.
 */
@Entity
@Table(name = "miembros_equipo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiembroEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 120)
    private String cargo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AreaEquipo area;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(length = 1000)
    private String biografia;

    @Column(name = "linkedin_url", length = 300)
    private String linkedinUrl;

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
