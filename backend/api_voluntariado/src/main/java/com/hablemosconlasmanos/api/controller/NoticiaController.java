package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.NoticiaDTO;
import com.hablemosconlasmanos.api.dto.NoticiaResumenDTO;
import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.service.NoticiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/noticias")
@RequiredArgsConstructor
public class NoticiaController {

    private final NoticiaService noticiaService;

    @GetMapping
    public PaginaDTO<NoticiaResumenDTO> buscar(@RequestParam(required = false) String categoria,
                                               @RequestParam(required = false, name = "q") String texto,
                                               @RequestParam(defaultValue = "0") int pagina,
                                               @RequestParam(defaultValue = "9") int tamanio) {
        return noticiaService.buscarPublicadas(categoria, texto, pagina, tamanio);
    }

    @GetMapping("/categorias")
    public List<String> categorias() {
        return noticiaService.categorias();
    }

    @GetMapping("/{slug}")
    public NoticiaDTO obtener(@PathVariable String slug) {
        return noticiaService.obtenerPublicada(slug);
    }
}
