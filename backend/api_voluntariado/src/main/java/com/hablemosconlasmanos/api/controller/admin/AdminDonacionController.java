package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.DonacionDTO;
import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.entity.EstadoDonacion;
import com.hablemosconlasmanos.api.entity.TipoDonacion;
import com.hablemosconlasmanos.api.service.DonacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/donaciones")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDonacionController {

    private final DonacionService donacionService;

    @GetMapping
    public PaginaDTO<DonacionDTO> buscar(@RequestParam(required = false) EstadoDonacion estado,
                                         @RequestParam(required = false) TipoDonacion tipo,
                                         @RequestParam(defaultValue = "0") int pagina,
                                         @RequestParam(defaultValue = "20") int tamanio) {
        return donacionService.buscar(estado, tipo, pagina, tamanio);
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar(@RequestParam(required = false) EstadoDonacion estado,
                                           @RequestParam(required = false) TipoDonacion tipo) {
        return CsvRespuesta.de("donaciones", donacionService.exportarCsv(estado, tipo));
    }

    /** Confirmacion manual (ej. transferencia o deposito bancario). */
    @PatchMapping("/{id}/estado")
    public DonacionDTO cambiarEstado(@PathVariable Long id, @RequestParam EstadoDonacion estado) {
        return donacionService.cambiarEstado(id, estado);
    }
}
