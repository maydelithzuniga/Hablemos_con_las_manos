package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.dto.ProyectoDTO;
import com.hablemosconlasmanos.api.entity.AreaTematica;
import com.hablemosconlasmanos.api.entity.EstadoProyecto;
import com.hablemosconlasmanos.api.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;

    @GetMapping
    public PaginaDTO<ProyectoDTO> buscar(@RequestParam(required = false) String pais,
                                         @RequestParam(required = false) AreaTematica area,
                                         @RequestParam(required = false) EstadoProyecto estado,
                                         @RequestParam(defaultValue = "0") int pagina,
                                         @RequestParam(defaultValue = "9") int tamanio) {
        return proyectoService.buscar(pais, area, estado, pagina, tamanio);
    }

    @GetMapping("/destacados")
    public List<ProyectoDTO> destacados() {
        return proyectoService.destacados();
    }

    @GetMapping("/{slug}")
    public ProyectoDTO obtener(@PathVariable String slug) {
        return proyectoService.obtenerPorSlug(slug);
    }
}
