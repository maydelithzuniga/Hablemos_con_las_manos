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
 * Convocatoria (llamado) abierta para postular a un programa en un pais y periodo.
 */
@Entity
@Table(name = "convocatorias")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Convocatoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "programa_id")
    private Programa programa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id")
    private Pais pais;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    @Column(name = "fecha_cierre", nullable = false)
    private LocalDate fechaCierre;

    /** Fecha estimada de inicio del voluntariado. */
    @Column(name = "fecha_inicio_voluntariado")
    private LocalDate fechaInicioVoluntariado;

    private Integer cupos;

    /** Perfiles buscados, ej. "Docentes, psicologos, interpretes de lengua de senas". */
    @Column(length = 1000)
    private String perfiles;

    @Builder.Default
    @Column(nullable = false)
    private boolean publicada = true;

    /** Una convocatoria esta abierta si esta publicada y hoy esta dentro del rango de fechas. */
    public boolean estaAbierta() {
        LocalDate hoy = LocalDate.now();
        return publicada && !hoy.isBefore(fechaApertura) && !hoy.isAfter(fechaCierre);
    }

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
