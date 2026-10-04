import type { AreaTematica, EstadoPostulacion, EstadoProyecto, Modalidad, TipoAliado, TipoDocumento, TipoMensaje, TipoPrograma } from '../api/tipos'

export const nombreArea: Record<AreaTematica, string> = {
  EDUCACION: 'Educación',
  INCLUSION: 'Inclusión',
  SALUD: 'Salud',
  INFANCIA: 'Infancia',
  MEDIO_AMBIENTE: 'Medio ambiente',
  DESARROLLO_COMUNITARIO: 'Desarrollo comunitario',
  DERECHOS_HUMANOS: 'Derechos humanos',
}

export const nombreEstadoProyecto: Record<EstadoProyecto, string> = {
  EN_CURSO: 'En curso',
  FINALIZADO: 'Finalizado',
  PROXIMAMENTE: 'Próximamente',
}

export const nombreTipoPrograma: Record<TipoPrograma, string> = {
  PROFESIONAL: 'Profesional',
  JUVENIL: 'Juvenil',
  CORPORATIVO: 'Corporativo',
  LOCAL: 'Local',
  VIRTUAL: 'Virtual',
}

export const nombreModalidad: Record<Modalidad, string> = {
  PRESENCIAL: 'Presencial',
  VIRTUAL: 'Virtual',
  HIBRIDA: 'Híbrida',
}

export const nombreEstadoPostulacion: Record<EstadoPostulacion, string> = {
  RECIBIDA: 'Recibida',
  EN_REVISION: 'En revisión',
  ENTREVISTA: 'Entrevista',
  ACEPTADA: 'Aceptada',
  RECHAZADA: 'No seleccionada',
  RETIRADA: 'Retirada',
}

export const nombreTipoAliado: Record<TipoAliado, string> = {
  EMPRESA: 'Empresa',
  FUNDACION: 'Fundación',
  GOBIERNO: 'Gobierno',
  UNIVERSIDAD: 'Universidad',
  ORGANISMO_INTERNACIONAL: 'Organismo internacional',
}

export const nombreTipoDocumento: Record<TipoDocumento, string> = {
  MEMORIA_ANUAL: 'Memoria anual',
  ESTADO_FINANCIERO: 'Estado financiero',
  AUDITORIA: 'Auditoría',
  POLITICA: 'Política',
  OTRO: 'Otro',
}

export const nombreTipoMensaje: Record<TipoMensaje, string> = {
  GENERAL: 'Consulta general',
  VOLUNTARIADO: 'Voluntariado',
  EMPRESAS: 'Empresas y alianzas',
  PRENSA: 'Prensa',
  DONACIONES: 'Donaciones',
}

export function fecha(iso?: string | null, opciones: Intl.DateTimeFormatOptions = { day: 'numeric', month: 'long', year: 'numeric' }) {
  if (!iso) return ''
  const d = new Date(iso.length === 10 ? `${iso}T12:00:00` : iso)
  return d.toLocaleDateString('es-PE', opciones)
}

export function numero(n: number) {
  return n.toLocaleString('es-PE')
}

export function diasRestantes(iso: string) {
  const fin = new Date(`${iso}T23:59:59`).getTime()
  return Math.max(0, Math.ceil((fin - Date.now()) / 86400000))
}
