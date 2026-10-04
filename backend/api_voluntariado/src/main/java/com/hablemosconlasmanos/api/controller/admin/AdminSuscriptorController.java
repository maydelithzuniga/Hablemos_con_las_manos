package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.SuscriptorDTO;
import com.hablemosconlasmanos.api.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/suscriptores")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSuscriptorController {

    private final NewsletterService newsletterService;

    @GetMapping
    public List<SuscriptorDTO> listar() {
        return newsletterService.listarActivos();
    }

    /** CSV para importar en Mailchimp, Brevo, etc. */
    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar() {
        return CsvRespuesta.de("suscriptores", newsletterService.exportarCsv());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        newsletterService.eliminar(id);
    }
}
