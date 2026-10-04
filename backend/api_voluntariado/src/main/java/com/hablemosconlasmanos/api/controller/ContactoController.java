package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.ContactoRequest;
import com.hablemosconlasmanos.api.dto.MensajeDTO;
import com.hablemosconlasmanos.api.service.ContactoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contacto")
@RequiredArgsConstructor
public class ContactoController {

    private final ContactoService contactoService;

    /** Formulario de contacto (consultas generales, empresas, prensa...). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MensajeDTO enviar(@Valid @RequestBody ContactoRequest request) {
        return contactoService.recibir(request);
    }
}
