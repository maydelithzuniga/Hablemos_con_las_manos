package com.hablemosconlasmanos.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Donacion unica o aporte mensual de un socio.
 */
@Entity
@Table(name = "donaciones")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Donacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Referencia unica enviada a la pasarela de pago. */
    @Column(nullable = false, unique = true, length = 40)
    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoDonacion tipo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, length = 3)
    private String moneda;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(length = 30)
    private String telefono;

    @Column(name = "documento_identidad", length = 30)
    private String documentoIdentidad;

    @Column(length = 80)
    private String pais;

    @Column(length = 500)
    private String mensaje;

    @Builder.Default
    @Column(nullable = false)
    private boolean anonima = false;

    /** Destino opcional de la donacion (un proyecto especifico). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoDonacion estado = EstadoDonacion.PENDIENTE;

    @Column(name = "metodo_pago", length = 40)
    private String metodoPago;

    /** Identificador de la transaccion devuelto por la pasarela. */
    @Column(name = "id_transaccion", length = 120)
    private String idTransaccion;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
