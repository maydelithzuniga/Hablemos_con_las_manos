package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.MiembroEquipoDTO;
import com.hablemosconlasmanos.api.dto.MiembroEquipoRequest;
import com.hablemosconlasmanos.api.service.InstitucionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/equipo")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class AdminEquipoController {

    private final InstitucionalService institucionalService;

    @GetMapping
    public List<MiembroEquipoDTO> listar() {
        return institucionalService.equipoTodos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MiembroEquipoDTO crear(@Valid @RequestBody MiembroEquipoRequest request) {
        return institucionalService.guardarMiembro(null, request);
    }

    @PutMapping("/{id}")
    public MiembroEquipoDTO actualizar(@PathVariable Long id, @Valid @RequestBody MiembroEquipoRequest request) {
        return institucionalService.guardarMiembro(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        institucionalService.eliminarMiembro(id);
    }
}
