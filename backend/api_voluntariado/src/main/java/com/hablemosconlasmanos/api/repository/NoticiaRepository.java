package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {

    @Query("""
            select n from Noticia n
            where n.publicada = true
              and (:categoria is null or lower(n.categoria) = lower(:categoria))
              and (lower(n.titulo) like lower(concat('%', :texto, '%'))
                   or lower(n.resumen) like lower(concat('%', :texto, '%')))
            """)
    Page<Noticia> buscarPublicadas(@Param("categoria") String categoria,
                                   @Param("texto") String texto,
                                   Pageable pageable);

    Optional<Noticia> findBySlugAndPublicadaTrue(String slug);

    List<Noticia> findTop3ByPublicadaTrueOrderByFechaPublicacionDesc();

    boolean existsBySlug(String slug);

    @Query("select distinct n.categoria from Noticia n where n.publicada = true and n.categoria is not null order by n.categoria")
    List<String> findCategorias();
}
