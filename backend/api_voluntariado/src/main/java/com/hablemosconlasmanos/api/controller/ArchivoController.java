package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.ArchivoDTO;
import com.hablemosconlasmanos.api.service.ArchivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/archivos")
@RequiredArgsConstructor
public class ArchivoController {

    private final ArchivoService archivoService;

    /**
     * Sube el CV (PDF, max 5MB) del postulante. Devuelve una referencia
     * ("cv/...") que se envia en el campo cvUrl de la postulacion.
     */
    @PostMapping(value = "/cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ArchivoDTO subirCv(@RequestParam("archivo") MultipartFile archivo) {
        return archivoService.guardarCv(archivo);
    }
}
