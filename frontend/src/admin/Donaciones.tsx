import { useState } from 'react'
import { api } from '../api/cliente'
import type { Donacion, EstadoDonacion, Pagina } from '../api/tipos'
import { Cargando, ErrorCarga } from '../components/Estados'
import { fecha } from '../utils/formato'
import { descargar, useAdmin } from './useAdmin'

const estados: EstadoDonacion[] = ['PENDIENTE', 'COMPLETADA', 'FALLIDA', 'CANCELADA']

export default function Donaciones() {
  const [estado, setEstado] = useState('')
  const { datos, error, cargando, recargar } = useAdmin<Pagina<Donacion>>(`/api/admin/donaciones?tamanio=50${estado ? `&estado=${estado}` : ''}`)

  async function cambiar(d: Donacion, nuevo: EstadoDonacion) {
    if (!confirm(`¿Marcar la donación ${d.referencia} como ${nuevo}?`)) return
    await api(`/api/admin/donaciones/${d.id}/estado?estado=${nuevo}`, { method: 'PATCH', auth: true })
    recargar()
  }

  return (
    <>
      <div className="admin__encabezado">
        <h1>Donaciones</h1>
        <button className="boton boton--chico" onClick={() => descargar('/api/admin/donaciones/exportar', 'donaciones.csv')}>
          Exportar CSV
        </button>
      </div>
      <div className="filtros" role="group" aria-label="Estado">
        <button className="chip" aria-pressed={!estado} onClick={() => setEstado('')}>
          Todas
        </button>
        {estados.map((e) => (
          <button key={e} className="chip" aria-pressed={estado === e} onClick={() => setEstado(e)}>
            {e.toLowerCase()}
          </button>
        ))}
      </div>
      {cargando && !datos ? (
        <Cargando />
      ) : error ? (
        <ErrorCarga mensaje={error} />
      ) : (
        <div className="tabla-envoltura">
          <table className="tabla">
            <thead>
              <tr>
                <th>Fecha</th>
                <th>Donante</th>
                <th>Tipo</th>
                <th>Monto</th>
                <th>Estado</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {datos?.contenido.map((d) => (
                <tr key={d.id}>
                  <td>{fecha(d.creadoEn, { day: '2-digit', month: 'short', year: 'numeric' })}</td>
                  <td>
                    {d.nombre}
                    <br />
                    <small>{d.email}</small>
                  </td>
                  <td>{d.tipo === 'MENSUAL' ? 'Mensual' : 'Única'}</td>
                  <td>
                    {d.moneda} {d.monto}
                  </td>
                  <td>
                    <span className={`estado estado--${d.estado.toLowerCase()}`}>{d.estado.toLowerCase()}</span>
                  </td>
                  <td>
                    {d.estado === 'PENDIENTE' && (
                      <button className="boton boton--chico" onClick={() => cambiar(d, 'COMPLETADA')}>
                        Confirmar
                      </button>
                    )}
                  </td>
                </tr>
              ))}
              {!datos?.contenido.length && (
                <tr>
                  <td colSpan={6}>No hay donaciones.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </>
  )
}
