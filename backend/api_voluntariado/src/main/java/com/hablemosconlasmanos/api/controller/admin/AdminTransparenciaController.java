package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.DocumentoTransparenciaDTO;
import com.hablemosconlasmanos.api.dto.DocumentoTransparenciaRequest;
import com.hablemosconlasmanos.api.service.InstitucionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/transparencia")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class AdminTransparenciaController {

    private final InstitucionalService institucionalService;

    @GetMapping
    public List<DocumentoTransparenciaDTO> listar() {
        return institucionalService.documentos(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentoTransparenciaDTO crear(@Valid @RequestBody DocumentoTransparenciaRequest request) {
        return institucionalService.guardarDocumento(null, request);
    }

    @PutMapping("/{id}")
    public DocumentoTransparenciaDTO actualizar(@PathVariable Long id, @Valid @RequestBody DocumentoTransparenciaRequest request) {
        return institucionalService.guardarDocumento(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        institucionalService.eliminarDocumento(id);
    }
}
