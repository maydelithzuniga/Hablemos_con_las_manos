package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    Optional<Postulacion> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    boolean existsByEmailIgnoreCaseAndConvocatoriaIdAndEstadoNotIn(String email, Long convocatoriaId,
                                                                  List<EstadoPostulacion> estados);

    long countByEstado(EstadoPostulacion estado);

    @Query("""
            select p from Postulacion p
            where (:estado is null or p.estado = :estado)
              and (:convocatoriaId is null or p.convocatoria.id = :convocatoriaId)
              and (lower(p.nombres) like lower(concat('%', :texto, '%'))
                   or lower(p.apellidos) like lower(concat('%', :texto, '%'))
                   or lower(p.email) like lower(concat('%', :texto, '%')))
            """)
    Page<Postulacion> buscar(@Param("estado") EstadoPostulacion estado,
                             @Param("convocatoriaId") Long convocatoriaId,
                             @Param("texto") String texto,
                             Pageable pageable);
}
