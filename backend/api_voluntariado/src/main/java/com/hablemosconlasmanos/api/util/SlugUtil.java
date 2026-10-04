package com.hablemosconlasmanos.api.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.function.Predicate;

/** Genera slugs amigables para URLs a partir de titulos ("Voluntariado en Peru" -> "voluntariado-en-peru"). */
public final class SlugUtil {

    private SlugUtil() {
    }

    public static String generar(String texto) {
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        String slug = sinTildes.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (slug.length() > 150) {
            slug = slug.substring(0, 150).replaceAll("-$", "");
        }
        return slug.isEmpty() ? "item" : slug;
    }

    /** Genera un slug unico agregando un sufijo numerico si ya existe. */
    public static String generarUnico(String texto, Predicate<String> existe) {
        String base = generar(texto);
        String slug = base;
        int i = 2;
        while (existe.test(slug)) {
            slug = base + "-" + i++;
        }
        return slug;
    }
}
