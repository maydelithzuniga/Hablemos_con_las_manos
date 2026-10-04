package com.hablemosconlasmanos.api.dto;

/** Resultado de subir un archivo: URL publica para guardarla en otro recurso. */
public record ArchivoDTO(String nombre, String url, long tamanio, String tipo) {
}
