package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.MensajeDTO;
import com.hablemosconlasmanos.api.dto.SuscripcionRequest;
import com.hablemosconlasmanos.api.dto.SuscriptorDTO;
import com.hablemosconlasmanos.api.entity.Suscriptor;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.repository.SuscriptorRepository;
import com.hablemosconlasmanos.api.util.CodigoUtil;
import com.hablemosconlasmanos.api.util.CsvUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsletterService {

    private final SuscriptorRepository suscriptorRepository;
    private final NotificacionService notificacionService;

    @Value("${app.newsletter.url-baja:http://localhost:5173/newsletter/baja}")
    private String urlBaja;

    /** Suscribe (o reactiva) un email. Es idempotente: no revela si el email ya existia. */
    @Transactional
    public MensajeDTO suscribir(SuscripcionRequest r) {
        String email = r.email().trim().toLowerCase(Locale.ROOT);
        Suscriptor s = suscriptorRepository.findByEmailIgnoreCase(email).orElse(null);
        if (s == null) {
            s = suscriptorRepository.save(Suscriptor.builder()
                    .email(email)
                    .nombre(r.nombre())
                    .tokenBaja(CodigoUtil.token(32))
                    .build());
            notificacionService.suscripcionConfirmada(s, urlBaja + "?token=" + s.getTokenBaja());
        } else if (!s.isActivo()) {
            s.setActivo(true);
            notificacionService.suscripcionConfirmada(s, urlBaja + "?token=" + s.getTokenBaja());
        }
        return new MensajeDTO("¡Gracias por suscribirte a nuestro boletín!");
    }

    @Transactional
    public MensajeDTO darDeBaja(String token) {
        Suscriptor s = suscriptorRepository.findByTokenBaja(token)
                .orElseThrow(() -> new RecursoNoEncontradoException("El enlace de baja no es válido"));
        s.setActivo(false);
        return new MensajeDTO("Te diste de baja del boletín");
    }

    public List<SuscriptorDTO> listarActivos() {
        return suscriptorRepository.findByActivoTrueOrderByCreadoEnDesc().stream().map(SuscriptorDTO::de).toList();
    }

    public String exportarCsv() {
        return CsvUtil.construir(Arrays.asList("Email", "Nombre", "Fecha"),
                listarActivos().stream().map(s -> Arrays.<Object>asList(s.email(), s.nombre(), s.creadoEn())).toList());
    }

    @Transactional
    public void eliminar(Long id) {
        suscriptorRepository.delete(suscriptorRepository.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Suscriptor", id)));
    }
}
