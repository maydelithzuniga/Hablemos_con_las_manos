package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoTransparenciaRepository extends JpaRepository<DocumentoTransparencia, Long> {

    List<DocumentoTransparencia> findAllByOrderByAnioDescTituloAsc();

    List<DocumentoTransparencia> findByTipoOrderByAnioDesc(TipoDocumento tipo);
}
