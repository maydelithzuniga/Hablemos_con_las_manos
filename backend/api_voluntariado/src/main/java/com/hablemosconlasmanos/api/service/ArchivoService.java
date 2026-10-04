package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.ArchivoDTO;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Guarda los archivos subidos en disco.
 * <ul>
 *   <li>publico/imagenes y publico/documentos: se sirven en /uploads/** (fotos, logos, memorias PDF).</li>
 *   <li>privado/cv: CVs de postulantes, solo descargables desde el panel de administracion.</li>
 * </ul>
 * El tipo real se valida leyendo los primeros bytes del archivo (no se confia en la extension).
 */
@Service
public class ArchivoService {

    private static final Pattern NOMBRE_SEGURO = Pattern.compile("^[a-f0-9-]{36}\\.(pdf)$");

    private final Path raiz;
    private final String urlPublica;

    public ArchivoService(@Value("${app.archivos.directorio}") String directorio,
                          @Value("${app.archivos.url-publica}") String urlPublica) {
        this.raiz = Path.of(directorio).toAbsolutePath().normalize();
        this.urlPublica = urlPublica.replaceAll("/+$", "");
    }

    public ArchivoDTO guardarImagen(MultipartFile archivo) {
        String extension = detectarTipo(archivo);
        if (extension.equals("pdf")) {
            throw new IllegalArgumentException("Solo se permiten imágenes JPG, PNG, WEBP o GIF");
        }
        return guardarPublico(archivo, "imagenes", extension);
    }

    public ArchivoDTO guardarDocumentoPublico(MultipartFile archivo) {
        exigirPdf(archivo);
        return guardarPublico(archivo, "documentos", "pdf");
    }

    /** Guarda el CV en la carpeta privada y devuelve la referencia "cv/<nombre>". */
    public ArchivoDTO guardarCv(MultipartFile archivo) {
        exigirPdf(archivo);
        String nombre = UUID.randomUUID() + ".pdf";
        escribir(archivo, raiz.resolve("privado").resolve("cv").resolve(nombre));
        return new ArchivoDTO(nombre, "cv/" + nombre, archivo.getSize(), "application/pdf");
    }

    public Resource obtenerCv(String nombre) {
        if (!NOMBRE_SEGURO.matcher(nombre).matches()) {
            throw new RecursoNoEncontradoException("Archivo no encontrado");
        }
        Path ruta = raiz.resolve("privado").resolve("cv").resolve(nombre);
        if (!Files.exists(ruta)) {
            throw new RecursoNoEncontradoException("Archivo no encontrado");
        }
        return new PathResource(ruta);
    }

    private ArchivoDTO guardarPublico(MultipartFile archivo, String carpeta, String extension) {
        String nombre = UUID.randomUUID() + "." + extension;
        escribir(archivo, raiz.resolve("publico").resolve(carpeta).resolve(nombre));
        String tipo = extension.equals("pdf") ? "application/pdf" : "image/" + (extension.equals("jpg") ? "jpeg" : extension);
        return new ArchivoDTO(nombre, urlPublica + "/" + carpeta + "/" + nombre, archivo.getSize(), tipo);
    }

    private void exigirPdf(MultipartFile archivo) {
        if (!detectarTipo(archivo).equals("pdf")) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF");
        }
    }

    private void escribir(MultipartFile archivo, Path destino) {
        try (InputStream in = archivo.getInputStream()) {
            Files.createDirectories(destino.getParent());
            Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar el archivo", e);
        }
    }

    /** Detecta el tipo de archivo por su "firma" (magic bytes). */
    private String detectarTipo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Debes adjuntar un archivo");
        }
        byte[] cabecera = new byte[12];
        try (InputStream in = archivo.getInputStream()) {
            int leidos = in.readNBytes(cabecera, 0, cabecera.length);
            cabecera = Arrays.copyOf(cabecera, leidos);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer el archivo", e);
        }
        if (empiezaCon(cabecera, "%PDF".getBytes())) {
            return "pdf";
        }
        if (empiezaCon(cabecera, new byte[]{(byte) 0x89, 'P', 'N', 'G'})) {
            return "png";
        }
        if (empiezaCon(cabecera, new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF})) {
            return "jpg";
        }
        if (empiezaCon(cabecera, "GIF8".getBytes())) {
            return "gif";
        }
        if (cabecera.length >= 12 && empiezaCon(cabecera, "RIFF".getBytes())
                && new String(cabecera, 8, 4).equals("WEBP")) {
            return "webp";
        }
        throw new IllegalArgumentException("Tipo de archivo no permitido");
    }

    private static boolean empiezaCon(byte[] datos, byte[] prefijo) {
        if (datos.length < prefijo.length) {
            return false;
        }
        for (int i = 0; i < prefijo.length; i++) {
            if (datos[i] != prefijo[i]) {
                return false;
            }
        }
        return true;
    }
}
