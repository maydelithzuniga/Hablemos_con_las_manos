// Contenido de relleno: se muestra si la API no esta disponible.
// Coincide con los datos de ejemplo que carga el backend (config/DataLoader.java).
import type {
  Aliado,
  CifraImpacto,
  Convocatoria,
  DocumentoTransparencia,
  Impacto,
  Inicio,
  MiembroEquipo,
  Noticia,
  NoticiaResumen,
  Pagina,
  Pais,
  Programa,
  Proyecto,
  Testimonio,
} from '../api/tipos'

const hoy = new Date()
const dias = (n: number) => new Date(hoy.getTime() + n * 86400000).toISOString().slice(0, 10)

export const paises: Pais[] = [
  { id: 1, nombre: 'Perú', codigo: 'PE', descripcion: 'Trabajamos con comunidades sordas de Lima, Cusco y Arequipa.', activo: true },
  { id: 2, nombre: 'Chile', codigo: 'CL', descripcion: 'Talleres de lengua de señas en escuelas públicas de Santiago y Valparaíso.', activo: true },
  { id: 3, nombre: 'Bolivia', codigo: 'BO', descripcion: 'Acompañamiento a familias de niños y niñas sordos en El Alto.', activo: true },
  { id: 4, nombre: 'Colombia', codigo: 'CO', descripcion: 'Formación de intérpretes comunitarios en Medellín y Bogotá.', activo: true },
]

export const programas: Programa[] = [
  {
    id: 1,
    titulo: 'Voluntariado Profesional',
    slug: 'voluntariado-profesional',
    resumen: 'Pon tu profesión al servicio de comunidades sordas durante 6 a 12 meses.',
    descripcion:
      'Profesionales de educación, salud, psicología y ciencias sociales se suman a proyectos de inclusión junto a organizaciones locales, fortaleciendo capacidades y generando materiales accesibles en lengua de señas.',
    tipo: 'PROFESIONAL',
    modalidad: 'PRESENCIAL',
    duracion: '6 a 12 meses',
    requisitos: 'Título profesional, mínimo 1 año de experiencia, disponibilidad a tiempo completo.',
    beneficios: 'Formación previa, seguro, alojamiento y estipendio mensual.',
    destacado: true,
    activo: true,
  },
  {
    id: 2,
    titulo: 'Voluntariado Juvenil',
    slug: 'voluntariado-juvenil',
    resumen: 'Jóvenes de 18 a 29 años que aprenden lengua de señas y apoyan actividades comunitarias.',
    descripcion:
      'Un programa de fines de semana donde jóvenes participan en talleres, actividades recreativas y campañas de sensibilización junto a la comunidad sorda.',
    tipo: 'JUVENIL',
    modalidad: 'HIBRIDA',
    duracion: '3 meses',
    requisitos: 'Tener entre 18 y 29 años. No se requiere conocimiento previo de lengua de señas.',
    beneficios: 'Curso básico de lengua de señas certificado.',
    destacado: true,
    activo: true,
  },
  {
    id: 3,
    titulo: 'Voluntariado Corporativo',
    slug: 'voluntariado-corporativo',
    resumen: 'Experiencias de voluntariado para equipos de empresas comprometidas con la inclusión.',
    descripcion:
      'Diseñamos jornadas a medida para que los colaboradores de tu empresa aprendan lengua de señas y participen en proyectos de accesibilidad.',
    tipo: 'CORPORATIVO',
    modalidad: 'PRESENCIAL',
    duracion: '1 a 5 días',
    destacado: false,
    activo: true,
  },
]

const proyecto = (
  id: number,
  titulo: string,
  slug: string,
  pais: Pais,
  programa: Programa,
  area: Proyecto['area'],
  resumen: string,
  beneficiarios: number,
  destacado: boolean,
): Proyecto => ({
  id,
  titulo,
  slug,
  resumen,
  descripcion: `${resumen} El proyecto se ejecuta junto a organizaciones locales de personas sordas.`,
  pais,
  programaId: programa.id,
  programaTitulo: programa.titulo,
  area,
  estado: 'EN_CURSO',
  fechaInicio: dias(-180),
  beneficiarios,
  destacado,
})

export const proyectos: Proyecto[] = [
  proyecto(1, 'Aulas que se escuchan con las manos', 'aulas-que-se-escuchan-con-las-manos', paises[0], programas[0], 'EDUCACION',
    'Capacitación a docentes de escuelas inclusivas en lengua de señas peruana.', 1200, true),
  proyecto(2, 'Familias que señan', 'familias-que-senan', paises[2], programas[1], 'INFANCIA',
    'Talleres para que madres y padres aprendan a comunicarse con sus hijos sordos.', 350, true),
  proyecto(3, 'Salud accesible', 'salud-accesible', paises[1], programas[0], 'SALUD',
    'Intérpretes en centros de salud primaria y guías accesibles para pacientes sordos.', 800, false),
  proyecto(4, 'Intérpretes comunitarios', 'interpretes-comunitarios', paises[3], programas[0], 'INCLUSION',
    'Formación de intérpretes que acompañan trámites, juicios y consultas médicas.', 500, true),
]

export const convocatorias: Convocatoria[] = [
  {
    id: 1,
    titulo: 'Voluntariado Profesional 2027 - Perú',
    descripcion: 'Buscamos profesionales para sumarse a nuestros proyectos educativos en Lima y Cusco.',
    programaId: 1,
    programaTitulo: 'Voluntariado Profesional',
    pais: paises[0],
    fechaApertura: dias(-10),
    fechaCierre: dias(45),
    fechaInicioVoluntariado: dias(90),
    cupos: 15,
    perfiles: 'Docentes, psicólogos, fonoaudiólogos, intérpretes de lengua de señas',
    publicada: true,
    abierta: true,
  },
  {
    id: 2,
    titulo: 'Voluntariado Juvenil - Verano',
    descripcion: 'Tres meses de talleres y actividades comunitarias los fines de semana.',
    programaId: 2,
    programaTitulo: 'Voluntariado Juvenil',
    pais: null,
    fechaApertura: dias(-5),
    fechaCierre: dias(30),
    cupos: 40,
    publicada: true,
    abierta: true,
  },
]

export const noticias: Noticia[] = [
  {
    id: 1,
    titulo: 'Celebramos el Día Internacional de las Lenguas de Señas',
    slug: 'celebramos-el-dia-internacional-de-las-lenguas-de-senas',
    resumen: 'Más de 300 personas participaron en actividades en cuatro países.',
    contenido:
      'Más de 300 personas participaron en actividades en cuatro países.\n\nEste es un contenido de ejemplo. Edítalo desde el panel de administración.',
    categoria: 'Comunidad',
    autor: 'Equipo de Comunicaciones',
    publicada: true,
    destacada: false,
    fechaPublicacion: new Date(hoy.getTime() - 2 * 86400000).toISOString(),
    pais: paises[0],
  },
  {
    id: 2,
    titulo: 'Nueva alianza para formar intérpretes en Colombia',
    slug: 'nueva-alianza-para-formar-interpretes-en-colombia',
    resumen: 'Junto a universidades locales lanzamos un diplomado gratuito.',
    contenido:
      'Junto a universidades locales lanzamos un diplomado gratuito.\n\nEste es un contenido de ejemplo. Edítalo desde el panel de administración.',
    categoria: 'Alianzas',
    autor: 'Equipo de Comunicaciones',
    publicada: true,
    destacada: false,
    fechaPublicacion: new Date(hoy.getTime() - 9 * 86400000).toISOString(),
    pais: paises[3],
  },
  {
    id: 3,
    titulo: 'Abrimos la convocatoria de Voluntariado Profesional 2027',
    slug: 'abrimos-la-convocatoria-de-voluntariado-profesional-2027',
    resumen: 'Conoce los perfiles que buscamos y cómo postular.',
    contenido:
      'Conoce los perfiles que buscamos y cómo postular.\n\nEste es un contenido de ejemplo. Edítalo desde el panel de administración.',
    categoria: 'Convocatorias',
    autor: 'Equipo de Comunicaciones',
    publicada: true,
    destacada: false,
    fechaPublicacion: new Date(hoy.getTime() - 16 * 86400000).toISOString(),
  },
]

export const noticiasResumen: NoticiaResumen[] = noticias.map(({ contenido: _c, pais: _p, ...n }) => n)

export const paginaNoticias: Pagina<NoticiaResumen> = {
  contenido: noticiasResumen,
  pagina: 0,
  tamanio: 9,
  totalElementos: noticiasResumen.length,
  totalPaginas: 1,
  ultima: true,
}

export const paginaProyectos: Pagina<Proyecto> = {
  contenido: proyectos,
  pagina: 0,
  tamanio: 9,
  totalElementos: proyectos.length,
  totalPaginas: 1,
  ultima: true,
}

export const testimonios: Testimonio[] = [
  {
    id: 1,
    nombre: 'Andrea',
    rol: 'Voluntaria profesional - Perú',
    texto: 'Fue un año que me cambió la vida: aprendí más de lo que enseñé y hoy la lengua de señas es parte de mí.',
    programaId: 1,
    programaTitulo: 'Voluntariado Profesional',
    aprobado: true,
  },
  {
    id: 2,
    nombre: 'Mateo',
    rol: 'Voluntario juvenil - Bolivia',
    texto: 'Cada fin de semana con las familias fue una lección de paciencia, alegría y comunidad.',
    programaId: 2,
    programaTitulo: 'Voluntariado Juvenil',
    aprobado: true,
  },
  {
    id: 3,
    nombre: 'Rosa',
    rol: 'Mamá participante - Lima',
    texto: 'Ahora puedo contarle cuentos a mi hija en su propia lengua. Gracias por enseñarnos con tanto cariño.',
    aprobado: true,
  },
]

export const aliados: Aliado[] = [
  { id: 1, nombre: 'Empresa Aliada', tipo: 'EMPRESA', orden: 1, activo: true },
  { id: 2, nombre: 'Universidad Ejemplo', tipo: 'UNIVERSIDAD', orden: 2, activo: true },
  { id: 3, nombre: 'Fundación Ejemplo', tipo: 'FUNDACION', orden: 3, activo: true },
  { id: 4, nombre: 'Municipalidad Ejemplo', tipo: 'GOBIERNO', orden: 4, activo: true },
  { id: 5, nombre: 'Asociación de Sordos', tipo: 'FUNDACION', orden: 5, activo: true },
  { id: 6, nombre: 'Organismo Internacional', tipo: 'ORGANISMO_INTERNACIONAL', orden: 6, activo: true },
]

export const equipo: MiembroEquipo[] = [
  { id: 1, nombre: 'Nombre Apellido', cargo: 'Directora Ejecutiva', area: 'EQUIPO', orden: 1, activo: true },
  { id: 2, nombre: 'Nombre Apellido', cargo: 'Coordinador de Voluntariado', area: 'EQUIPO', orden: 2, activo: true },
  { id: 3, nombre: 'Nombre Apellido', cargo: 'Intérprete de LSP', area: 'EQUIPO', orden: 3, activo: true },
  { id: 4, nombre: 'Nombre Apellido', cargo: 'Comunicaciones', area: 'EQUIPO', orden: 4, activo: true },
  { id: 5, nombre: 'Nombre Apellido', cargo: 'Presidenta del Directorio', area: 'DIRECTORIO', orden: 1, activo: true },
  { id: 6, nombre: 'Nombre Apellido', cargo: 'Director', area: 'DIRECTORIO', orden: 2, activo: true },
]

export const transparencia: DocumentoTransparencia[] = [
  { id: 1, titulo: 'Memoria anual 2025', tipo: 'MEMORIA_ANUAL', anio: 2025, archivoUrl: '#', descripcion: 'Resumen de actividades y logros del año.' },
  { id: 2, titulo: 'Estados financieros 2025', tipo: 'ESTADO_FINANCIERO', anio: 2025, archivoUrl: '#' },
  { id: 3, titulo: 'Memoria anual 2024', tipo: 'MEMORIA_ANUAL', anio: 2024, archivoUrl: '#' },
  { id: 4, titulo: 'Política de protección de datos', tipo: 'POLITICA', anio: 2024, archivoUrl: '#' },
]

export const cifras: CifraImpacto[] = [
  { id: 1, etiqueta: 'Voluntarios y voluntarias', valor: 1500, prefijo: '+', icono: 'users', orden: 1 },
  { id: 2, etiqueta: 'Personas beneficiadas', valor: 20000, prefijo: '+', icono: 'heart', orden: 2 },
  { id: 3, etiqueta: 'Países', valor: 4, icono: 'globe', orden: 3 },
  { id: 4, etiqueta: 'Años de trayectoria', valor: 10, icono: 'calendar', orden: 4 },
]

export const impacto: Impacto = {
  cifras,
  paisesActivos: 4,
  proyectosEnCurso: 4,
  proyectosTotales: 4,
  beneficiarios: 2850,
  voluntariosAceptados: 0,
  sociosMensuales: 0,
}

export const inicio: Inicio = {
  programasDestacados: programas.filter((p) => p.destacado),
  proyectosDestacados: proyectos.filter((p) => p.destacado),
  convocatoriasAbiertas: convocatorias,
  ultimasNoticias: noticiasResumen,
  testimonios,
  aliados,
  impacto,
}
