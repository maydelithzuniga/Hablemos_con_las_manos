package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.ImpactoDTO;
import com.hablemosconlasmanos.api.dto.InicioDTO;
import com.hablemosconlasmanos.api.service.PortadaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InicioController {

    private final PortadaService portadaService;

    /** Todo el contenido de la portada en una sola llamada. */
    @GetMapping("/inicio")
    public InicioDTO inicio() {
        return portadaService.inicio();
    }

    /** Cifras de impacto ("+X voluntarios", "Y paises", ...). */
    @GetMapping("/impacto")
    public ImpactoDTO impacto() {
        return portadaService.impacto();
    }
}
