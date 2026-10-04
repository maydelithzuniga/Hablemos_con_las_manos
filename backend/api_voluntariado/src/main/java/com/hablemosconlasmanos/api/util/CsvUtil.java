package com.hablemosconlasmanos.api.util;

import java.util.List;
import java.util.stream.Collectors;

/** Construccion sencilla de archivos CSV para las exportaciones del panel. */
public final class CsvUtil {

    private CsvUtil() {
    }

    public static String construir(List<String> encabezados, List<List<Object>> filas) {
        StringBuilder sb = new StringBuilder("﻿"); // BOM para que Excel respete las tildes
        sb.append(linea(encabezados.stream().map(e -> (Object) e).toList()));
        filas.forEach(fila -> sb.append(linea(fila)));
        return sb.toString();
    }

    private static String linea(List<Object> valores) {
        return valores.stream().map(CsvUtil::escapar).collect(Collectors.joining(",")) + "\r\n";
    }

    private static String escapar(Object valor) {
        if (valor == null) {
            return "";
        }
        String texto = valor.toString();
        // Evita inyeccion de formulas al abrir el CSV en Excel
        if (!texto.isEmpty() && "=+-@".indexOf(texto.charAt(0)) >= 0) {
            texto = "'" + texto;
        }
        if (texto.contains(",") || texto.contains("\"") || texto.contains("\n") || texto.contains("\r")) {
            texto = "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }
}
