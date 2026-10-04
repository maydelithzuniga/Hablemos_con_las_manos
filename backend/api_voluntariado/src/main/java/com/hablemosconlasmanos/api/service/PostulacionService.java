package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.CambioEstadoPostulacionRequest;
import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.dto.PostulacionCreadaDTO;
import com.hablemosconlasmanos.api.dto.PostulacionDTO;
import com.hablemosconlasmanos.api.dto.PostulacionRequest;
import com.hablemosconlasmanos.api.dto.SeguimientoPostulacionDTO;
import com.hablemosconlasmanos.api.entity.Convocatoria;
import com.hablemosconlasmanos.api.entity.EstadoPostulacion;
import com.hablemosconlasmanos.api.entity.Postulacion;
import com.hablemosconlasmanos.api.entity.Programa;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.exception.ReglaNegocioException;
import com.hablemosconlasmanos.api.repository.PostulacionRepository;
import com.hablemosconlasmanos.api.util.CodigoUtil;
import com.hablemosconlasmanos.api.util.CsvUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostulacionService {

    private static final List<EstadoPostulacion> ESTADOS_CERRADOS =
            List.of(EstadoPostulacion.RECHAZADA, EstadoPostulacion.RETIRADA);

    private final PostulacionRepository postulacionRepository;
    private final ConvocatoriaService convocatoriaService;
    private final ProgramaService programaService;
    private final NotificacionService notificacionService;

    @Transactional
    public PostulacionCreadaDTO postular(PostulacionRequest r) {
        Convocatoria convocatoria = null;
        Programa programa = null;
        String email = r.email().trim().toLowerCase(Locale.ROOT);

        if (r.convocatoriaId() != null) {
            convocatoria = convocatoriaService.buscar(r.convocatoriaId());
            if (!convocatoria.estaAbierta()) {
                throw new ReglaNegocioException("La convocatoria no se encuentra abierta");
            }
            if (postulacionRepository.existsByEmailIgnoreCaseAndConvocatoriaIdAndEstadoNotIn(
                    email, convocatoria.getId(), ESTADOS_CERRADOS)) {
                throw new ReglaNegocioException("Ya tienes una postulacion activa en esta convocatoria");
            }
            programa = convocatoria.getPrograma();
        } else if (r.programaId() != null) {
            programa = programaService.buscar(r.programaId());
            if (!programa.isActivo()) {
                throw new ReglaNegocioException("El programa no esta recibiendo postulaciones");
            }
        }
        if (r.cvUrl() != null && !r.cvUrl().isBlank() && !r.cvUrl().matches("^cv/[a-f0-9-]{36}\\.pdf$")) {
            throw new IllegalArgumentException("El CV debe subirse primero en /api/archivos/cv");
        }

        Postulacion p = Postulacion.builder()
                .codigo(generarCodigo())
                .convocatoria(convocatoria)
                .programa(programa)
                .nombres(r.nombres().trim())
                .apellidos(r.apellidos().trim())
                .email(email)
                .telefono(r.telefono())
                .documentoIdentidad(r.documentoIdentidad())
                .fechaNacimiento(r.fechaNacimiento())
                .paisResidencia(r.paisResidencia())
                .ciudad(r.ciudad())
                .profesion(r.profesion())
                .nivelLenguaSenas(r.nivelLenguaSenas())
                .experiencia(r.experiencia())
                .motivacion(r.motivacion())
                .disponibilidad(r.disponibilidad())
                .cvUrl(r.cvUrl() == null || r.cvUrl().isBlank() ? null : r.cvUrl())
                .aceptaPoliticaDatos(r.aceptaPoliticaDatos())
                .build();
        postulacionRepository.save(p);
        notificacionService.postulacionRecibida(p);
        return new PostulacionCreadaDTO(p.getCodigo(),
                "Recibimos tu postulacion. Guarda tu codigo de seguimiento: " + p.getCodigo());
    }

    /** Consulta publica: requiere el codigo y el email usado al postular. */
    public SeguimientoPostulacionDTO seguimiento(String codigo, String email) {
        return postulacionRepository.findByCodigo(codigo.trim().toUpperCase(Locale.ROOT))
                .filter(p -> email != null && p.getEmail().equalsIgnoreCase(email.trim()))
                .map(SeguimientoPostulacionDTO::de)
                .orElseThrow(() -> new RecursoNoEncontradoException("No encontramos una postulacion con esos datos"));
    }

    public PaginaDTO<PostulacionDTO> buscar(EstadoPostulacion estado, Long convocatoriaId, String texto,
                                            int pagina, int tamanio) {
        PageRequest page = PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamanio, 1), 100),
                Sort.by(Sort.Direction.DESC, "creadoEn"));
        return PaginaDTO.de(postulacionRepository.buscar(estado, convocatoriaId, texto == null ? "" : texto.trim(), page),
                PostulacionDTO::de);
    }

    public PostulacionDTO obtener(Long id) {
        return PostulacionDTO.de(buscar(id));
    }

    @Transactional
    public PostulacionDTO cambiarEstado(Long id, CambioEstadoPostulacionRequest r) {
        Postulacion p = buscar(id);
        boolean cambio = p.getEstado() != r.estado();
        p.setEstado(r.estado());
        if (r.notasInternas() != null) {
            p.setNotasInternas(r.notasInternas());
        }
        postulacionRepository.flush();
        if (cambio && r.notificar()) {
            notificacionService.estadoPostulacionCambiado(p);
        }
        return PostulacionDTO.de(p);
    }

    @Transactional
    public void eliminar(Long id) {
        postulacionRepository.delete(buscar(id));
    }

    public String exportarCsv(EstadoPostulacion estado, Long convocatoriaId) {
        List<PostulacionDTO> todas = postulacionRepository
                .buscar(estado, convocatoriaId, "", PageRequest.of(0, 10_000, Sort.by("creadoEn")))
                .map(PostulacionDTO::de).getContent();
        return CsvUtil.construir(
                Arrays.asList("Codigo", "Fecha", "Estado", "Postula a", "Nombres", "Apellidos", "Email", "Telefono",
                        "Documento", "Pais", "Ciudad", "Profesion", "Lengua de senas", "Disponibilidad", "CV"),
                todas.stream().map(p -> Arrays.<Object>asList(p.codigo(), p.creadoEn(), p.estado(), p.postulacionA(),
                        p.nombres(), p.apellidos(), p.email(), p.telefono(), p.documentoIdentidad(),
                        p.paisResidencia(), p.ciudad(), p.profesion(), p.nivelLenguaSenas(), p.disponibilidad(),
                        p.cvUrl())).toList());
    }

    private Postulacion buscar(Long id) {
        return postulacionRepository.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("Postulacion", id));
    }

    private String generarCodigo() {
        String codigo;
        do {
            codigo = "HM-" + CodigoUtil.codigo(8);
        } while (postulacionRepository.existsByCodigo(codigo));
        return codigo;
    }
}
