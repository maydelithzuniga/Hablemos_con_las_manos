import { useCallback, useEffect, useState } from 'react'
import { api } from '../api/cliente'

/** Igual que useApi pero autenticado y con "recargar" para el panel. */
export function useAdmin<T>(ruta: string) {
  const [datos, setDatos] = useState<T>()
  const [error, setError] = useState<string | null>(null)
  const [cargando, setCargando] = useState(true)

  const recargar = useCallback(() => {
    setCargando(true)
    api<T>(ruta, { auth: true })
      .then((d) => {
        setDatos(d)
        setError(null)
      })
      .catch((e: Error) => setError(e.message))
      .finally(() => setCargando(false))
  }, [ruta])

  useEffect(recargar, [recargar])

  return { datos, error, cargando, recargar }
}

/** Descarga un archivo protegido (CSV, CV) usando el token. */
export async function descargar(ruta: string, nombre: string) {
  const blob = await api<Blob>(ruta, { auth: true })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = nombre
  a.click()
  URL.revokeObjectURL(url)
}
