package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.PostulacionCreadaDTO;
import com.hablemosconlasmanos.api.dto.PostulacionRequest;
import com.hablemosconlasmanos.api.dto.SeguimientoPostulacionDTO;
import com.hablemosconlasmanos.api.service.PostulacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/postulaciones")
@RequiredArgsConstructor
public class PostulacionController {

    private final PostulacionService postulacionService;

    /** Formulario "Quiero ser voluntario/a". */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostulacionCreadaDTO postular(@Valid @RequestBody PostulacionRequest request) {
        return postulacionService.postular(request);
    }

    /** Consulta del estado con el codigo de seguimiento y el email usado al postular. */
    @GetMapping("/seguimiento/{codigo}")
    public SeguimientoPostulacionDTO seguimiento(@PathVariable String codigo, @RequestParam String email) {
        return postulacionService.seguimiento(codigo, email);
    }
}
