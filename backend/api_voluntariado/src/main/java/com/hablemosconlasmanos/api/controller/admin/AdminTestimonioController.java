package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.TestimonioDTO;
import com.hablemosconlasmanos.api.dto.TestimonioRequest;
import com.hablemosconlasmanos.api.service.InstitucionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/testimonios")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class AdminTestimonioController {

    private final InstitucionalService institucionalService;

    /** ?aprobado=false para ver los pendientes de moderacion. */
    @GetMapping
    public List<TestimonioDTO> listar(@RequestParam(required = false) Boolean aprobado) {
        return institucionalService.testimonios(aprobado);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TestimonioDTO crear(@Valid @RequestBody TestimonioRequest request) {
        return institucionalService.guardarTestimonio(null, request);
    }

    @PutMapping("/{id}")
    public TestimonioDTO actualizar(@PathVariable Long id, @Valid @RequestBody TestimonioRequest request) {
        return institucionalService.guardarTestimonio(id, request);
    }

    @PatchMapping("/{id}/aprobacion")
    public TestimonioDTO aprobar(@PathVariable Long id, @RequestParam boolean aprobado) {
        return institucionalService.aprobarTestimonio(id, aprobado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        institucionalService.eliminarTestimonio(id);
    }
}
