package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface DonacionRepository extends JpaRepository<Donacion, Long> {

    Optional<Donacion> findByReferencia(String referencia);

    @Query("""
            select d from Donacion d
            where (:estado is null or d.estado = :estado)
              and (:tipo is null or d.tipo = :tipo)
            """)
    Page<Donacion> buscar(@Param("estado") EstadoDonacion estado,
                          @Param("tipo") TipoDonacion tipo,
                          Pageable pageable);

    @Query("""
            select coalesce(sum(d.monto), 0) from Donacion d
            where d.estado = :estado and d.moneda = :moneda and d.fechaPago >= :desde
            """)
    BigDecimal sumarMonto(@Param("estado") EstadoDonacion estado,
                          @Param("moneda") String moneda,
                          @Param("desde") LocalDateTime desde);

    long countByEstado(EstadoDonacion estado);

    @Query("select count(distinct lower(d.email)) from Donacion d where d.tipo = :tipo and d.estado = :estado")
    long contarDonantes(@Param("tipo") TipoDonacion tipo, @Param("estado") EstadoDonacion estado);
}
