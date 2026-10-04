package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.ConvocatoriaDTO;
import com.hablemosconlasmanos.api.service.ConvocatoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/convocatorias")
@RequiredArgsConstructor
public class ConvocatoriaController {

    private final ConvocatoriaService convocatoriaService;

    /** Convocatorias abiertas para postular hoy. */
    @GetMapping
    public List<ConvocatoriaDTO> abiertas() {
        return convocatoriaService.listarAbiertas();
    }

    @GetMapping("/{id}")
    public ConvocatoriaDTO obtener(@PathVariable Long id) {
        return convocatoriaService.obtenerPublica(id);
    }
}
