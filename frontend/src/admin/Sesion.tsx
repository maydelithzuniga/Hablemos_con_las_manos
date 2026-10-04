import { createContext, useCallback, useContext, useEffect, useState, type ReactNode } from 'react'
import { api, guardarToken, obtenerToken } from '../api/cliente'
import type { Token, Usuario } from '../api/tipos'

interface Sesion {
  usuario: Usuario | null
  cargando: boolean
  ingresar: (email: string, password: string) => Promise<void>
  salir: () => void
}

const ContextoSesion = createContext<Sesion | null>(null)

export function ProveedorSesion({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<Usuario | null>(null)
  const [cargando, setCargando] = useState(!!obtenerToken())

  useEffect(() => {
    if (!obtenerToken()) return
    api<Usuario>('/api/auth/yo', { auth: true })
      .then(setUsuario)
      .catch(() => guardarToken(null))
      .finally(() => setCargando(false))
  }, [])

  const ingresar = useCallback(async (email: string, password: string) => {
    const r = await api<Token>('/api/auth/login', { method: 'POST', body: { email, password } })
    guardarToken(r.token)
    setUsuario(r.usuario)
  }, [])

  const salir = useCallback(() => {
    guardarToken(null)
    setUsuario(null)
  }, [])

  return <ContextoSesion.Provider value={{ usuario, cargando, ingresar, salir }}>{children}</ContextoSesion.Provider>
}

// eslint-disable-next-line react-refresh/only-export-components
export function useSesion() {
  const sesion = useContext(ContextoSesion)
  if (!sesion) throw new Error('useSesion debe usarse dentro de ProveedorSesion')
  return sesion
}
