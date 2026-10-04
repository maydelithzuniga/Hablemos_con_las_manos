package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Long> {

    Page<MensajeContacto> findByTipo(TipoMensaje tipo, Pageable pageable);

    Page<MensajeContacto> findByLeido(boolean leido, Pageable pageable);

    long countByLeidoFalse();
}
