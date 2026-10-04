import { useEffect, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { api } from '../api/cliente'
import { img } from '../data/imagenes'

export default function BajaNewsletter() {
  const [params] = useSearchParams()
  const token = params.get('token')
  const [mensaje, setMensaje] = useState('Procesando…')

  useEffect(() => {
    if (!token) {
      setMensaje('El enlace no es válido.')
      return
    }
    api<{ mensaje: string }>(`/api/newsletter/${encodeURIComponent(token)}`, { method: 'DELETE' })
      .then((r) => setMensaje(r.mensaje))
      .catch((e: Error) => setMensaje(e.message))
  }, [token])

  return (
    <section className="seccion seccion--confeti exito">
      <div className="contenedor exito__fila">
        <img src={img.gatoCurioso} alt="" />
        <div className="caja caja--borde">
          <h1>Boletín</h1>
          <p role="status">{mensaje}</p>
          <p>¡Te vamos a extrañar! Puedes volver a suscribirte cuando quieras.</p>
          <Link to="/" className="boton boton--rosa">
            Volver al inicio
          </Link>
        </div>
      </div>
    </section>
  )
}
