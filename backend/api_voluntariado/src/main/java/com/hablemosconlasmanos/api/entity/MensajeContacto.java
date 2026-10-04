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
 * Mensaje recibido desde el formulario de contacto.
 */
@Entity
@Table(name = "mensajes_contacto")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MensajeContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(length = 30)
    private String telefono;

    /** Empresa u organizacion (para alianzas corporativas). */
    @Column(length = 150)
    private String organizacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMensaje tipo;

    @Column(nullable = false, length = 150)
    private String asunto;

    @Column(nullable = false, length = 3000)
    private String mensaje;

    @Builder.Default
    @Column(nullable = false)
    private boolean leido = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean respondido = false;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
