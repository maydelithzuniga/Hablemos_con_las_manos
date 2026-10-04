package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.AliadoDTO;
import com.hablemosconlasmanos.api.dto.AliadoRequest;
import com.hablemosconlasmanos.api.dto.CifraImpactoDTO;
import com.hablemosconlasmanos.api.dto.CifraImpactoRequest;
import com.hablemosconlasmanos.api.dto.DocumentoTransparenciaDTO;
import com.hablemosconlasmanos.api.dto.DocumentoTransparenciaRequest;
import com.hablemosconlasmanos.api.dto.MiembroEquipoDTO;
import com.hablemosconlasmanos.api.dto.MiembroEquipoRequest;
import com.hablemosconlasmanos.api.dto.TestimonioDTO;
import com.hablemosconlasmanos.api.dto.TestimonioRequest;
import com.hablemosconlasmanos.api.entity.Aliado;
import com.hablemosconlasmanos.api.entity.AreaEquipo;
import com.hablemosconlasmanos.api.entity.CifraImpacto;
import com.hablemosconlasmanos.api.entity.DocumentoTransparencia;
import com.hablemosconlasmanos.api.entity.MiembroEquipo;
import com.hablemosconlasmanos.api.entity.Testimonio;
import com.hablemosconlasmanos.api.entity.TipoAliado;
import com.hablemosconlasmanos.api.entity.TipoDocumento;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.repository.AliadoRepository;
import com.hablemosconlasmanos.api.repository.CifraImpactoRepository;
import com.hablemosconlasmanos.api.repository.DocumentoTransparenciaRepository;
import com.hablemosconlasmanos.api.repository.MiembroEquipoRepository;
import com.hablemosconlasmanos.api.repository.TestimonioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Contenido institucional administrable: aliados, equipo, testimonios,
 * documentos de transparencia y cifras de impacto.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InstitucionalService {

    private final AliadoRepository aliadoRepository;
    private final MiembroEquipoRepository miembroRepository;
    private final TestimonioRepository testimonioRepository;
    private final DocumentoTransparenciaRepository documentoRepository;
    private final CifraImpactoRepository cifraRepository;
    private final ProgramaService programaService;

    // ----------------------------------------------------------- Aliados
    public List<AliadoDTO> aliadosActivos(TipoAliado tipo) {
        List<Aliado> aliados = tipo == null
                ? aliadoRepository.findByActivoTrueOrderByOrdenAscNombreAsc()
                : aliadoRepository.findByActivoTrueAndTipoOrderByOrdenAscNombreAsc(tipo);
        return aliados.stream().map(AliadoDTO::de).toList();
    }

    public List<AliadoDTO> aliadosTodos() {
        return aliadoRepository.findAll(Sort.by("orden", "nombre")).stream().map(AliadoDTO::de).toList();
    }

    @Transactional
    public AliadoDTO guardarAliado(Long id, AliadoRequest r) {
        Aliado a = id == null ? new Aliado() : buscar(aliadoRepository, id, "Aliado");
        a.setNombre(r.nombre());
        a.setLogoUrl(r.logoUrl());
        a.setSitioWeb(r.sitioWeb());
        a.setTipo(r.tipo());
        a.setDescripcion(r.descripcion());
        a.setOrden(r.orden() != null ? r.orden() : 0);
        a.setActivo(r.activo() == null || r.activo());
        return AliadoDTO.de(aliadoRepository.save(a));
    }

    @Transactional
    public void eliminarAliado(Long id) {
        aliadoRepository.delete(buscar(aliadoRepository, id, "Aliado"));
    }

    // ----------------------------------------------------------- Equipo
    public List<MiembroEquipoDTO> equipoActivo(AreaEquipo area) {
        List<MiembroEquipo> miembros = area == null
                ? miembroRepository.findByActivoTrueOrderByAreaAscOrdenAscNombreAsc()
                : miembroRepository.findByActivoTrueAndAreaOrderByOrdenAscNombreAsc(area);
        return miembros.stream().map(MiembroEquipoDTO::de).toList();
    }

    public List<MiembroEquipoDTO> equipoTodos() {
        return miembroRepository.findAll(Sort.by("area", "orden", "nombre")).stream().map(MiembroEquipoDTO::de).toList();
    }

    @Transactional
    public MiembroEquipoDTO guardarMiembro(Long id, MiembroEquipoRequest r) {
        MiembroEquipo m = id == null ? new MiembroEquipo() : buscar(miembroRepository, id, "Integrante");
        m.setNombre(r.nombre());
        m.setCargo(r.cargo());
        m.setArea(r.area());
        m.setFotoUrl(r.fotoUrl());
        m.setBiografia(r.biografia());
        m.setLinkedinUrl(r.linkedinUrl());
        m.setOrden(r.orden() != null ? r.orden() : 0);
        m.setActivo(r.activo() == null || r.activo());
        return MiembroEquipoDTO.de(miembroRepository.save(m));
    }

    @Transactional
    public void eliminarMiembro(Long id) {
        miembroRepository.delete(buscar(miembroRepository, id, "Integrante"));
    }

    // ----------------------------------------------------------- Testimonios
    public List<TestimonioDTO> testimoniosAprobados() {
        return testimonioRepository.findByAprobadoTrueOrderByCreadoEnDesc().stream().map(TestimonioDTO::de).toList();
    }

    public List<TestimonioDTO> testimonios(Boolean aprobado) {
        List<Testimonio> lista = aprobado == null
                ? testimonioRepository.findAll(Sort.by(Sort.Direction.DESC, "creadoEn"))
                : testimonioRepository.findByAprobadoOrderByCreadoEnDesc(aprobado);
        return lista.stream().map(TestimonioDTO::de).toList();
    }

    /** Testimonio enviado desde la web: siempre queda pendiente de moderacion. */
    @Transactional
    public TestimonioDTO enviarTestimonio(TestimonioRequest r) {
        return TestimonioDTO.de(testimonioRepository.save(construirTestimonio(new Testimonio(), r, false)));
    }

    @Transactional
    public TestimonioDTO guardarTestimonio(Long id, TestimonioRequest r) {
        Testimonio t = id == null ? new Testimonio() : buscar(testimonioRepository, id, "Testimonio");
        return TestimonioDTO.de(testimonioRepository.save(construirTestimonio(t, r, Boolean.TRUE.equals(r.aprobado()))));
    }

    @Transactional
    public TestimonioDTO aprobarTestimonio(Long id, boolean aprobado) {
        Testimonio t = buscar(testimonioRepository, id, "Testimonio");
        t.setAprobado(aprobado);
        return TestimonioDTO.de(t);
    }

    @Transactional
    public void eliminarTestimonio(Long id) {
        testimonioRepository.delete(buscar(testimonioRepository, id, "Testimonio"));
    }

    private Testimonio construirTestimonio(Testimonio t, TestimonioRequest r, boolean aprobado) {
        t.setNombre(r.nombre().trim());
        t.setRol(r.rol());
        t.setTexto(r.texto().trim());
        t.setFotoUrl(r.fotoUrl());
        t.setPrograma(r.programaId() != null ? programaService.buscar(r.programaId()) : null);
        t.setAprobado(aprobado);
        return t;
    }

    // ----------------------------------------------------------- Transparencia
    public List<DocumentoTransparenciaDTO> documentos(TipoDocumento tipo) {
        List<DocumentoTransparencia> docs = tipo == null
                ? documentoRepository.findAllByOrderByAnioDescTituloAsc()
                : documentoRepository.findByTipoOrderByAnioDesc(tipo);
        return docs.stream().map(DocumentoTransparenciaDTO::de).toList();
    }

    @Transactional
    public DocumentoTransparenciaDTO guardarDocumento(Long id, DocumentoTransparenciaRequest r) {
        DocumentoTransparencia d = id == null ? new DocumentoTransparencia() : buscar(documentoRepository, id, "Documento");
        d.setTitulo(r.titulo());
        d.setTipo(r.tipo());
        d.setAnio(r.anio());
        d.setArchivoUrl(r.archivoUrl());
        d.setDescripcion(r.descripcion());
        return DocumentoTransparenciaDTO.de(documentoRepository.save(d));
    }

    @Transactional
    public void eliminarDocumento(Long id) {
        documentoRepository.delete(buscar(documentoRepository, id, "Documento"));
    }

    // ----------------------------------------------------------- Cifras de impacto
    public List<CifraImpactoDTO> cifras() {
        return cifraRepository.findAllByOrderByOrdenAsc().stream().map(CifraImpactoDTO::de).toList();
    }

    @Transactional
    public CifraImpactoDTO guardarCifra(Long id, CifraImpactoRequest r) {
        CifraImpacto c = id == null ? new CifraImpacto() : buscar(cifraRepository, id, "Cifra");
        c.setEtiqueta(r.etiqueta());
        c.setValor(r.valor());
        c.setPrefijo(r.prefijo());
        c.setSufijo(r.sufijo());
        c.setIcono(r.icono());
        c.setOrden(r.orden() != null ? r.orden() : 0);
        return CifraImpactoDTO.de(cifraRepository.save(c));
    }

    @Transactional
    public void eliminarCifra(Long id) {
        cifraRepository.delete(buscar(cifraRepository, id, "Cifra"));
    }

    private static <T> T buscar(JpaRepository<T, Long> repo, Long id, String recurso) {
        return repo.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de(recurso, id));
    }
}
