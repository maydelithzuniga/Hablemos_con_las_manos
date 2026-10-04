package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.entity.Donacion;
import com.hablemosconlasmanos.api.entity.MensajeContacto;
import com.hablemosconlasmanos.api.entity.Postulacion;
import com.hablemosconlasmanos.api.entity.Suscriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Envio de correos de confirmacion. Se ejecuta de forma asincrona para no
 * demorar la respuesta al usuario, y si el correo esta deshabilitado
 * (app.correo.habilitado=false) solo se registra en el log.
 */
@Slf4j
@Service
public class NotificacionService {

    private final ObjectProvider<JavaMailSender> mailSender;
    private final boolean habilitado;
    private final String remitente;
    private final String correoEquipo;

    public NotificacionService(ObjectProvider<JavaMailSender> mailSender,
                               @Value("${app.correo.habilitado}") boolean habilitado,
                               @Value("${app.correo.remitente}") String remitente,
                               @Value("${app.correo.notificaciones}") String correoEquipo) {
        this.mailSender = mailSender;
        this.habilitado = habilitado;
        this.remitente = remitente;
        this.correoEquipo = correoEquipo;
    }

    @Async
    public void postulacionRecibida(Postulacion p) {
        enviar(p.getEmail(), "Recibimos tu postulacion - Hablemos con las Manos",
                "Hola " + p.getNombres() + ",\n\n"
                        + "Gracias por querer ser parte de nuestro voluntariado. Recibimos tu postulacion "
                        + "y nuestro equipo la revisara pronto.\n\n"
                        + "Tu codigo de seguimiento es: " + p.getCodigo() + "\n"
                        + "Con el puedes consultar el estado de tu postulacion en nuestra web.\n\n"
                        + "Un abrazo,\nEquipo Hablemos con las Manos");
        enviar(correoEquipo, "Nueva postulacion: " + p.getNombres() + " " + p.getApellidos(),
                "Se recibio una nueva postulacion (" + p.getCodigo() + ") de " + p.getEmail() + ".");
    }

    @Async
    public void estadoPostulacionCambiado(Postulacion p) {
        String detalle = switch (p.getEstado()) {
            case EN_REVISION -> "Tu postulacion esta siendo revisada por nuestro equipo.";
            case ENTREVISTA -> "Queremos conocerte! Pronto te contactaremos para coordinar una entrevista.";
            case ACEPTADA -> "Felicitaciones! Fuiste seleccionado(a) como voluntario(a). Te escribiremos con los proximos pasos.";
            case RECHAZADA -> "En esta oportunidad no continuaras en el proceso. Te invitamos a postular a futuras convocatorias.";
            default -> "El estado de tu postulacion cambio a: " + p.getEstado();
        };
        enviar(p.getEmail(), "Actualizacion de tu postulacion " + p.getCodigo(),
                "Hola " + p.getNombres() + ",\n\n" + detalle + "\n\nEquipo Hablemos con las Manos");
    }

    @Async
    public void donacionConfirmada(Donacion d) {
        enviar(d.getEmail(), "Gracias por tu donacion!",
                "Hola " + d.getNombre() + ",\n\n"
                        + "Confirmamos tu " + (d.getTipo().name().equals("MENSUAL") ? "aporte mensual" : "donacion")
                        + " de " + d.getMoneda() + " " + d.getMonto() + " (referencia " + d.getReferencia() + ").\n"
                        + "Tu apoyo hace posible nuestros proyectos. Gracias!\n\nEquipo Hablemos con las Manos");
    }

    @Async
    public void mensajeContactoRecibido(MensajeContacto m) {
        enviar(m.getEmail(), "Recibimos tu mensaje",
                "Hola " + m.getNombre() + ",\n\nGracias por escribirnos. Te responderemos a la brevedad.\n\n"
                        + "Equipo Hablemos con las Manos");
        enviar(correoEquipo, "[Contacto - " + m.getTipo() + "] " + m.getAsunto(),
                "De: " + m.getNombre() + " <" + m.getEmail() + ">\n"
                        + (m.getOrganizacion() != null ? "Organizacion: " + m.getOrganizacion() + "\n" : "")
                        + "\n" + m.getMensaje());
    }

    @Async
    public void suscripcionConfirmada(Suscriptor s, String urlBaja) {
        enviar(s.getEmail(), "Te suscribiste a nuestro boletin",
                "Gracias por suscribirte! Recibiras noticias de nuestros proyectos y convocatorias.\n\n"
                        + "Si deseas darte de baja: " + urlBaja);
    }

    private void enviar(String para, String asunto, String cuerpo) {
        if (!habilitado) {
            log.info("[correo deshabilitado] Para: {} | Asunto: {}", para, asunto);
            return;
        }
        JavaMailSender sender = mailSender.getIfAvailable();
        if (sender == null) {
            log.warn("No hay JavaMailSender configurado; no se envio el correo a {}", para);
            return;
        }
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(para);
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);
            sender.send(mensaje);
        } catch (Exception e) {
            log.error("No se pudo enviar el correo a {}: {}", para, e.getMessage());
        }
    }
}
