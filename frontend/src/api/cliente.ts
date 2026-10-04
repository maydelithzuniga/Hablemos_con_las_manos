import type { ErrorApi } from './tipos'

export const API_URL = (import.meta.env.VITE_API_URL ?? 'http://localhost:8080').replace(/\/+$/, '')

const CLAVE_TOKEN = 'hcm_token'

export function obtenerToken(): string | null {
  try {
    return localStorage.getItem(CLAVE_TOKEN)
  } catch {
    return null
  }
}

export function guardarToken(token: string | null) {
  try {
    if (token) localStorage.setItem(CLAVE_TOKEN, token)
    else localStorage.removeItem(CLAVE_TOKEN)
  } catch {
    /* almacenamiento no disponible */
  }
}

/** Error lanzado cuando la API responde con un estado distinto de 2xx. */
export class ApiError extends Error {
  estado: number
  detalles: string[]

  constructor(error: ErrorApi) {
    super(error.mensaje)
    this.estado = error.estado
    this.detalles = error.detalles ?? []
  }
}

type Opciones = Omit<RequestInit, 'body'> & { body?: unknown; auth?: boolean }

export async function api<T>(ruta: string, { body, auth, headers, ...resto }: Opciones = {}): Promise<T> {
  const esFormData = body instanceof FormData
  const cabeceras = new Headers(headers)
  if (body !== undefined && !esFormData) cabeceras.set('Content-Type', 'application/json')
  const token = obtenerToken()
  if (auth && token) cabeceras.set('Authorization', `Bearer ${token}`)

  let respuesta: Response
  try {
    respuesta = await fetch(`${API_URL}${ruta}`, {
      ...resto,
      headers: cabeceras,
      body: body === undefined ? undefined : esFormData ? (body as FormData) : JSON.stringify(body),
    })
  } catch {
    throw new ApiError({ estado: 0, mensaje: 'No pudimos conectar con el servidor. Intentalo nuevamente.' })
  }

  if (respuesta.status === 401 && auth) {
    guardarToken(null)
  }
  if (!respuesta.ok) {
    let error: ErrorApi = { estado: respuesta.status, mensaje: 'Ocurrio un error inesperado' }
    try {
      error = { ...error, ...(await respuesta.json()) }
    } catch {
      /* respuesta sin cuerpo JSON */
    }
    throw new ApiError(error)
  }
  if (respuesta.status === 204) return undefined as T
  const tipo = respuesta.headers.get('Content-Type') ?? ''
  return (tipo.includes('application/json') ? respuesta.json() : respuesta.blob()) as Promise<T>
}

/** Convierte una URL de imagen de la API (relativa o absoluta) en una usable por el navegador. */
export function urlArchivo(url?: string | null): string | undefined {
  if (!url) return undefined
  if (/^(https?:|data:|blob:|\/)/.test(url)) return url
  return `${API_URL}/${url}`
}
