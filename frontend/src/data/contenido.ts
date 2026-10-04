// Textos institucionales. Los datos sobre la comunidad sorda vienen del
// material "Senas que rompen barreras - Sesion 2"; el resto es relleno editable.
import { img } from './imagenes'

export const proposito = {
  mision:
    'Romper las barreras de comunicación que vive la comunidad sorda, acercando la Lengua de Señas Peruana (LSP) a escuelas, familias, empresas y servicios públicos a través del voluntariado.',
  vision:
    'Un país donde cada persona sorda pueda estudiar, trabajar, atenderse en salud y participar en su comunidad en su propia lengua.',
  valores: ['Respeto', 'Inclusión', 'Comunidad', 'Alegría', 'Compromiso'],
}

/** Datos del problema (diapositivas 3 a 10 del material). */
export const datosProblema = [
  {
    cifra: '0,1%',
    texto: 'de la población peruana oyente sabe lengua de señas peruana, según la Federación Mundial de Sordos.',
    color: 'rosa',
  },
  {
    cifra: '532 mil',
    texto: 'personas con discapacidad auditiva viven en el Perú.',
    color: 'amarillo',
  },
  {
    cifra: '76%',
    texto: 'de instituciones educativas públicas no estaban en condiciones de atender a estudiantes sordos hasta 2019 (Defensoría del Pueblo).',
    color: 'rojo',
  },
  {
    cifra: '83%',
    texto: 'de instituciones educativas privadas tampoco podían brindarles servicios educativos (Defensoría del Pueblo, 2019).',
    color: 'violeta',
  },
]

export const barreras = [
  { titulo: 'Educación', texto: 'Las metodologías de enseñanza no se adaptan a las necesidades de estudiantes sordos.', imagen: img.fotoAula },
  { titulo: 'Salud', texto: 'Pocos centros de salud cuentan con intérpretes o personal que sepa LSP.', imagen: img.fotoSalud },
  { titulo: 'Empleo', texto: 'Los procesos de selección y los espacios de trabajo rara vez son accesibles.', imagen: img.fotoTrabajo },
  { titulo: 'Justicia y Estado', texto: 'Trámites, comisarías y audiencias sin intérpretes dejan a muchas personas sin voz.', imagen: img.fotoCongreso },
]

/** Linea de tiempo de la LSP en el Peru. */
export const historiaLsp = [
  { anio: '2010', titulo: 'Se promulga la Ley N° 29535', texto: 'Otorga el reconocimiento oficial a la lengua de señas peruana.' },
  { anio: '2015', titulo: 'Última guía de LSP', texto: 'Se distribuye la última "Guía para el aprendizaje de la Lengua de Señas Peruana".' },
  { anio: '2023', titulo: 'Perfil del intérprete', texto: 'Se publica el perfil del intérprete de LSP: ¡tardaron 13 años en crearlo!' },
  { anio: 'Hoy', titulo: 'Seguimos trabajando', texto: 'La LSP aún no está en la lista de lenguas originarias del Ministerio de Cultura.' },
]

/** "¿Como interactuamos con una persona sorda?" (diapositivas 13 a 21). */
export const consejos = [
  {
    titulo: 'Llama su atención con respeto',
    texto: 'Puedes tocar levemente su hombro. Si está a distancia, mueve el brazo, genera vibraciones suaves sobre la mesa o prende y apaga las luces.',
    imagen: img.consejoHombro,
  },
  {
    titulo: 'Mantén el contacto visual',
    texto: 'Trata de no perder el contacto visual y, si es una conversación grupal, respeta los turnos.',
    imagen: img.consejoTurnos,
  },
  {
    titulo: 'Deja ver tu boca',
    texto: 'No es así siempre, pero una parte de la comunidad sorda sabe leer los labios.',
    imagen: img.consejoSenas,
  },
  {
    titulo: 'No grites: comunica',
    texto: 'Si no sabes LSP no debes gritar. Usa señas de sentido común, muestra imágenes o escribe.',
    imagen: img.consejoEscribir,
  },
  {
    titulo: 'Usa luces y señales visuales',
    texto: 'Prender o apagar la luz es una forma clara de pedir atención en un espacio grande.',
    imagen: img.consejoLuz,
  },
  {
    titulo: 'Aprende cómo lo diría una persona sorda',
    texto: 'Aprender LSP es la mejor manera de comunicarte. ¡Súmate a nuestros talleres!',
    imagen: img.consejoLibro,
  },
]

export const terminos = [
  { incorrecto: 'Sordomudo / Sordito', correcto: 'Persona sorda / Persona con discapacidad auditiva' },
  { incorrecto: 'Lenguaje de señas', correcto: 'Lengua de señas' },
]

/** Personajes del material. Los nombres son de relleno. */
export const personajes = [
  { nombre: 'Leo', descripcion: 'Curioso y conversador, siempre listo para aprender una seña nueva.', imagen: img.ninoGuino },
  { nombre: 'Sofi', descripcion: 'Usa audífono y es experta en LSP. Le encanta enseñar a sus amigos.', imagen: img.ninaFeliz },
  { nombre: 'Michi', descripcion: 'El gato del grupo. No habla ni seña… pero lo entiende todo. ¿Miau?', imagen: img.gatoSentado },
]
