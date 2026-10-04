package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MiembroEquipoRepository extends JpaRepository<MiembroEquipo, Long> {

    List<MiembroEquipo> findByActivoTrueOrderByAreaAscOrdenAscNombreAsc();

    List<MiembroEquipo> findByActivoTrueAndAreaOrderByOrdenAscNombreAsc(AreaEquipo area);
}
