package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.EstadoMensajeRequest;
import com.hablemosconlasmanos.api.dto.MensajeContactoDTO;
import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.entity.TipoMensaje;
import com.hablemosconlasmanos.api.service.ContactoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/mensajes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminMensajeController {

    private final ContactoService contactoService;

    @GetMapping
    public PaginaDTO<MensajeContactoDTO> buscar(@RequestParam(required = false) TipoMensaje tipo,
                                                @RequestParam(required = false) Boolean leido,
                                                @RequestParam(defaultValue = "0") int pagina,
                                                @RequestParam(defaultValue = "20") int tamanio) {
        return contactoService.buscar(tipo, leido, pagina, tamanio);
    }

    /** Ver el mensaje lo marca como leido. */
    @GetMapping("/{id}")
    public MensajeContactoDTO obtener(@PathVariable Long id) {
        return contactoService.obtenerYMarcarLeido(id);
    }

    @PatchMapping("/{id}")
    public MensajeContactoDTO actualizarEstado(@PathVariable Long id, @Valid @RequestBody EstadoMensajeRequest request) {
        return contactoService.actualizarEstado(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        contactoService.eliminar(id);
    }
}
