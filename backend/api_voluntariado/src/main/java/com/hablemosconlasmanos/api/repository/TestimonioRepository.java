package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestimonioRepository extends JpaRepository<Testimonio, Long> {

    List<Testimonio> findByAprobadoTrueOrderByCreadoEnDesc();

    List<Testimonio> findByAprobadoOrderByCreadoEnDesc(boolean aprobado);
}
