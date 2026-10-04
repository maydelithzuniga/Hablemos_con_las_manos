import { Link } from 'react-router-dom'
import { img } from '../data/imagenes'

export default function NoEncontrado() {
  return (
    <section className="seccion seccion--confeti exito">
      <div className="contenedor exito__fila">
        <img src={img.gatoCurioso} alt="" />
        <div className="caja caja--borde">
          <span className="globo">¿Miau?</span>
          <h1 className="display">Página no encontrada</h1>
          <p>Parece que esta página se perdió en el parque. ¡Volvamos al inicio!</p>
          <Link to="/" className="boton boton--rosa">
            Ir al inicio
          </Link>
        </div>
      </div>
    </section>
  )
}
