import { useState } from 'react'
import { api } from '../api/cliente'
import type { MensajeContacto, Pagina } from '../api/tipos'
import { Cargando, ErrorCarga } from '../components/Estados'
import { fecha, nombreTipoMensaje } from '../utils/formato'
import { useAdmin } from './useAdmin'

export default function Mensajes() {
  const [soloNoLeidos, setSoloNoLeidos] = useState(false)
  const { datos, error, cargando, recargar } = useAdmin<Pagina<MensajeContacto>>(
    `/api/admin/mensajes?tamanio=50${soloNoLeidos ? '&leido=false' : ''}`,
  )
  const [abierto, setAbierto] = useState<number | null>(null)

  async function marcar(m: MensajeContacto, cambios: Partial<Pick<MensajeContacto, 'leido' | 'respondido'>>) {
    await api(`/api/admin/mensajes/${m.id}`, { method: 'PATCH', auth: true, body: cambios })
    recargar()
  }

  async function abrir(m: MensajeContacto) {
    setAbierto(abierto === m.id ? null : m.id)
    if (!m.leido) marcar(m, { leido: true })
  }

  return (
    <>
      <div className="admin__encabezado">
        <h1>Mensajes de contacto</h1>
        <label className="check">
          <input type="checkbox" checked={soloNoLeidos} onChange={(e) => setSoloNoLeidos(e.target.checked)} />
          <span>Solo no leídos</span>
        </label>
      </div>
      {cargando && !datos ? (
        <Cargando />
      ) : error ? (
        <ErrorCarga mensaje={error} />
      ) : (
        <ul className="mensajes">
          {datos?.contenido.map((m) => (
            <li key={m.id} className={`caja mensaje ${m.leido ? '' : 'mensaje--nuevo'}`}>
              <button className="mensaje__cabecera" onClick={() => abrir(m)} aria-expanded={abierto === m.id}>
                <span className="etiqueta">{nombreTipoMensaje[m.tipo]}</span>
                <strong>{m.asunto}</strong>
                <span>
                  {m.nombre} · {fecha(m.creadoEn, { day: '2-digit', month: 'short' })}
                </span>
              </button>
              {abierto === m.id && (
                <div className="mensaje__cuerpo">
                  <p>
                    <a href={`mailto:${m.email}?subject=Re: ${encodeURIComponent(m.asunto)}`}>{m.email}</a>
                    {m.telefono && ` · ${m.telefono}`}
                    {m.organizacion && ` · ${m.organizacion}`}
                  </p>
                  <p className="contenido-rico">{m.mensaje}</p>
                  <label className="check">
                    <input type="checkbox" checked={m.respondido} onChange={(e) => marcar(m, { respondido: e.target.checked })} />
                    <span>Respondido</span>
                  </label>
                </div>
              )}
            </li>
          ))}
          {!datos?.contenido.length && <li className="vacio">No hay mensajes.</li>}
        </ul>
      )}
    </>
  )
}
