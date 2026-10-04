package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.ArchivoDTO;
import com.hablemosconlasmanos.api.service.ArchivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/archivos")
@RequiredArgsConstructor
public class AdminArchivoController {

    private final ArchivoService archivoService;

    /** Imagen para noticias, proyectos, programas, equipo o logos de aliados. */
    @PostMapping(value = "/imagenes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ArchivoDTO subirImagen(@RequestParam("archivo") MultipartFile archivo) {
        return archivoService.guardarImagen(archivo);
    }

    /** PDF publico (memorias, estados financieros). */
    @PostMapping(value = "/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ArchivoDTO subirDocumento(@RequestParam("archivo") MultipartFile archivo) {
        return archivoService.guardarDocumentoPublico(archivo);
    }

    /** Descarga privada del CV de un postulante. */
    @GetMapping("/cv/{nombre}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> descargarCv(@PathVariable String nombre) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombre + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(archivoService.obtenerCv(nombre));
    }
}
