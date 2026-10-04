package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.MensajeDTO;
import com.hablemosconlasmanos.api.dto.SuscripcionRequest;
import com.hablemosconlasmanos.api.service.NewsletterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/newsletter")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;

    @PostMapping
    public MensajeDTO suscribir(@Valid @RequestBody SuscripcionRequest request) {
        return newsletterService.suscribir(request);
    }

    /** Baja desde el enlace del correo. */
    @DeleteMapping("/{token}")
    public MensajeDTO darDeBaja(@PathVariable String token) {
        return newsletterService.darDeBaja(token);
    }
}
