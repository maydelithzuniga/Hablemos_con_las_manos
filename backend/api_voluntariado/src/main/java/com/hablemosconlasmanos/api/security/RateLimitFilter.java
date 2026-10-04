package com.hablemosconlasmanos.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Limita la cantidad de envios de formularios publicos (postulaciones,
 * contacto, donaciones, newsletter, login) por IP para frenar spam y
 * ataques de fuerza bruta. Ventana fija en memoria: suficiente para una
 * sola instancia; con varias instancias usar un limitador distribuido.
 */
public class RateLimitFilter extends OncePerRequestFilter {

    private static final long VENTANA_MS = 60_000;

    private final int maxPorMinuto;
    private final Map<String, Ventana> ventanas = new ConcurrentHashMap<>();

    public RateLimitFilter(int maxPorMinuto) {
        this.maxPorMinuto = maxPorMinuto;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String ruta = request.getRequestURI();
        return !HttpMethod.POST.matches(request.getMethod())
                || !ruta.startsWith("/api/")
                || ruta.startsWith("/api/admin/")
                || ruta.equals("/api/donaciones/webhook");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        long ahora = System.currentTimeMillis();
        if (ventanas.size() > 10_000) {
            ventanas.values().removeIf(v -> ahora - v.inicio > VENTANA_MS);
        }
        String clave = request.getRemoteAddr() + "|" + request.getRequestURI();
        Ventana ventana = ventanas.compute(clave, (k, v) ->
                v == null || ahora - v.inicio > VENTANA_MS ? new Ventana(ahora) : v.incrementar());
        if (ventana.contador > maxPorMinuto) {
            response.setStatus(429);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"estado\":429,\"error\":\"Too Many Requests\","
                    + "\"mensaje\":\"Demasiados intentos, espera un minuto e intentalo nuevamente\"}");
            return;
        }
        chain.doFilter(request, response);
    }

    private static final class Ventana {
        private final long inicio;
        private int contador = 1;

        private Ventana(long inicio) {
            this.inicio = inicio;
        }

        private Ventana incrementar() {
            contador++;
            return this;
        }
    }
}
