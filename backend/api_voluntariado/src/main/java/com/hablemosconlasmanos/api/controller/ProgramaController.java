package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.ProgramaDTO;
import com.hablemosconlasmanos.api.entity.TipoPrograma;
import com.hablemosconlasmanos.api.service.ProgramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/programas")
@RequiredArgsConstructor
public class ProgramaController {

    private final ProgramaService programaService;

    @GetMapping
    public List<ProgramaDTO> listar(@RequestParam(required = false) TipoPrograma tipo) {
        return programaService.listarActivos(tipo);
    }

    @GetMapping("/{slug}")
    public ProgramaDTO obtener(@PathVariable String slug) {
        return programaService.obtenerPorSlug(slug);
    }
}
