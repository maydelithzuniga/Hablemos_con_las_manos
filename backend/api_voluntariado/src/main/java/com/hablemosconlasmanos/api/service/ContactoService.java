package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.ContactoRequest;
import com.hablemosconlasmanos.api.dto.EstadoMensajeRequest;
import com.hablemosconlasmanos.api.dto.MensajeContactoDTO;
import com.hablemosconlasmanos.api.dto.MensajeDTO;
import com.hablemosconlasmanos.api.dto.PaginaDTO;
import com.hablemosconlasmanos.api.entity.MensajeContacto;
import com.hablemosconlasmanos.api.entity.TipoMensaje;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.repository.MensajeContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactoService {

    private final MensajeContactoRepository mensajeRepository;
    private final NotificacionService notificacionService;

    @Transactional
    public MensajeDTO recibir(ContactoRequest r) {
        MensajeContacto m = mensajeRepository.save(MensajeContacto.builder()
                .nombre(r.nombre().trim())
                .email(r.email().trim())
                .telefono(r.telefono())
                .organizacion(r.organizacion())
                .tipo(r.tipo())
                .asunto(r.asunto().trim())
                .mensaje(r.mensaje().trim())
                .build());
        notificacionService.mensajeContactoRecibido(m);
        return new MensajeDTO("Gracias por escribirnos, te responderemos pronto");
    }

    public PaginaDTO<MensajeContactoDTO> buscar(TipoMensaje tipo, Boolean leido, int pagina, int tamanio) {
        PageRequest page = PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamanio, 1), 100),
                Sort.by(Sort.Direction.DESC, "creadoEn"));
        Page<MensajeContacto> resultado;
        if (tipo != null) {
            resultado = mensajeRepository.findByTipo(tipo, page);
        } else if (leido != null) {
            resultado = mensajeRepository.findByLeido(leido, page);
        } else {
            resultado = mensajeRepository.findAll(page);
        }
        return PaginaDTO.de(resultado, MensajeContactoDTO::de);
    }

    @Transactional
    public MensajeContactoDTO actualizarEstado(Long id, EstadoMensajeRequest r) {
        MensajeContacto m = buscar(id);
        if (r.leido() != null) {
            m.setLeido(r.leido());
        }
        if (r.respondido() != null) {
            m.setRespondido(r.respondido());
            if (r.respondido()) {
                m.setLeido(true);
            }
        }
        return MensajeContactoDTO.de(m);
    }

    @Transactional
    public MensajeContactoDTO obtenerYMarcarLeido(Long id) {
        MensajeContacto m = buscar(id);
        m.setLeido(true);
        return MensajeContactoDTO.de(m);
    }

    @Transactional
    public void eliminar(Long id) {
        mensajeRepository.delete(buscar(id));
    }

    private MensajeContacto buscar(Long id) {
        return mensajeRepository.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("Mensaje", id));
    }
}
