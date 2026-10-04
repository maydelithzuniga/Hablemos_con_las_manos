package com.hablemosconlasmanos.api.config;

import com.hablemosconlasmanos.api.entity.*;
import com.hablemosconlasmanos.api.repository.*;
import com.hablemosconlasmanos.api.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Al arrancar:
 * <ol>
 *   <li>Crea el usuario administrador inicial si no existe ningun usuario.</li>
 *   <li>Si app.datos-ejemplo=true y la base esta vacia, carga contenido de ejemplo
 *       para que el frontend tenga algo que mostrar desde el primer dia.</li>
 * </ol>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PaisRepository paisRepository;
    private final ProgramaRepository programaRepository;
    private final ProyectoRepository proyectoRepository;
    private final ConvocatoriaRepository convocatoriaRepository;
    private final NoticiaRepository noticiaRepository;
    private final AliadoRepository aliadoRepository;
    private final MiembroEquipoRepository miembroRepository;
    private final TestimonioRepository testimonioRepository;
    private final CifraImpactoRepository cifraRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.nombre}")
    private String adminNombre;

    @Value("${app.datos-ejemplo}")
    private boolean datosEjemplo;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        crearAdministrador();
        if (datosEjemplo && programaRepository.count() == 0) {
            cargarDatosEjemplo();
        }
    }

    private void crearAdministrador() {
        if (usuarioRepository.count() > 0) {
            return;
        }
        usuarioRepository.save(Usuario.builder()
                .nombre(adminNombre)
                .email(adminEmail.toLowerCase(Locale.ROOT))
                .password(passwordEncoder.encode(adminPassword))
                .rol(Rol.ADMIN)
                .build());
        log.warn("Usuario administrador inicial creado: {} (cambia la contrasena!)", adminEmail);
    }

    private void cargarDatosEjemplo() {
        log.info("Cargando datos de ejemplo...");
        Pais peru = pais("Peru", "PE", "Trabajamos con comunidades sordas de Lima, Cusco y Arequipa.");
        Pais chile = pais("Chile", "CL", "Talleres de lengua de senas en escuelas publicas de Santiago y Valparaiso.");
        Pais bolivia = pais("Bolivia", "BO", "Acompanamiento a familias de ninos y ninas sordos en El Alto.");
        Pais colombia = pais("Colombia", "CO", "Formacion de interpretes comunitarios en Medellin y Bogota.");

        Programa profesional = programaRepository.save(Programa.builder()
                .titulo("Voluntariado Profesional")
                .slug("voluntariado-profesional")
                .resumen("Pon tu profesion al servicio de comunidades sordas durante 6 a 12 meses.")
                .descripcion("Profesionales de educacion, salud, psicologia y ciencias sociales se suman a proyectos "
                        + "de inclusion junto a organizaciones locales, fortaleciendo capacidades y generando "
                        + "materiales accesibles en lengua de senas.")
                .tipo(TipoPrograma.PROFESIONAL)
                .modalidad(Modalidad.PRESENCIAL)
                .duracion("6 a 12 meses")
                .requisitos("Titulo profesional, minimo 1 anio de experiencia, disponibilidad a tiempo completo.")
                .beneficios("Formacion previa, seguro, alojamiento y estipendio mensual.")
                .destacado(true)
                .build());
        Programa juvenil = programaRepository.save(Programa.builder()
                .titulo("Voluntariado Juvenil")
                .slug("voluntariado-juvenil")
                .resumen("Jovenes de 18 a 29 anios que aprenden lengua de senas y apoyan actividades comunitarias.")
                .descripcion("Un programa de fines de semana donde jovenes participan en talleres, actividades "
                        + "recreativas y campanas de sensibilizacion junto a la comunidad sorda.")
                .tipo(TipoPrograma.JUVENIL)
                .modalidad(Modalidad.HIBRIDA)
                .duracion("3 meses")
                .requisitos("Tener entre 18 y 29 anios. No se requiere conocimiento previo de lengua de senas.")
                .beneficios("Curso basico de lengua de senas certificado.")
                .destacado(true)
                .build());
        programaRepository.save(Programa.builder()
                .titulo("Voluntariado Corporativo")
                .slug("voluntariado-corporativo")
                .resumen("Experiencias de voluntariado para equipos de empresas comprometidas con la inclusion.")
                .descripcion("Disenamos jornadas a medida para que los colaboradores de tu empresa aprendan lengua "
                        + "de senas y participen en proyectos de accesibilidad.")
                .tipo(TipoPrograma.CORPORATIVO)
                .modalidad(Modalidad.PRESENCIAL)
                .duracion("1 a 5 dias")
                .build());

        proyecto("Aulas que se escuchan con las manos", peru, profesional, AreaTematica.EDUCACION,
                "Capacitacion a docentes de escuelas inclusivas en lengua de senas peruana.", 1200, true);
        proyecto("Familias que senan", bolivia, juvenil, AreaTematica.INFANCIA,
                "Talleres para que madres y padres aprendan a comunicarse con sus hijos sordos.", 350, true);
        proyecto("Salud accesible", chile, profesional, AreaTematica.SALUD,
                "Interpretes en centros de salud primaria y guias accesibles para pacientes sordos.", 800, false);
        proyecto("Interpretes comunitarios", colombia, profesional, AreaTematica.INCLUSION,
                "Formacion de interpretes que acompanan tramites, juicios y consultas medicas.", 500, true);

        convocatoriaRepository.save(Convocatoria.builder()
                .titulo("Voluntariado Profesional 2027 - Peru")
                .descripcion("Buscamos profesionales para sumarse a nuestros proyectos educativos en Lima y Cusco.")
                .programa(profesional)
                .pais(peru)
                .fechaApertura(LocalDate.now().minusDays(10))
                .fechaCierre(LocalDate.now().plusDays(45))
                .fechaInicioVoluntariado(LocalDate.now().plusMonths(3))
                .cupos(15)
                .perfiles("Docentes, psicologos, fonoaudiologos, interpretes de lengua de senas")
                .build());
        convocatoriaRepository.save(Convocatoria.builder()
                .titulo("Voluntariado Juvenil - Verano")
                .descripcion("Tres meses de talleres y actividades comunitarias los fines de semana.")
                .programa(juvenil)
                .fechaApertura(LocalDate.now().minusDays(5))
                .fechaCierre(LocalDate.now().plusDays(30))
                .cupos(40)
                .build());

        noticia("Celebramos el Dia Internacional de las Lenguas de Senas",
                "Mas de 300 personas participaron en actividades en cuatro paises.", "Comunidad", peru);
        noticia("Nueva alianza para formar interpretes en Colombia",
                "Junto a universidades locales lanzamos un diplomado gratuito.", "Alianzas", colombia);
        noticia("Abrimos la convocatoria de Voluntariado Profesional 2027",
                "Conoce los perfiles que buscamos y como postular.", "Convocatorias", null);

        aliadoRepository.saveAll(List.of(
                Aliado.builder().nombre("Empresa Aliada Ejemplo").tipo(TipoAliado.EMPRESA).orden(1).build(),
                Aliado.builder().nombre("Universidad Ejemplo").tipo(TipoAliado.UNIVERSIDAD).orden(2).build(),
                Aliado.builder().nombre("Fundacion Ejemplo").tipo(TipoAliado.FUNDACION).orden(3).build()));

        miembroRepository.saveAll(List.of(
                MiembroEquipo.builder().nombre("Nombre Apellido").cargo("Directora Ejecutiva").area(AreaEquipo.EQUIPO).orden(1).build(),
                MiembroEquipo.builder().nombre("Nombre Apellido").cargo("Coordinador de Voluntariado").area(AreaEquipo.EQUIPO).orden(2).build(),
                MiembroEquipo.builder().nombre("Nombre Apellido").cargo("Presidenta del Directorio").area(AreaEquipo.DIRECTORIO).orden(1).build()));

        testimonioRepository.saveAll(List.of(
                Testimonio.builder().nombre("Andrea").rol("Voluntaria profesional - Peru").programa(profesional)
                        .texto("Fue un anio que me cambio la vida: aprendi mas de lo que ensene y hoy la lengua de senas es parte de mi.")
                        .aprobado(true).build(),
                Testimonio.builder().nombre("Mateo").rol("Voluntario juvenil - Bolivia").programa(juvenil)
                        .texto("Cada fin de semana con las familias fue una leccion de paciencia, alegria y comunidad.")
                        .aprobado(true).build()));

        cifraRepository.saveAll(List.of(
                CifraImpacto.builder().etiqueta("Voluntarios y voluntarias").valor(1500L).prefijo("+").icono("users").orden(1).build(),
                CifraImpacto.builder().etiqueta("Personas beneficiadas").valor(20000L).prefijo("+").icono("heart").orden(2).build(),
                CifraImpacto.builder().etiqueta("Paises").valor(4L).icono("globe").orden(3).build(),
                CifraImpacto.builder().etiqueta("Anios de trayectoria").valor(10L).icono("calendar").orden(4).build()));

    }

    private Pais pais(String nombre, String codigo, String descripcion) {
        return paisRepository.save(Pais.builder().nombre(nombre).codigo(codigo).descripcion(descripcion).build());
    }

    private void proyecto(String titulo, Pais pais, Programa programa, AreaTematica area, String resumen,
                          int beneficiarios, boolean destacado) {
        proyectoRepository.save(Proyecto.builder()
                .titulo(titulo)
                .slug(SlugUtil.generar(titulo))
                .resumen(resumen)
                .descripcion(resumen + " El proyecto se ejecuta junto a organizaciones locales de personas sordas.")
                .pais(pais)
                .programa(programa)
                .area(area)
                .fechaInicio(LocalDate.now().minusMonths(6))
                .beneficiarios(beneficiarios)
                .destacado(destacado)
                .build());
    }

    private void noticia(String titulo, String resumen, String categoria, Pais pais) {
        noticiaRepository.save(Noticia.builder()
                .titulo(titulo)
                .slug(SlugUtil.generar(titulo))
                .resumen(resumen)
                .contenido(resumen + "\n\nEste es un contenido de ejemplo. Editalo desde el panel de administracion.")
                .categoria(categoria)
                .autor("Equipo de Comunicaciones")
                .pais(pais)
                .publicada(true)
                .fechaPublicacion(LocalDateTime.now().minusDays(noticiaRepository.count() * 7))
                .build());
    }
}
