package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SuscriptorRepository extends JpaRepository<Suscriptor, Long> {

    Optional<Suscriptor> findByEmailIgnoreCase(String email);

    Optional<Suscriptor> findByTokenBaja(String tokenBaja);

    List<Suscriptor> findByActivoTrueOrderByCreadoEnDesc();

    long countByActivoTrue();
}
