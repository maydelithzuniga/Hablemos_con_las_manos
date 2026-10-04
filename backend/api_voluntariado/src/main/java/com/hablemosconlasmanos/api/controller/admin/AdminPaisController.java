package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.PaisDTO;
import com.hablemosconlasmanos.api.dto.PaisRequest;
import com.hablemosconlasmanos.api.service.PaisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/paises")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class AdminPaisController {

    private final PaisService paisService;

    @GetMapping
    public List<PaisDTO> listar() {
        return paisService.listarTodos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaisDTO crear(@Valid @RequestBody PaisRequest request) {
        return paisService.crear(request);
    }

    @PutMapping("/{id}")
    public PaisDTO actualizar(@PathVariable Long id, @Valid @RequestBody PaisRequest request) {
        return paisService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        paisService.eliminar(id);
    }
}
