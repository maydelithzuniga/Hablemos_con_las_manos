package com.hablemosconlasmanos.api.controller.admin;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/** Construye la respuesta HTTP de descarga de un CSV. */
final class CsvRespuesta {

    private CsvRespuesta() {
    }

    static ResponseEntity<byte[]> de(String nombreBase, String csv) {
        String nombre = nombreBase + "-" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombre + "\"")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(csv.getBytes(StandardCharsets.UTF_8));
    }
}
