package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.CifraImpactoDTO;
import com.hablemosconlasmanos.api.dto.CifraImpactoRequest;
import com.hablemosconlasmanos.api.service.InstitucionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cifras")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class AdminCifraController {

    private final InstitucionalService institucionalService;

    @GetMapping
    public List<CifraImpactoDTO> listar() {
        return institucionalService.cifras();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CifraImpactoDTO crear(@Valid @RequestBody CifraImpactoRequest request) {
        return institucionalService.guardarCifra(null, request);
    }

    @PutMapping("/{id}")
    public CifraImpactoDTO actualizar(@PathVariable Long id, @Valid @RequestBody CifraImpactoRequest request) {
        return institucionalService.guardarCifra(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        institucionalService.eliminarCifra(id);
    }
}
