package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.dto.ProyectoDTO;
import com.hablemosconlasmanos.api.dto.ProyectoRequest;
import com.hablemosconlasmanos.api.entity.AreaTematica;
import com.hablemosconlasmanos.api.entity.EstadoProyecto;
import com.hablemosconlasmanos.api.entity.Proyecto;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.repository.ProyectoRepository;
import com.hablemosconlasmanos.api.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final PaisService paisService;
    private final ProgramaService programaService;

    public PaginaDTO<ProyectoDTO> buscar(String pais, AreaTematica area, EstadoProyecto estado, int pagina, int tamanio) {
        PageRequest page = PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamanio, 1), 50),
                Sort.by(Sort.Direction.DESC, "fechaInicio").and(Sort.by("titulo")));
        return PaginaDTO.de(proyectoRepository.buscar(vacioANull(pais), area, estado, page), ProyectoDTO::de);
    }

    public List<ProyectoDTO> destacados() {
        return proyectoRepository.findDestacados().stream().map(ProyectoDTO::de).toList();
    }

    public ProyectoDTO obtenerPorSlug(String slug) {
        return proyectoRepository.findBySlug(slug).map(ProyectoDTO::de)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Proyecto", slug));
    }

    public ProyectoDTO obtener(Long id) {
        return ProyectoDTO.de(buscarEntidad(id));
    }

    @Transactional
    public ProyectoDTO crear(ProyectoRequest request) {
        validarFechas(request);
        Proyecto proyecto = new Proyecto();
        proyecto.setSlug(SlugUtil.generarUnico(request.titulo(), proyectoRepository::existsBySlug));
        aplicar(proyecto, request);
        return ProyectoDTO.de(proyectoRepository.save(proyecto));
    }

    @Transactional
    public ProyectoDTO actualizar(Long id, ProyectoRequest request) {
        validarFechas(request);
        Proyecto proyecto = buscarEntidad(id);
        aplicar(proyecto, request);
        return ProyectoDTO.de(proyecto);
    }

    @Transactional
    public void eliminar(Long id) {
        proyectoRepository.delete(buscarEntidad(id));
    }

    public Proyecto buscarEntidad(Long id) {
        return proyectoRepository.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("Proyecto", id));
    }

    private void validarFechas(ProyectoRequest r) {
        if (r.fechaInicio() != null && r.fechaFin() != null && r.fechaFin().isBefore(r.fechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }
    }

    private void aplicar(Proyecto p, ProyectoRequest r) {
        p.setTitulo(r.titulo());
        p.setResumen(r.resumen());
        p.setDescripcion(r.descripcion());
        p.setPais(paisService.buscar(r.paisId()));
        p.setPrograma(r.programaId() != null ? programaService.buscar(r.programaId()) : null);
        p.setArea(r.area());
        p.setEstado(r.estado() != null ? r.estado() : EstadoProyecto.EN_CURSO);
        p.setSocioLocal(r.socioLocal());
        p.setFechaInicio(r.fechaInicio());
        p.setFechaFin(r.fechaFin());
        p.setBeneficiarios(r.beneficiarios() != null ? r.beneficiarios() : 0);
        p.setImagenUrl(r.imagenUrl());
        p.setDestacado(Boolean.TRUE.equals(r.destacado()));
    }

    private static String vacioANull(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
