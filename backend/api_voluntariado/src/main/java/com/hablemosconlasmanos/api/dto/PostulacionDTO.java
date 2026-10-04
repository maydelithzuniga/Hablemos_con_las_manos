package com.hablemosconlasmanos.api.dto;

import com.hablemosconlasmanos.api.entity.EstadoPostulacion;
import com.hablemosconlasmanos.api.entity.Postulacion;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Vista completa de una postulacion para el panel de administracion. */
public record PostulacionDTO(Long id, String codigo, Long convocatoriaId, Long programaId, String postulacionA,
                             String nombres, String apellidos, String email, String telefono,
                             String documentoIdentidad, LocalDate fechaNacimiento, String paisResidencia,
                             String ciudad, String profesion, String nivelLenguaSenas, String experiencia,
                             String motivacion, String disponibilidad, String cvUrl, EstadoPostulacion estado,
                             String notasInternas, LocalDateTime creadoEn, LocalDateTime actualizadoEn) {

    public static PostulacionDTO de(Postulacion p) {
        return new PostulacionDTO(p.getId(), p.getCodigo(),
                p.getConvocatoria() != null ? p.getConvocatoria().getId() : null,
                p.getPrograma() != null ? p.getPrograma().getId() : null,
                destino(p), p.getNombres(), p.getApellidos(), p.getEmail(), p.getTelefono(),
                p.getDocumentoIdentidad(), p.getFechaNacimiento(), p.getPaisResidencia(), p.getCiudad(),
                p.getProfesion(), p.getNivelLenguaSenas(), p.getExperiencia(), p.getMotivacion(),
                p.getDisponibilidad(), p.getCvUrl(), p.getEstado(), p.getNotasInternas(),
                p.getCreadoEn(), p.getActualizadoEn());
    }

    /** Texto legible de a que se postulo (convocatoria o programa). */
    static String destino(Postulacion p) {
        if (p.getConvocatoria() != null) {
            return p.getConvocatoria().getTitulo();
        }
        return p.getPrograma() != null ? p.getPrograma().getTitulo() : "Voluntariado general";
    }
}
