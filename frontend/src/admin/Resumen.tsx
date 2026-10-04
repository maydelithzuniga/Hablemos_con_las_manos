import { Link } from 'react-router-dom'
import type { Dashboard } from '../api/tipos'
import { Cargando, ErrorCarga } from '../components/Estados'
import { nombreEstadoPostulacion } from '../utils/formato'
import { useAdmin } from './useAdmin'
import { useSesion } from './Sesion'

export default function Resumen() {
  const { usuario } = useSesion()
  const { datos, error, cargando } = useAdmin<Dashboard>('/api/admin/dashboard')
  if (usuario?.rol !== 'ADMIN') return <p>Usa el menú para gestionar el contenido del sitio.</p>
  if (cargando) return <Cargando />
  if (error || !datos) return <ErrorCarga mensaje={error ?? 'Sin datos'} />

  const tarjetas = [
    { titulo: 'Mensajes sin leer', valor: datos.mensajesSinLeer, ruta: '/admin/mensajes' },
    { titulo: 'Convocatorias abiertas', valor: datos.convocatoriasAbiertas },
    { titulo: 'Suscriptores', valor: datos.suscriptoresActivos },
    { titulo: 'Socios mensuales', valor: datos.sociosMensuales, ruta: '/admin/donaciones' },
    { titulo: 'Donaciones pendientes', valor: datos.donacionesPendientes, ruta: '/admin/donaciones' },
    { titulo: 'Testimonios por aprobar', valor: datos.testimoniosPorAprobar, ruta: '/admin/testimonios' },
  ]

  return (
    <>
      <h1>Hola, {usuario.nombre} 👋</h1>
      <div className="grid grid--3 admin__tarjetas">
        {tarjetas.map((t) => (
          <div key={t.titulo} className="caja">
            <span className="display">{t.valor}</span>
            <p>{t.ruta ? <Link to={t.ruta}>{t.titulo}</Link> : t.titulo}</p>
          </div>
        ))}
        <div className="caja">
          <h2>Recaudado (30 días)</h2>
          {Object.entries(datos.recaudadoUltimos30Dias).map(([moneda, monto]) => (
            <p key={moneda}>
              <strong>{moneda}</strong> {Number(monto).toLocaleString('es-PE')}
            </p>
          ))}
        </div>
        <div className="caja">
          <h2>Postulaciones</h2>
          <ul className="admin__lista-estados">
            {Object.entries(datos.postulacionesPorEstado).map(([estado, n]) => (
              <li key={estado}>
                <Link to={`/admin/postulaciones?estado=${estado}`}>
                  {nombreEstadoPostulacion[estado as keyof typeof nombreEstadoPostulacion]}
                </Link>
                <strong>{n}</strong>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </>
  )
}
