package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.dto.ProyectoDTO;
import com.hablemosconlasmanos.api.dto.ProyectoRequest;
import com.hablemosconlasmanos.api.entity.AreaTematica;
import com.hablemosconlasmanos.api.entity.EstadoProyecto;
import com.hablemosconlasmanos.api.service.ProyectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/proyectos")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class AdminProyectoController {

    private final ProyectoService proyectoService;

    @GetMapping
    public PaginaDTO<ProyectoDTO> listar(@RequestParam(required = false) String pais,
                                         @RequestParam(required = false) AreaTematica area,
                                         @RequestParam(required = false) EstadoProyecto estado,
                                         @RequestParam(defaultValue = "0") int pagina,
                                         @RequestParam(defaultValue = "20") int tamanio) {
        return proyectoService.buscar(pais, area, estado, pagina, tamanio);
    }

    @GetMapping("/{id}")
    public ProyectoDTO obtener(@PathVariable Long id) {
        return proyectoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProyectoDTO crear(@Valid @RequestBody ProyectoRequest request) {
        return proyectoService.crear(request);
    }

    @PutMapping("/{id}")
    public ProyectoDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProyectoRequest request) {
        return proyectoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
    }
}
