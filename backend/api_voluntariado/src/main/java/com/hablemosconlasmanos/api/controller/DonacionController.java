package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.DonacionCreadaDTO;
import com.hablemosconlasmanos.api.dto.DonacionRequest;
import com.hablemosconlasmanos.api.dto.EstadoDonacionDTO;
import com.hablemosconlasmanos.api.dto.NotificacionPagoRequest;
import com.hablemosconlasmanos.api.service.DonacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/donaciones")
@RequiredArgsConstructor
public class DonacionController {

    private final DonacionService donacionService;

    /** Inicia una donacion unica o mensual; el frontend redirige a urlPago. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DonacionCreadaDTO donar(@Valid @RequestBody DonacionRequest request) {
        return donacionService.iniciar(request);
    }

    /** Estado de la donacion para la pagina de agradecimiento. */
    @GetMapping("/{referencia}")
    public EstadoDonacionDTO estado(@PathVariable String referencia) {
        return donacionService.estado(referencia);
    }

    /** Notificacion de la pasarela de pago (protegida con el header X-Webhook-Secret). */
    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void webhook(@RequestHeader(name = "X-Webhook-Secret", required = false) String secreto,
                        @Valid @RequestBody NotificacionPagoRequest request) {
        donacionService.procesarNotificacion(secreto, request);
    }
}
