package com.hablemosconlasmanos.api.exception;

/** Se lanza cuando un recurso solicitado no existe (responde 404). */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public static RecursoNoEncontradoException de(String recurso, Object id) {
        return new RecursoNoEncontradoException(recurso + " no encontrado(a): " + id);
    }
}
