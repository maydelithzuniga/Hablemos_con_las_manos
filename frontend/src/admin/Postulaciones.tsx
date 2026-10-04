import { useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { api } from '../api/cliente'
import type { EstadoPostulacion, Pagina, Postulacion } from '../api/tipos'
import { AlertaError } from '../components/Alerta'
import { Cargando, ErrorCarga } from '../components/Estados'
import { fecha, nombreEstadoPostulacion } from '../utils/formato'
import { descargar, useAdmin } from './useAdmin'

const estados = Object.keys(nombreEstadoPostulacion) as EstadoPostulacion[]

function Detalle({ p, alGuardar }: { p: Postulacion; alGuardar: () => void }) {
  const [estado, setEstado] = useState(p.estado)
  const [notas, setNotas] = useState(p.notasInternas ?? '')
  const [notificar, setNotificar] = useState(true)
  const [error, setError] = useState<unknown>(null)
  const [guardando, setGuardando] = useState(false)

  async function guardar() {
    setGuardando(true)
    setError(null)
    try {
      await api(`/api/admin/postulaciones/${p.id}/estado`, {
        method: 'PATCH',
        auth: true,
        body: { estado, notasInternas: notas, notificar },
      })
      alGuardar()
    } catch (e) {
      setError(e)
    } finally {
      setGuardando(false)
    }
  }

  const filas: [string, string | undefined][] = [
    ['Postula a', p.postulacionA],
    ['Email', p.email],
    ['Teléfono', p.telefono],
    ['Documento', p.documentoIdentidad],
    ['Residencia', [p.ciudad, p.paisResidencia].filter(Boolean).join(', ')],
    ['Profesión', p.profesion],
    ['Lengua de señas', { NINGUNO: 'Nada aún', BASICO: 'Básico', INTERMEDIO: 'Intermedio', AVANZADO: 'Avanzado' }[p.nivelLenguaSenas ?? ''] ?? p.nivelLenguaSenas],
    ['Disponibilidad', p.disponibilidad],
  ]

  return (
    <div className="caja admin__detalle">
      <h2>
        {p.nombres} {p.apellidos} <small>{p.codigo}</small>
      </h2>
      <dl>
        {filas.map(([k, v]) =>
          v ? (
            <div key={k}>
              <dt>{k}</dt>
              <dd>{v}</dd>
            </div>
          ) : null,
        )}
      </dl>
      <h3>Motivación</h3>
      <p>{p.motivacion}</p>
      {p.experiencia && (
        <>
          <h3>Experiencia</h3>
          <p>{p.experiencia}</p>
        </>
      )}
      {p.cvUrl && (
        <button className="boton boton--blanco boton--chico" onClick={() => descargar(`/api/admin/archivos/${p.cvUrl}`, `CV-${p.codigo}.pdf`)}>
          Descargar CV
        </button>
      )}
      <div className="formulario">
        <div className="campo">
          <label htmlFor="adm-estado">Estado</label>
          <select id="adm-estado" value={estado} onChange={(e) => setEstado(e.target.value as EstadoPostulacion)}>
            {estados.map((e) => (
              <option key={e} value={e}>
                {nombreEstadoPostulacion[e]}
              </option>
            ))}
          </select>
        </div>
        <div className="campo">
          <label htmlFor="adm-notas">Notas internas</label>
          <textarea id="adm-notas" value={notas} onChange={(e) => setNotas(e.target.value)} />
        </div>
        <label className="check">
          <input type="checkbox" checked={notificar} onChange={(e) => setNotificar(e.target.checked)} />
          <span>Avisar al postulante por correo si cambia el estado</span>
        </label>
        <AlertaError error={error} />
        <button className="boton boton--rosa" onClick={guardar} disabled={guardando}>
          {guardando ? 'Guardando…' : 'Guardar'}
        </button>
      </div>
    </div>
  )
}

export default function Postulaciones() {
  const [params, setParams] = useSearchParams()
  const estado = params.get('estado') ?? ''
  const [q, setQ] = useState('')
  const [pagina, setPagina] = useState(0)
  const [seleccion, setSeleccion] = useState<number | null>(null)
  const consulta = new URLSearchParams({ pagina: String(pagina), tamanio: '20' })
  if (estado) consulta.set('estado', estado)
  if (q) consulta.set('q', q)
  const { datos, error, cargando, recargar } = useAdmin<Pagina<Postulacion>>(`/api/admin/postulaciones?${consulta}`)
  const elegida = datos?.contenido.find((p) => p.id === seleccion)

  return (
    <>
      <div className="admin__encabezado">
        <h1>Postulaciones</h1>
        <button
          className="boton boton--chico"
          onClick={() => descargar(`/api/admin/postulaciones/exportar${estado ? `?estado=${estado}` : ''}`, 'postulaciones.csv')}
        >
          Exportar CSV
        </button>
      </div>
      <div className="filtros filtros--selects">
        <div className="campo">
          <label htmlFor="f-estado">Estado</label>
          <select
            id="f-estado"
            value={estado}
            onChange={(e) => {
              setPagina(0)
              setParams(e.target.value ? { estado: e.target.value } : {})
            }}
          >
            <option value="">Todos</option>
            {estados.map((e) => (
              <option key={e} value={e}>
                {nombreEstadoPostulacion[e]}
              </option>
            ))}
          </select>
        </div>
        <div className="campo">
          <label htmlFor="f-q">Buscar</label>
          <input id="f-q" placeholder="Nombre o email" value={q} onChange={(e) => { setPagina(0); setQ(e.target.value) }} />
        </div>
      </div>
      {cargando && !datos ? (
        <Cargando />
      ) : error ? (
        <ErrorCarga mensaje={error} />
      ) : (
        <div className="admin__dividido">
          <div className="tabla-envoltura">
            <table className="tabla">
              <thead>
                <tr>
                  <th>Fecha</th>
                  <th>Nombre</th>
                  <th>Postula a</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                {datos?.contenido.map((p) => (
                  <tr key={p.id} className={p.id === seleccion ? 'seleccionada' : ''} onClick={() => setSeleccion(p.id)}>
                    <td>{fecha(p.creadoEn, { day: '2-digit', month: 'short' })}</td>
                    <td>
                      <button className="tabla__enlace" onClick={() => setSeleccion(p.id)}>
                        {p.nombres} {p.apellidos}
                      </button>
                    </td>
                    <td>{p.postulacionA}</td>
                    <td>
                      <span className={`estado estado--${p.estado.toLowerCase()}`}>{nombreEstadoPostulacion[p.estado]}</span>
                    </td>
                  </tr>
                ))}
                {!datos?.contenido.length && (
                  <tr>
                    <td colSpan={4}>No hay postulaciones.</td>
                  </tr>
                )}
              </tbody>
            </table>
            {datos && datos.totalPaginas > 1 && (
              <div className="paginacion">
                <button className="boton boton--blanco boton--chico" disabled={pagina === 0} onClick={() => setPagina(pagina - 1)}>←</button>
                <span>{pagina + 1} / {datos.totalPaginas}</span>
                <button className="boton boton--blanco boton--chico" disabled={datos.ultima} onClick={() => setPagina(pagina + 1)}>→</button>
              </div>
            )}
          </div>
          {elegida ? <Detalle key={elegida.id} p={elegida} alGuardar={recargar} /> : <p className="vacio">Selecciona una postulación.</p>}
        </div>
      )}
    </>
  )
}
