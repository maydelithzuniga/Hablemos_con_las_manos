package com.hablemosconlasmanos.api;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiVoluntariadoApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void portadaDevuelveContenidoDeEjemplo() throws Exception {
        mvc.perform(get("/api/inicio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.programasDestacados.length()").value(greaterThan(0)))
                .andExpect(jsonPath("$.convocatoriasAbiertas.length()").value(greaterThan(0)))
                .andExpect(jsonPath("$.impacto.paisesActivos").value(4));
        mvc.perform(get("/api/proyectos").param("pais", "PE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos").value(1));
        mvc.perform(get("/api/paises/bo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.proyectos.length()").value(1));
        mvc.perform(get("/api/noticias").param("q", "alianza"))
                .andExpect(jsonPath("$.contenido.length()").value(1));
        mvc.perform(get("/api/programas/no-existe")).andExpect(status().isNotFound());
    }

    @Test
    void flujoDePostulacion() throws Exception {
        String convocatorias = mvc.perform(get("/api/convocatorias")).andReturn().getResponse().getContentAsString();
        Integer convocatoriaId = JsonPath.read(convocatorias, "$[0].id");
        String body = """
                {"convocatoriaId": %d, "nombres": "Lucia", "apellidos": "Perez", "email": "lucia@correo.com",
                 "motivacion": "Quiero aportar a la inclusion", "aceptaPoliticaDatos": true}
                """.formatted(convocatoriaId);

        String respuesta = mvc.perform(post("/api/postulaciones").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value(startsWith("HM-")))
                .andReturn().getResponse().getContentAsString();
        String codigo = JsonPath.read(respuesta, "$.codigo");

        mvc.perform(post("/api/postulaciones").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
        mvc.perform(get("/api/postulaciones/seguimiento/" + codigo).param("email", "LUCIA@correo.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RECIBIDA"));
        mvc.perform(get("/api/postulaciones/seguimiento/" + codigo).param("email", "otro@correo.com"))
                .andExpect(status().isNotFound());

        String token = login("admin@hablemosconlasmanos.org", "Admin12345");
        String lista = mvc.perform(get("/api/admin/postulaciones").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(lista, "$.contenido[0].id");
        mvc.perform(patch("/api/admin/postulaciones/" + id + "/estado").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\": \"ENTREVISTA\", \"notificar\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREVISTA"));
        mvc.perform(get("/api/admin/postulaciones/exportar").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", startsWith("attachment")));
    }

    @Test
    void validacionDeFormularios() throws Exception {
        mvc.perform(post("/api/contacto").contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"no-es-email\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.length()").value(greaterThan(0)));
        mvc.perform(post("/api/contacto").contentType(MediaType.APPLICATION_JSON).content("""
                        {"nombre": "Ana", "email": "ana@empresa.com", "tipo": "EMPRESAS", "organizacion": "ACME",
                         "asunto": "Voluntariado corporativo", "mensaje": "Queremos sumarnos"}
                        """))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/newsletter").contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"ana@empresa.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void seguridadDelPanel() throws Exception {
        mvc.perform(get("/api/admin/dashboard")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"admin@hablemosconlasmanos.org\", \"password\": \"incorrecta\"}"))
                .andExpect(status().isUnauthorized());

        String admin = login("admin@hablemosconlasmanos.org", "Admin12345");
        mvc.perform(get("/api/admin/dashboard").header("Authorization", "Bearer " + admin))
                .andExpect(status().isOk());
        mvc.perform(post("/api/admin/usuarios").header("Authorization", "Bearer " + admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Editor\", \"email\": \"editor@org.com\", \"password\": \"Editor12345\", \"rol\": \"EDITOR\"}"))
                .andExpect(status().isCreated());

        String editor = login("editor@org.com", "Editor12345");
        mvc.perform(post("/api/admin/noticias").header("Authorization", "Bearer " + editor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\": \"Noticia del editor\", \"resumen\": \"Resumen\", \"contenido\": \"Texto\", \"publicada\": true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value("noticia-del-editor"));
        mvc.perform(get("/api/admin/postulaciones").header("Authorization", "Bearer " + editor))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/admin/usuarios").header("Authorization", "Bearer " + editor))
                .andExpect(status().isForbidden());
    }

    @Test
    void flujoDeDonacion() throws Exception {
        String respuesta = mvc.perform(post("/api/donaciones").contentType(MediaType.APPLICATION_JSON).content("""
                        {"tipo": "MENSUAL", "monto": 50, "moneda": "pen", "nombre": "Carlos", "email": "carlos@correo.com"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andReturn().getResponse().getContentAsString();
        String referencia = JsonPath.read(respuesta, "$.referencia");
        String notificacion = "{\"referencia\": \"" + referencia + "\", \"estado\": \"COMPLETADA\", \"idTransaccion\": \"TX-1\"}";

        mvc.perform(post("/api/donaciones/webhook").header("X-Webhook-Secret", "malo")
                        .contentType(MediaType.APPLICATION_JSON).content(notificacion))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/donaciones/webhook").header("X-Webhook-Secret", "secreto-webhook-desarrollo")
                        .contentType(MediaType.APPLICATION_JSON).content(notificacion))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/donaciones/" + referencia))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("COMPLETADA"))
                .andExpect(jsonPath("$.moneda").value("PEN"));
    }

    @Test
    void subidaDeCv() throws Exception {
        mvc.perform(multipart("/api/archivos/cv")
                        .file(new MockMultipartFile("archivo", "cv.pdf", "application/pdf", "<script>".getBytes())))
                .andExpect(status().isBadRequest());
        String respuesta = mvc.perform(multipart("/api/archivos/cv")
                        .file(new MockMultipartFile("archivo", "cv.pdf", "application/pdf", "%PDF-1.4 contenido".getBytes())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String url = JsonPath.read(respuesta, "$.url");

        mvc.perform(get("/api/admin/archivos/" + url)).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/admin/archivos/" + url)
                        .header("Authorization", "Bearer " + login("admin@hablemosconlasmanos.org", "Admin12345")))
                .andExpect(status().isOk());
    }

    private String login(String email, String password) throws Exception {
        String respuesta = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(respuesta, "$.token");
    }
}
