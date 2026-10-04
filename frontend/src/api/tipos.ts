// Tipos que reflejan los DTO de backend/api_voluntariado

export type TipoPrograma = 'PROFESIONAL' | 'JUVENIL' | 'CORPORATIVO' | 'LOCAL' | 'VIRTUAL'
export type Modalidad = 'PRESENCIAL' | 'VIRTUAL' | 'HIBRIDA'
export type AreaTematica =
  | 'EDUCACION'
  | 'INCLUSION'
  | 'SALUD'
  | 'INFANCIA'
  | 'MEDIO_AMBIENTE'
  | 'DESARROLLO_COMUNITARIO'
  | 'DERECHOS_HUMANOS'
export type EstadoProyecto = 'EN_CURSO' | 'FINALIZADO' | 'PROXIMAMENTE'
export type EstadoPostulacion = 'RECIBIDA' | 'EN_REVISION' | 'ENTREVISTA' | 'ACEPTADA' | 'RECHAZADA' | 'RETIRADA'
export type TipoDonacion = 'UNICA' | 'MENSUAL'
export type EstadoDonacion = 'PENDIENTE' | 'COMPLETADA' | 'FALLIDA' | 'CANCELADA'
export type TipoMensaje = 'GENERAL' | 'VOLUNTARIADO' | 'EMPRESAS' | 'PRENSA' | 'DONACIONES'
export type TipoAliado = 'EMPRESA' | 'FUNDACION' | 'GOBIERNO' | 'UNIVERSIDAD' | 'ORGANISMO_INTERNACIONAL'
export type AreaEquipo = 'EQUIPO' | 'DIRECTORIO' | 'CONSEJO_ASESOR'
export type TipoDocumento = 'MEMORIA_ANUAL' | 'ESTADO_FINANCIERO' | 'AUDITORIA' | 'POLITICA' | 'OTRO'
export type Rol = 'ADMIN' | 'EDITOR'

export interface Pagina<T> {
  contenido: T[]
  pagina: number
  tamanio: number
  totalElementos: number
  totalPaginas: number
  ultima: boolean
}

export interface Pais {
  id: number
  nombre: string
  codigo: string
  descripcion?: string
  imagenUrl?: string
  activo: boolean
}

export interface Programa {
  id: number
  titulo: string
  slug: string
  resumen: string
  descripcion: string
  tipo: TipoPrograma
  modalidad: Modalidad
  duracion?: string
  requisitos?: string
  beneficios?: string
  imagenUrl?: string
  destacado: boolean
  activo: boolean
}

export interface Proyecto {
  id: number
  titulo: string
  slug: string
  resumen: string
  descripcion: string
  pais: Pais
  programaId?: number
  programaTitulo?: string
  area: AreaTematica
  estado: EstadoProyecto
  socioLocal?: string
  fechaInicio?: string
  fechaFin?: string
  beneficiarios: number
  imagenUrl?: string
  destacado: boolean
}

export interface Convocatoria {
  id: number
  titulo: string
  descripcion?: string
  programaId: number
  programaTitulo: string
  pais?: Pais | null
  fechaApertura: string
  fechaCierre: string
  fechaInicioVoluntariado?: string
  cupos?: number
  perfiles?: string
  publicada: boolean
  abierta: boolean
}

export interface PaisDetalle {
  pais: Pais
  proyectos: Proyecto[]
  convocatoriasAbiertas: Convocatoria[]
}

export interface NoticiaResumen {
  id: number
  titulo: string
  slug: string
  resumen: string
  imagenUrl?: string
  categoria?: string
  autor?: string
  publicada: boolean
  destacada: boolean
  fechaPublicacion?: string
}

export interface Noticia extends NoticiaResumen {
  contenido: string
  pais?: Pais | null
}

export interface Testimonio {
  id: number
  nombre: string
  rol?: string
  texto: string
  fotoUrl?: string
  programaId?: number
  programaTitulo?: string
  aprobado: boolean
}

export interface Aliado {
  id: number
  nombre: string
  logoUrl?: string
  sitioWeb?: string
  tipo: TipoAliado
  descripcion?: string
  orden: number
  activo: boolean
}

export interface MiembroEquipo {
  id: number
  nombre: string
  cargo: string
  area: AreaEquipo
  fotoUrl?: string
  biografia?: string
  linkedinUrl?: string
  orden: number
  activo: boolean
}

export interface DocumentoTransparencia {
  id: number
  titulo: string
  tipo: TipoDocumento
  anio: number
  archivoUrl: string
  descripcion?: string
}

export interface CifraImpacto {
  id: number
  etiqueta: string
  valor: number
  prefijo?: string
  sufijo?: string
  icono?: string
  orden: number
}

export interface Impacto {
  cifras: CifraImpacto[]
  paisesActivos: number
  proyectosEnCurso: number
  proyectosTotales: number
  beneficiarios: number
  voluntariosAceptados: number
  sociosMensuales: number
}

export interface Inicio {
  programasDestacados: Programa[]
  proyectosDestacados: Proyecto[]
  convocatoriasAbiertas: Convocatoria[]
  ultimasNoticias: NoticiaResumen[]
  testimonios: Testimonio[]
  aliados: Aliado[]
  impacto: Impacto
}

export interface Seguimiento {
  codigo: string
  nombres: string
  postulacionA: string
  estado: EstadoPostulacion
  fechaPostulacion: string
  ultimaActualizacion: string
}

export interface DonacionCreada {
  referencia: string
  estado: EstadoDonacion
  urlPago: string
}

export interface EstadoDonacionPublico {
  referencia: string
  tipo: TipoDonacion
  monto: number
  moneda: string
  estado: EstadoDonacion
}

export interface Archivo {
  nombre: string
  url: string
  tamanio: number
  tipo: string
}

export interface Usuario {
  id: number
  nombre: string
  email: string
  rol: Rol
  activo: boolean
}

export interface Token {
  token: string
  tipo: string
  expiraEn: string
  usuario: Usuario
}

export interface Postulacion {
  id: number
  codigo: string
  postulacionA: string
  nombres: string
  apellidos: string
  email: string
  telefono?: string
  documentoIdentidad?: string
  paisResidencia?: string
  ciudad?: string
  profesion?: string
  nivelLenguaSenas?: string
  experiencia?: string
  motivacion: string
  disponibilidad?: string
  cvUrl?: string
  estado: EstadoPostulacion
  notasInternas?: string
  creadoEn: string
}

export interface Donacion {
  id: number
  referencia: string
  tipo: TipoDonacion
  monto: number
  moneda: string
  nombre: string
  email: string
  estado: EstadoDonacion
  proyectoTitulo?: string
  metodoPago?: string
  creadoEn: string
}

export interface MensajeContacto {
  id: number
  nombre: string
  email: string
  telefono?: string
  organizacion?: string
  tipo: TipoMensaje
  asunto: string
  mensaje: string
  leido: boolean
  respondido: boolean
  creadoEn: string
}

export interface Dashboard {
  postulacionesPorEstado: Record<EstadoPostulacion, number>
  mensajesSinLeer: number
  suscriptoresActivos: number
  donacionesCompletadas: number
  donacionesPendientes: number
  recaudadoUltimos30Dias: Record<string, number>
  sociosMensuales: number
  convocatoriasAbiertas: number
  testimoniosPorAprobar: number
}

export interface ErrorApi {
  estado: number
  mensaje: string
  detalles?: string[] | null
}
