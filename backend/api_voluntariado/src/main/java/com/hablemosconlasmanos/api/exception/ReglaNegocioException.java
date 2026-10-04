package com.hablemosconlasmanos.api.exception;

/** Se lanza cuando una operacion viola una regla de negocio (responde 409). */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
