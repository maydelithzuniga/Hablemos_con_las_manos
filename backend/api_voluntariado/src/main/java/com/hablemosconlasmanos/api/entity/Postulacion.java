package com.hablemosconlasmanos.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Postulacion de una persona para ser voluntaria.
 */
@Entity
@Table(name = "postulaciones")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Codigo publico para que el postulante consulte el estado de su postulacion. */
    @Column(nullable = false, unique = true, length = 12)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "convocatoria_id")
    private Convocatoria convocatoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id")
    private Programa programa;

    @Column(nullable = false, length = 80)
    private String nombres;

    @Column(nullable = false, length = 80)
    private String apellidos;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(length = 30)
    private String telefono;

    @Column(name = "documento_identidad", length = 30)
    private String documentoIdentidad;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "pais_residencia", length = 80)
    private String paisResidencia;

    @Column(length = 80)
    private String ciudad;

    @Column(length = 120)
    private String profesion;

    /** Nivel de lengua de senas declarado (ninguno, basico, intermedio, avanzado). */
    @Column(name = "nivel_lengua_senas", length = 20)
    private String nivelLenguaSenas;

    @Column(length = 2000)
    private String experiencia;

    @Column(nullable = false, length = 2000)
    private String motivacion;

    @Column(length = 300)
    private String disponibilidad;

    @Column(name = "cv_url", length = 500)
    private String cvUrl;

    @Column(name = "acepta_politica_datos", nullable = false)
    private boolean aceptaPoliticaDatos;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPostulacion estado = EstadoPostulacion.RECIBIDA;

    /** Notas internas del equipo de seleccion (no se muestran al postulante). */
    @Column(name = "notas_internas", length = 2000)
    private String notasInternas;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
