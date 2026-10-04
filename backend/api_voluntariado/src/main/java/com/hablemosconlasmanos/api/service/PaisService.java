package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.ConvocatoriaDTO;
import com.hablemosconlasmanos.api.dto.PaisDTO;
import com.hablemosconlasmanos.api.dto.PaisDetalleDTO;
import com.hablemosconlasmanos.api.dto.PaisRequest;
import com.hablemosconlasmanos.api.dto.ProyectoDTO;
import com.hablemosconlasmanos.api.entity.Pais;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.exception.ReglaNegocioException;
import com.hablemosconlasmanos.api.repository.ConvocatoriaRepository;
import com.hablemosconlasmanos.api.repository.PaisRepository;
import com.hablemosconlasmanos.api.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaisService {

    private final PaisRepository paisRepository;
    private final ProyectoRepository proyectoRepository;
    private final ConvocatoriaRepository convocatoriaRepository;

    public List<PaisDTO> listarActivos() {
        return paisRepository.findByActivoTrueOrderByNombreAsc().stream().map(PaisDTO::de).toList();
    }

    public List<PaisDTO> listarTodos() {
        return paisRepository.findAll(Sort.by("nombre")).stream().map(PaisDTO::de).toList();
    }

    public PaisDetalleDTO detalle(String codigo) {
        Pais pais = paisRepository.findByCodigoIgnoreCase(codigo)
                .filter(Pais::isActivo)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Pais", codigo));
        List<ProyectoDTO> proyectos = proyectoRepository
                .buscar(pais.getCodigo(), null, null, Pageable.ofSize(100)).stream()
                .map(ProyectoDTO::de).toList();
        List<ConvocatoriaDTO> convocatorias = convocatoriaRepository.findAbiertas(LocalDate.now()).stream()
                .filter(c -> c.getPais() != null && c.getPais().getId().equals(pais.getId()))
                .map(ConvocatoriaDTO::de).toList();
        return new PaisDetalleDTO(PaisDTO.de(pais), proyectos, convocatorias);
    }

    @Transactional
    public PaisDTO crear(PaisRequest request) {
        if (paisRepository.existsByCodigoIgnoreCase(request.codigo())) {
            throw new ReglaNegocioException("Ya existe un pais con el codigo " + request.codigo());
        }
        Pais pais = new Pais();
        aplicar(pais, request);
        return PaisDTO.de(paisRepository.save(pais));
    }

    @Transactional
    public PaisDTO actualizar(Long id, PaisRequest request) {
        Pais pais = buscar(id);
        paisRepository.findByCodigoIgnoreCase(request.codigo())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new ReglaNegocioException("Ya existe un pais con el codigo " + request.codigo());
                });
        aplicar(pais, request);
        return PaisDTO.de(pais);
    }

    @Transactional
    public void eliminar(Long id) {
        paisRepository.delete(buscar(id));
    }

    public Pais buscar(Long id) {
        return paisRepository.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("Pais", id));
    }

    private void aplicar(Pais pais, PaisRequest r) {
        pais.setNombre(r.nombre());
        pais.setCodigo(r.codigo().toUpperCase(Locale.ROOT));
        pais.setDescripcion(r.descripcion());
        pais.setImagenUrl(r.imagenUrl());
        pais.setActivo(r.activo() == null || r.activo());
    }
}
