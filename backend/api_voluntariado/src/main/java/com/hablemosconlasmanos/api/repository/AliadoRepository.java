package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AliadoRepository extends JpaRepository<Aliado, Long> {

    List<Aliado> findByActivoTrueOrderByOrdenAscNombreAsc();

    List<Aliado> findByActivoTrueAndTipoOrderByOrdenAscNombreAsc(TipoAliado tipo);
}
