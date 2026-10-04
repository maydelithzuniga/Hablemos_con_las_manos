import { useEffect, useState } from 'react'
import { api } from '../api/cliente'

interface Estado<T> {
  datos: T | undefined
  cargando: boolean
  error: string | null
  /** true cuando se muestran los datos de relleno porque la API no respondio. */
  deRelleno: boolean
}

/**
 * Pide datos a la API. Si la API no esta disponible y se pasa un "relleno",
 * se muestra ese contenido de ejemplo para que el sitio nunca quede vacio.
 */
export function useApi<T>(ruta: string | null, relleno?: T): Estado<T> {
  const [estado, setEstado] = useState<Estado<T>>({ datos: undefined, cargando: !!ruta, error: null, deRelleno: false })

  useEffect(() => {
    if (!ruta) return
    let activo = true
    setEstado((e) => ({ ...e, cargando: true, error: null }))
    api<T>(ruta)
      .then((datos) => activo && setEstado({ datos, cargando: false, error: null, deRelleno: false }))
      .catch((e: Error & { estado?: number }) => {
        if (!activo) return
        const sinConexion = e.estado === 0
        if (relleno !== undefined && sinConexion) {
          setEstado({ datos: relleno, cargando: false, error: null, deRelleno: true })
        } else {
          setEstado({ datos: undefined, cargando: false, error: e.message, deRelleno: false })
        }
      })
    return () => {
      activo = false
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [ruta])

  return estado
}
