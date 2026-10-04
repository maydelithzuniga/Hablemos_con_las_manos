package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.AliadoDTO;
import com.hablemosconlasmanos.api.dto.AliadoRequest;
import com.hablemosconlasmanos.api.service.InstitucionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/aliados")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class AdminAliadoController {

    private final InstitucionalService institucionalService;

    @GetMapping
    public List<AliadoDTO> listar() {
        return institucionalService.aliadosTodos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AliadoDTO crear(@Valid @RequestBody AliadoRequest request) {
        return institucionalService.guardarAliado(null, request);
    }

    @PutMapping("/{id}")
    public AliadoDTO actualizar(@PathVariable Long id, @Valid @RequestBody AliadoRequest request) {
        return institucionalService.guardarAliado(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        institucionalService.eliminarAliado(id);
    }
}
