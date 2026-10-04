package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.ConvocatoriaDTO;
import com.hablemosconlasmanos.api.dto.ConvocatoriaRequest;
import com.hablemosconlasmanos.api.entity.Convocatoria;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.repository.ConvocatoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConvocatoriaService {

    private final ConvocatoriaRepository convocatoriaRepository;
    private final ProgramaService programaService;
    private final PaisService paisService;

    public List<ConvocatoriaDTO> listarAbiertas() {
        return convocatoriaRepository.findAbiertas(LocalDate.now()).stream().map(ConvocatoriaDTO::de).toList();
    }

    public List<ConvocatoriaDTO> listarTodas() {
        return convocatoriaRepository.findTodas().stream().map(ConvocatoriaDTO::de).toList();
    }

    /** Detalle publico: solo convocatorias publicadas. */
    public ConvocatoriaDTO obtenerPublica(Long id) {
        Convocatoria c = buscar(id);
        if (!c.isPublicada()) {
            throw RecursoNoEncontradoException.de("Convocatoria", id);
        }
        return ConvocatoriaDTO.de(c);
    }

    public ConvocatoriaDTO obtener(Long id) {
        return ConvocatoriaDTO.de(buscar(id));
    }

    @Transactional
    public ConvocatoriaDTO crear(ConvocatoriaRequest request) {
        Convocatoria c = new Convocatoria();
        aplicar(c, request);
        return ConvocatoriaDTO.de(convocatoriaRepository.save(c));
    }

    @Transactional
    public ConvocatoriaDTO actualizar(Long id, ConvocatoriaRequest request) {
        Convocatoria c = buscar(id);
        aplicar(c, request);
        return ConvocatoriaDTO.de(c);
    }

    @Transactional
    public void eliminar(Long id) {
        convocatoriaRepository.delete(buscar(id));
    }

    public Convocatoria buscar(Long id) {
        return convocatoriaRepository.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("Convocatoria", id));
    }

    private void aplicar(Convocatoria c, ConvocatoriaRequest r) {
        if (r.fechaCierre().isBefore(r.fechaApertura())) {
            throw new IllegalArgumentException("La fecha de cierre no puede ser anterior a la de apertura");
        }
        c.setTitulo(r.titulo());
        c.setDescripcion(r.descripcion());
        c.setPrograma(programaService.buscar(r.programaId()));
        c.setPais(r.paisId() != null ? paisService.buscar(r.paisId()) : null);
        c.setFechaApertura(r.fechaApertura());
        c.setFechaCierre(r.fechaCierre());
        c.setFechaInicioVoluntariado(r.fechaInicioVoluntariado());
        c.setCupos(r.cupos());
        c.setPerfiles(r.perfiles());
        c.setPublicada(r.publicada() == null || r.publicada());
    }
}
