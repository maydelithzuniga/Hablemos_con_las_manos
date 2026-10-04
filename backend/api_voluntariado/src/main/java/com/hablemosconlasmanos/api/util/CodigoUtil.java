package com.hablemosconlasmanos.api.util;

import java.security.SecureRandom;

/** Generacion de codigos y tokens aleatorios. */
public final class CodigoUtil {

    private static final SecureRandom RANDOM = new SecureRandom();
    /** Sin caracteres ambiguos (0/O, 1/I) para que sean faciles de dictar. */
    private static final String ALFABETO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final String HEX = "0123456789abcdef";

    private CodigoUtil() {
    }

    public static String codigo(int longitud) {
        return aleatorio(ALFABETO, longitud);
    }

    public static String token(int longitud) {
        return aleatorio(HEX, longitud);
    }

    private static String aleatorio(String alfabeto, int longitud) {
        StringBuilder sb = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            sb.append(alfabeto.charAt(RANDOM.nextInt(alfabeto.length())));
        }
        return sb.toString();
    }
}
