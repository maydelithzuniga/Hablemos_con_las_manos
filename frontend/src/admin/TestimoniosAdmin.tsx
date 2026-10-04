import { useState } from 'react'
import { api } from '../api/cliente'
import type { Testimonio } from '../api/tipos'
import { Cargando, ErrorCarga } from '../components/Estados'
import { useAdmin } from './useAdmin'

export default function TestimoniosAdmin() {
  const [filtro, setFiltro] = useState<'false' | 'true' | ''>('false')
  const { datos, error, cargando, recargar } = useAdmin<Testimonio[]>(`/api/admin/testimonios${filtro ? `?aprobado=${filtro}` : ''}`)

  async function aprobar(t: Testimonio, aprobado: boolean) {
    await api(`/api/admin/testimonios/${t.id}/aprobacion?aprobado=${aprobado}`, { method: 'PATCH', auth: true })
    recargar()
  }

  async function eliminar(t: Testimonio) {
    if (!confirm('¿Eliminar este testimonio?')) return
    await api(`/api/admin/testimonios/${t.id}`, { method: 'DELETE', auth: true })
    recargar()
  }

  return (
    <>
      <div className="admin__encabezado">
        <h1>Testimonios</h1>
      </div>
      <div className="filtros" role="group" aria-label="Filtro">
        {([['false', 'Por aprobar'], ['true', 'Publicados'], ['', 'Todos']] as const).map(([v, t]) => (
          <button key={v} className="chip" aria-pressed={filtro === v} onClick={() => setFiltro(v)}>
            {t}
          </button>
        ))}
      </div>
      {cargando && !datos ? (
        <Cargando />
      ) : error ? (
        <ErrorCarga mensaje={error} />
      ) : (
        <div className="grid grid--3">
          {datos?.map((t) => (
            <article key={t.id} className="caja">
              <p>“{t.texto}”</p>
              <p>
                <strong>{t.nombre}</strong>
                {t.rol && <> · {t.rol}</>}
              </p>
              <div className="tabla__acciones">
                {t.aprobado ? (
                  <button className="boton boton--blanco boton--chico" onClick={() => aprobar(t, false)}>
                    Ocultar
                  </button>
                ) : (
                  <button className="boton boton--chico" onClick={() => aprobar(t, true)}>
                    Aprobar
                  </button>
                )}
                <button className="boton boton--blanco boton--chico" onClick={() => eliminar(t)}>
                  Eliminar
                </button>
              </div>
            </article>
          ))}
          {!datos?.length && <p className="vacio">No hay testimonios aquí.</p>}
        </div>
      )}
    </>
  )
}
