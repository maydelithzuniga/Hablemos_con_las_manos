package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramaRepository extends JpaRepository<Programa, Long> {

    List<Programa> findByActivoTrueOrderByTituloAsc();

    List<Programa> findByActivoTrueAndTipoOrderByTituloAsc(TipoPrograma tipo);

    List<Programa> findByActivoTrueAndDestacadoTrueOrderByTituloAsc();

    Optional<Programa> findBySlugAndActivoTrue(String slug);

    boolean existsBySlug(String slug);
}
