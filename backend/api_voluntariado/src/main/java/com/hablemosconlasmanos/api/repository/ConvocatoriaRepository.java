package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Long> {

    @Query("""
            select c from Convocatoria c join fetch c.programa left join fetch c.pais
            where c.publicada = true and c.fechaApertura <= :hoy and c.fechaCierre >= :hoy
            order by c.fechaCierre asc
            """)
    List<Convocatoria> findAbiertas(@Param("hoy") LocalDate hoy);

    @Query("select c from Convocatoria c join fetch c.programa left join fetch c.pais order by c.fechaApertura desc")
    List<Convocatoria> findTodas();
}
