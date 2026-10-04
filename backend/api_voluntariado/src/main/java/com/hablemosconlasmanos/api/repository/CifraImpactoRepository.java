package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CifraImpactoRepository extends JpaRepository<CifraImpacto, Long> {

    List<CifraImpacto> findAllByOrderByOrdenAsc();
}
