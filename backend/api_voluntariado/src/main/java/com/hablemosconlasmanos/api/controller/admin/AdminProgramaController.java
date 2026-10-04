package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.ProgramaDTO;
import com.hablemosconlasmanos.api.dto.ProgramaRequest;
import com.hablemosconlasmanos.api.service.ProgramaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/programas")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class AdminProgramaController {

    private final ProgramaService programaService;

    @GetMapping
    public List<ProgramaDTO> listar() {
        return programaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ProgramaDTO obtener(@PathVariable Long id) {
        return programaService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProgramaDTO crear(@Valid @RequestBody ProgramaRequest request) {
        return programaService.crear(request);
    }

    @PutMapping("/{id}")
    public ProgramaDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProgramaRequest request) {
        return programaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        programaService.eliminar(id);
    }
}
