package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.ProgramaDTO;
import com.hablemosconlasmanos.api.dto.ProgramaRequest;
import com.hablemosconlasmanos.api.entity.Programa;
import com.hablemosconlasmanos.api.entity.TipoPrograma;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.repository.ProgramaRepository;
import com.hablemosconlasmanos.api.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgramaService {

    private final ProgramaRepository programaRepository;

    public List<ProgramaDTO> listarActivos(TipoPrograma tipo) {
        List<Programa> programas = tipo == null
                ? programaRepository.findByActivoTrueOrderByTituloAsc()
                : programaRepository.findByActivoTrueAndTipoOrderByTituloAsc(tipo);
        return programas.stream().map(ProgramaDTO::de).toList();
    }

    public List<ProgramaDTO> listarDestacados() {
        return programaRepository.findByActivoTrueAndDestacadoTrueOrderByTituloAsc().stream().map(ProgramaDTO::de).toList();
    }

    public List<ProgramaDTO> listarTodos() {
        return programaRepository.findAll(Sort.by("titulo")).stream().map(ProgramaDTO::de).toList();
    }

    public ProgramaDTO obtenerPorSlug(String slug) {
        return programaRepository.findBySlugAndActivoTrue(slug).map(ProgramaDTO::de)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Programa", slug));
    }

    public ProgramaDTO obtener(Long id) {
        return ProgramaDTO.de(buscar(id));
    }

    @Transactional
    public ProgramaDTO crear(ProgramaRequest request) {
        Programa programa = new Programa();
        programa.setSlug(SlugUtil.generarUnico(request.titulo(), programaRepository::existsBySlug));
        aplicar(programa, request);
        return ProgramaDTO.de(programaRepository.save(programa));
    }

    @Transactional
    public ProgramaDTO actualizar(Long id, ProgramaRequest request) {
        Programa programa = buscar(id);
        aplicar(programa, request);
        return ProgramaDTO.de(programa);
    }

    @Transactional
    public void eliminar(Long id) {
        programaRepository.delete(buscar(id));
    }

    public Programa buscar(Long id) {
        return programaRepository.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("Programa", id));
    }

    private void aplicar(Programa p, ProgramaRequest r) {
        p.setTitulo(r.titulo());
        p.setResumen(r.resumen());
        p.setDescripcion(r.descripcion());
        p.setTipo(r.tipo());
        p.setModalidad(r.modalidad());
        p.setDuracion(r.duracion());
        p.setRequisitos(r.requisitos());
        p.setBeneficios(r.beneficios());
        p.setImagenUrl(r.imagenUrl());
        p.setDestacado(Boolean.TRUE.equals(r.destacado()));
        p.setActivo(r.activo() == null || r.activo());
    }
}
