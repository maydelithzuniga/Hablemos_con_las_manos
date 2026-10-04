package com.hablemosconlasmanos.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

/** Sirve los archivos publicos subidos (imagenes y PDFs de transparencia) desde /uploads/**. */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.archivos.directorio}")
    private String directorio;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Solo la carpeta "publico"; los CV quedan en "privado" y se descargan desde el panel.
        String ubicacion = Path.of(directorio, "publico").toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler("/uploads/**").addResourceLocations(ubicacion);
    }
}
