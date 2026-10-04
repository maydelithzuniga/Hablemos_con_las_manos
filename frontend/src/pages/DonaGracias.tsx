import { Link, useSearchParams } from 'react-router-dom'
import type { EstadoDonacionPublico } from '../api/tipos'
import { Cargando } from '../components/Estados'
import { img } from '../data/imagenes'
import { useApi } from '../hooks/useApi'

export default function DonaGracias() {
  const [params] = useSearchParams()
  const referencia = params.get('referencia')
  const { datos, cargando } = useApi<EstadoDonacionPublico>(referencia ? `/api/donaciones/${encodeURIComponent(referencia)}` : null)

  return (
    <section className="seccion seccion--confeti exito">
      <div className="mancha mancha--rosa" aria-hidden="true" />
      <div className="contenedor exito__fila">
        <img src={img.ninoGracias} alt="" />
        <div className="caja caja--borde">
          <span className="globo">GRACIAS</span>
          <h1 className="display">¡Gracias por tu apoyo!</h1>
          {cargando ? (
            <Cargando texto="Consultando tu donación…" />
          ) : datos ? (
            <>
              <p>
                Tu {datos.tipo === 'MENSUAL' ? 'aporte mensual' : 'donación'} de{' '}
                <strong>
                  {datos.moneda} {datos.monto}
                </strong>{' '}
                {datos.estado === 'COMPLETADA'
                  ? 'fue confirmado. ¡Te enviamos un correo de agradecimiento!'
                  : datos.estado === 'PENDIENTE'
                    ? 'está siendo procesado. Te avisaremos por correo cuando se confirme.'
                    : 'no pudo completarse. Puedes intentarlo nuevamente.'}
              </p>
              <p className="campo__ayuda">Referencia: {datos.referencia}</p>
            </>
          ) : (
            <p>Cada aporte nos ayuda a romper barreras de comunicación.</p>
          )}
          <div className="diapositiva__acciones">
            <Link to="/" className="boton boton--rosa">
              Volver al inicio
            </Link>
            <Link to="/proyectos" className="boton boton--blanco">
              Ver proyectos
            </Link>
          </div>
        </div>
      </div>
    </section>
  )
}
