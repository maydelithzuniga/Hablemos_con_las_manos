package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.NoticiaDTO;
import com.hablemosconlasmanos.api.dto.NoticiaRequest;
import com.hablemosconlasmanos.api.dto.NoticiaResumenDTO;
import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.service.NoticiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/noticias")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class AdminNoticiaController {

    private final NoticiaService noticiaService;

    /** Incluye borradores (no publicadas). */
    @GetMapping
    public PaginaDTO<NoticiaResumenDTO> listar(@RequestParam(defaultValue = "0") int pagina,
                                               @RequestParam(defaultValue = "20") int tamanio) {
        return noticiaService.listarTodas(pagina, tamanio);
    }

    @GetMapping("/{id}")
    public NoticiaDTO obtener(@PathVariable Long id) {
        return noticiaService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoticiaDTO crear(@Valid @RequestBody NoticiaRequest request) {
        return noticiaService.crear(request);
    }

    @PutMapping("/{id}")
    public NoticiaDTO actualizar(@PathVariable Long id, @Valid @RequestBody NoticiaRequest request) {
        return noticiaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        noticiaService.eliminar(id);
    }
}
