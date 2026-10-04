package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.PaisDTO;
import com.hablemosconlasmanos.api.dto.PaisDetalleDTO;
import com.hablemosconlasmanos.api.service.PaisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/paises")
@RequiredArgsConstructor
public class PaisController {

    private final PaisService paisService;

    @GetMapping
    public List<PaisDTO> listar() {
        return paisService.listarActivos();
    }

    /** Detalle de un pais con sus proyectos y convocatorias abiertas. */
    @GetMapping("/{codigo}")
    public PaisDetalleDTO detalle(@PathVariable String codigo) {
        return paisService.detalle(codigo);
    }
}
