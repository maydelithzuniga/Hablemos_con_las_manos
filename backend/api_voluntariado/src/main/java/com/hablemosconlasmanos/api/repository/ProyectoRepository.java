package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    @Query(value = """
            select p from Proyecto p join fetch p.pais
            where (:pais is null or upper(p.pais.codigo) = upper(:pais))
              and (:area is null or p.area = :area)
              and (:estado is null or p.estado = :estado)
            """, countQuery = """
            select count(p) from Proyecto p
            where (:pais is null or upper(p.pais.codigo) = upper(:pais))
              and (:area is null or p.area = :area)
              and (:estado is null or p.estado = :estado)
            """)
    Page<Proyecto> buscar(@Param("pais") String pais,
                          @Param("area") AreaTematica area,
                          @Param("estado") EstadoProyecto estado,
                          Pageable pageable);

    @Query("select p from Proyecto p join fetch p.pais where p.destacado = true order by p.fechaInicio desc")
    List<Proyecto> findDestacados();

    Optional<Proyecto> findBySlug(String slug);

    boolean existsBySlug(String slug);

    @Query("select coalesce(sum(p.beneficiarios), 0) from Proyecto p")
    long sumarBeneficiarios();

    long countByEstado(EstadoProyecto estado);
}
