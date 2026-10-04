package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.ConvocatoriaDTO;
import com.hablemosconlasmanos.api.dto.ConvocatoriaRequest;
import com.hablemosconlasmanos.api.service.ConvocatoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/convocatorias")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class AdminConvocatoriaController {

    private final ConvocatoriaService convocatoriaService;

    @GetMapping
    public List<ConvocatoriaDTO> listar() {
        return convocatoriaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ConvocatoriaDTO obtener(@PathVariable Long id) {
        return convocatoriaService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConvocatoriaDTO crear(@Valid @RequestBody ConvocatoriaRequest request) {
        return convocatoriaService.crear(request);
    }

    @PutMapping("/{id}")
    public ConvocatoriaDTO actualizar(@PathVariable Long id, @Valid @RequestBody ConvocatoriaRequest request) {
        return convocatoriaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        convocatoriaService.eliminar(id);
    }
}
