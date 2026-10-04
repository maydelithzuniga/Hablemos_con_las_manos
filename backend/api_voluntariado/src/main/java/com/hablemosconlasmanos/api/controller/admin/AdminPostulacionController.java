package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.CambioEstadoPostulacionRequest;
import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.dto.PostulacionDTO;
import com.hablemosconlasmanos.api.entity.EstadoPostulacion;
import com.hablemosconlasmanos.api.service.PostulacionService;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/postulaciones")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPostulacionController {

    private final PostulacionService postulacionService;

    @GetMapping
    public PaginaDTO<PostulacionDTO> buscar(@RequestParam(required = false) EstadoPostulacion estado,
                                            @RequestParam(required = false) Long convocatoriaId,
                                            @RequestParam(required = false, name = "q") String texto,
                                            @RequestParam(defaultValue = "0") int pagina,
                                            @RequestParam(defaultValue = "20") int tamanio) {
        return postulacionService.buscar(estado, convocatoriaId, texto, pagina, tamanio);
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar(@RequestParam(required = false) EstadoPostulacion estado,
                                           @RequestParam(required = false) Long convocatoriaId) {
        return CsvRespuesta.de("postulaciones", postulacionService.exportarCsv(estado, convocatoriaId));
    }

    @GetMapping("/{id}")
    public PostulacionDTO obtener(@PathVariable Long id) {
        return postulacionService.obtener(id);
    }

    /** Avanza la postulacion en el proceso de seleccion (y opcionalmente avisa por correo). */
    @PatchMapping("/{id}/estado")
    public PostulacionDTO cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambioEstadoPostulacionRequest request) {
        return postulacionService.cambiarEstado(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        postulacionService.eliminar(id);
    }
}
