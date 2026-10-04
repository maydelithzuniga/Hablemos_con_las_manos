package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.DashboardDTO;
import com.hablemosconlasmanos.api.dto.ImpactoDTO;
import com.hablemosconlasmanos.api.dto.InicioDTO;
import com.hablemosconlasmanos.api.entity.EstadoDonacion;
import com.hablemosconlasmanos.api.entity.EstadoPostulacion;
import com.hablemosconlasmanos.api.entity.EstadoProyecto;
import com.hablemosconlasmanos.api.entity.TipoDonacion;
import com.hablemosconlasmanos.api.repository.ConvocatoriaRepository;
import com.hablemosconlasmanos.api.repository.DonacionRepository;
import com.hablemosconlasmanos.api.repository.MensajeContactoRepository;
import com.hablemosconlasmanos.api.repository.PaisRepository;
import com.hablemosconlasmanos.api.repository.PostulacionRepository;
import com.hablemosconlasmanos.api.repository.ProyectoRepository;
import com.hablemosconlasmanos.api.repository.SuscriptorRepository;
import com.hablemosconlasmanos.api.repository.TestimonioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Vistas agregadas: portada del sitio, cifras de impacto y dashboard del panel. */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortadaService {

    private static final List<String> MONEDAS = List.of("PEN", "USD");

    private final ProgramaService programaService;
    private final ProyectoService proyectoService;
    private final ConvocatoriaService convocatoriaService;
    private final NoticiaService noticiaService;
    private final InstitucionalService institucionalService;
    private final PaisRepository paisRepository;
    private final ProyectoRepository proyectoRepository;
    private final PostulacionRepository postulacionRepository;
    private final DonacionRepository donacionRepository;
    private final MensajeContactoRepository mensajeRepository;
    private final SuscriptorRepository suscriptorRepository;
    private final ConvocatoriaRepository convocatoriaRepository;
    private final TestimonioRepository testimonioRepository;

    public InicioDTO inicio() {
        return new InicioDTO(
                programaService.listarDestacados(),
                proyectoService.destacados(),
                convocatoriaService.listarAbiertas(),
                noticiaService.ultimas(),
                institucionalService.testimoniosAprobados().stream().limit(6).toList(),
                institucionalService.aliadosActivos(null),
                impacto());
    }

    public ImpactoDTO impacto() {
        return new ImpactoDTO(
                institucionalService.cifras(),
                paisRepository.countByActivoTrue(),
                proyectoRepository.countByEstado(EstadoProyecto.EN_CURSO),
                proyectoRepository.count(),
                proyectoRepository.sumarBeneficiarios(),
                postulacionRepository.countByEstado(EstadoPostulacion.ACEPTADA),
                donacionRepository.contarDonantes(TipoDonacion.MENSUAL, EstadoDonacion.COMPLETADA));
    }

    public DashboardDTO dashboard() {
        Map<String, Long> porEstado = new LinkedHashMap<>();
        for (EstadoPostulacion estado : EstadoPostulacion.values()) {
            porEstado.put(estado.name(), postulacionRepository.countByEstado(estado));
        }
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        Map<String, BigDecimal> recaudado = new LinkedHashMap<>();
        for (String moneda : MONEDAS) {
            recaudado.put(moneda, donacionRepository.sumarMonto(EstadoDonacion.COMPLETADA, moneda, hace30Dias));
        }
        return new DashboardDTO(
                porEstado,
                mensajeRepository.countByLeidoFalse(),
                suscriptorRepository.countByActivoTrue(),
                donacionRepository.countByEstado(EstadoDonacion.COMPLETADA),
                donacionRepository.countByEstado(EstadoDonacion.PENDIENTE),
                recaudado,
                donacionRepository.contarDonantes(TipoDonacion.MENSUAL, EstadoDonacion.COMPLETADA),
                convocatoriaRepository.findAbiertas(LocalDate.now()).size(),
                testimonioRepository.findByAprobadoOrderByCreadoEnDesc(false).size());
    }
}
