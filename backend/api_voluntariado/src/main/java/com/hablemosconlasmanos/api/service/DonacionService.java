package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.DonacionCreadaDTO;
import com.hablemosconlasmanos.api.dto.DonacionDTO;
import com.hablemosconlasmanos.api.dto.DonacionRequest;
import com.hablemosconlasmanos.api.dto.EstadoDonacionDTO;
import com.hablemosconlasmanos.api.dto.NotificacionPagoRequest;
import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.dto.SuscripcionRequest;
import com.hablemosconlasmanos.api.entity.Donacion;
import com.hablemosconlasmanos.api.entity.EstadoDonacion;
import com.hablemosconlasmanos.api.entity.TipoDonacion;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.exception.ReglaNegocioException;
import com.hablemosconlasmanos.api.repository.DonacionRepository;
import com.hablemosconlasmanos.api.util.CodigoUtil;
import com.hablemosconlasmanos.api.util.CsvUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DonacionService {

    private final DonacionRepository donacionRepository;
    private final ProyectoService proyectoService;
    private final PasarelaPagoService pasarelaPago;
    private final NewsletterService newsletterService;
    private final NotificacionService notificacionService;

    @Value("${app.donaciones.webhook-secret}")
    private String webhookSecret;

    /** Registra la intencion de donar (PENDIENTE) y devuelve la URL de pago. */
    @Transactional
    public DonacionCreadaDTO iniciar(DonacionRequest r) {
        Donacion d = Donacion.builder()
                .referencia("DON-" + CodigoUtil.codigo(10))
                .tipo(r.tipo())
                .monto(r.monto())
                .moneda(r.moneda().toUpperCase(Locale.ROOT))
                .nombre(r.nombre().trim())
                .email(r.email().trim().toLowerCase(Locale.ROOT))
                .telefono(r.telefono())
                .documentoIdentidad(r.documentoIdentidad())
                .pais(r.pais())
                .mensaje(r.mensaje())
                .anonima(r.anonima())
                .proyecto(r.proyectoId() != null ? proyectoService.buscarEntidad(r.proyectoId()) : null)
                .build();
        donacionRepository.save(d);
        if (r.suscribirBoletin()) {
            newsletterService.suscribir(new SuscripcionRequest(d.getEmail(), d.getNombre()));
        }
        String urlPago = pasarelaPago.crearPago(d);
        return new DonacionCreadaDTO(d.getReferencia(), d.getEstado(), urlPago);
    }

    public EstadoDonacionDTO estado(String referencia) {
        return EstadoDonacionDTO.de(buscarPorReferencia(referencia));
    }

    /** Procesa la notificacion de la pasarela de pago, validando el secreto compartido. */
    @Transactional
    public void procesarNotificacion(String secreto, NotificacionPagoRequest r) {
        if (secreto == null || !MessageDigest.isEqual(
                secreto.getBytes(StandardCharsets.UTF_8), webhookSecret.getBytes(StandardCharsets.UTF_8))) {
            throw new org.springframework.security.access.AccessDeniedException("Firma de webhook invalida");
        }
        Donacion d = buscarPorReferencia(r.referencia());
        if (d.getEstado() == EstadoDonacion.COMPLETADA && r.estado() != EstadoDonacion.CANCELADA) {
            log.info("Notificacion repetida para la donacion {}, se ignora", d.getReferencia());
            return;
        }
        actualizarEstado(d, r.estado(), r.idTransaccion(), r.metodoPago());
    }

    public PaginaDTO<DonacionDTO> buscar(EstadoDonacion estado, TipoDonacion tipo, int pagina, int tamanio) {
        PageRequest page = PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamanio, 1), 100),
                Sort.by(Sort.Direction.DESC, "creadoEn"));
        return PaginaDTO.de(donacionRepository.buscar(estado, tipo, page), DonacionDTO::de);
    }

    /** Cambio manual desde el panel (ej. transferencia bancaria confirmada). */
    @Transactional
    public DonacionDTO cambiarEstado(Long id, EstadoDonacion estado) {
        Donacion d = donacionRepository.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("Donacion", id));
        if (d.getEstado() == estado) {
            throw new ReglaNegocioException("La donacion ya tiene el estado " + estado);
        }
        actualizarEstado(d, estado, d.getIdTransaccion(), d.getMetodoPago() != null ? d.getMetodoPago() : "MANUAL");
        return DonacionDTO.de(d);
    }

    public String exportarCsv(EstadoDonacion estado, TipoDonacion tipo) {
        var donaciones = donacionRepository.buscar(estado, tipo, PageRequest.of(0, 10_000, Sort.by("creadoEn")))
                .map(DonacionDTO::de).getContent();
        return CsvUtil.construir(
                Arrays.asList("Referencia", "Fecha", "Tipo", "Monto", "Moneda", "Estado", "Nombre", "Email",
                        "Documento", "Pais", "Proyecto", "Metodo de pago", "Transaccion", "Fecha de pago"),
                donaciones.stream().map(d -> Arrays.<Object>asList(d.referencia(), d.creadoEn(), d.tipo(), d.monto(),
                        d.moneda(), d.estado(), d.nombre(), d.email(), d.documentoIdentidad(), d.pais(),
                        d.proyectoTitulo(), d.metodoPago(), d.idTransaccion(), d.fechaPago())).toList());
    }

    private void actualizarEstado(Donacion d, EstadoDonacion estado, String idTransaccion, String metodoPago) {
        d.setEstado(estado);
        d.setIdTransaccion(idTransaccion);
        d.setMetodoPago(metodoPago);
        if (estado == EstadoDonacion.COMPLETADA) {
            d.setFechaPago(LocalDateTime.now());
            notificacionService.donacionConfirmada(d);
        }
    }

    private Donacion buscarPorReferencia(String referencia) {
        return donacionRepository.findByReferencia(referencia)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Donacion", referencia));
    }
}
