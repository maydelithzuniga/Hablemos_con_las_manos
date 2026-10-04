package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.AliadoDTO;
import com.hablemosconlasmanos.api.dto.DocumentoTransparenciaDTO;
import com.hablemosconlasmanos.api.dto.MensajeDTO;
import com.hablemosconlasmanos.api.dto.MiembroEquipoDTO;
import com.hablemosconlasmanos.api.dto.TestimonioDTO;
import com.hablemosconlasmanos.api.dto.TestimonioRequest;
import com.hablemosconlasmanos.api.entity.AreaEquipo;
import com.hablemosconlasmanos.api.entity.TipoAliado;
import com.hablemosconlasmanos.api.entity.TipoDocumento;
import com.hablemosconlasmanos.api.service.InstitucionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Secciones "Quienes somos", "Alianzas", "Testimonios" y "Transparencia". */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InstitucionalController {

    private final InstitucionalService institucionalService;

    @GetMapping("/aliados")
    public List<AliadoDTO> aliados(@RequestParam(required = false) TipoAliado tipo) {
        return institucionalService.aliadosActivos(tipo);
    }

    @GetMapping("/equipo")
    public List<MiembroEquipoDTO> equipo(@RequestParam(required = false) AreaEquipo area) {
        return institucionalService.equipoActivo(area);
    }

    @GetMapping("/testimonios")
    public List<TestimonioDTO> testimonios() {
        return institucionalService.testimoniosAprobados();
    }

    /** Un voluntario comparte su experiencia; se publica cuando el equipo lo aprueba. */
    @PostMapping("/testimonios")
    @ResponseStatus(HttpStatus.CREATED)
    public MensajeDTO enviarTestimonio(@Valid @RequestBody TestimonioRequest request) {
        institucionalService.enviarTestimonio(request);
        return new MensajeDTO("Gracias por compartir tu experiencia! La publicaremos tras revisarla");
    }

    @GetMapping("/transparencia")
    public List<DocumentoTransparenciaDTO> transparencia(@RequestParam(required = false) TipoDocumento tipo) {
        return institucionalService.documentos(tipo);
    }
}
