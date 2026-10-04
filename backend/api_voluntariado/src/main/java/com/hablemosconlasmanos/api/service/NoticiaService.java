package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.NoticiaDTO;
import com.hablemosconlasmanos.api.dto.NoticiaRequest;
import com.hablemosconlasmanos.api.dto.NoticiaResumenDTO;
import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.entity.Noticia;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.repository.NoticiaRepository;
import com.hablemosconlasmanos.api.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticiaService {

    private final NoticiaRepository noticiaRepository;
    private final PaisService paisService;

    public PaginaDTO<NoticiaResumenDTO> buscarPublicadas(String categoria, String texto, int pagina, int tamanio) {
        PageRequest page = PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamanio, 1), 50),
                Sort.by(Sort.Direction.DESC, "fechaPublicacion"));
        String cat = categoria == null || categoria.isBlank() ? null : categoria.trim();
        return PaginaDTO.de(noticiaRepository.buscarPublicadas(cat, texto == null ? "" : texto.trim(), page),
                NoticiaResumenDTO::de);
    }

    public List<NoticiaResumenDTO> ultimas() {
        return noticiaRepository.findTop3ByPublicadaTrueOrderByFechaPublicacionDesc().stream()
                .map(NoticiaResumenDTO::de).toList();
    }

    public List<String> categorias() {
        return noticiaRepository.findCategorias();
    }

    public NoticiaDTO obtenerPublicada(String slug) {
        return noticiaRepository.findBySlugAndPublicadaTrue(slug).map(NoticiaDTO::de)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Noticia", slug));
    }

    /** Listado del panel: incluye borradores. */
    public PaginaDTO<NoticiaResumenDTO> listarTodas(int pagina, int tamanio) {
        PageRequest page = PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamanio, 1), 100),
                Sort.by(Sort.Direction.DESC, "creadoEn"));
        return PaginaDTO.de(noticiaRepository.findAll(page), NoticiaResumenDTO::de);
    }

    public NoticiaDTO obtener(Long id) {
        return NoticiaDTO.de(buscar(id));
    }

    @Transactional
    public NoticiaDTO crear(NoticiaRequest request) {
        Noticia noticia = new Noticia();
        noticia.setSlug(SlugUtil.generarUnico(request.titulo(), noticiaRepository::existsBySlug));
        aplicar(noticia, request);
        return NoticiaDTO.de(noticiaRepository.save(noticia));
    }

    @Transactional
    public NoticiaDTO actualizar(Long id, NoticiaRequest request) {
        Noticia noticia = buscar(id);
        aplicar(noticia, request);
        return NoticiaDTO.de(noticia);
    }

    @Transactional
    public void eliminar(Long id) {
        noticiaRepository.delete(buscar(id));
    }

    private Noticia buscar(Long id) {
        return noticiaRepository.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("Noticia", id));
    }

    private void aplicar(Noticia n, NoticiaRequest r) {
        n.setTitulo(r.titulo());
        n.setResumen(r.resumen());
        n.setContenido(r.contenido());
        n.setImagenUrl(r.imagenUrl());
        n.setCategoria(r.categoria());
        n.setAutor(r.autor());
        n.setPais(r.paisId() != null ? paisService.buscar(r.paisId()) : null);
        n.setDestacada(Boolean.TRUE.equals(r.destacada()));
        boolean publicar = Boolean.TRUE.equals(r.publicada());
        // La fecha de publicacion se fija la primera vez que se publica
        if (publicar && n.getFechaPublicacion() == null) {
            n.setFechaPublicacion(LocalDateTime.now());
        }
        n.setPublicada(publicar);
    }
}
