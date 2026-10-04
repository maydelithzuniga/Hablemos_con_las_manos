import { Link } from 'react-router-dom'
import { img } from '../data/imagenes'
import { sitio } from '../data/sitio'
import Newsletter from './Newsletter'

export default function Pie() {
  const anio = new Date().getFullYear()
  return (
    <footer className="pie">
      <Newsletter />
      <div className="pie__principal">
        <div className="contenedor pie__grid">
          <div className="pie__marca">
            <img src={img.logoGrande} alt="" width={96} height={96} />
            <p>
              <strong>{sitio.nombre}</strong>
              <br />
              {sitio.lema}. Voluntariado por la inclusión de la comunidad sorda y la Lengua de Señas Peruana.
            </p>
            <ul className="pie__redes">
              {sitio.redes.map((r) => (
                <li key={r.nombre}>
                  <a href={r.url} target="_blank" rel="noreferrer">
                    {r.nombre}
                  </a>
                </li>
              ))}
            </ul>
          </div>
          <div>
            <h3>Nosotros</h3>
            <ul>
              <li><Link to="/nosotros">Quiénes somos</Link></li>
              <li><Link to="/nosotros#equipo">Equipo</Link></li>
              <li><Link to="/transparencia">Transparencia</Link></li>
              <li><Link to="/noticias">Noticias</Link></li>
            </ul>
          </div>
          <div>
            <h3>Súmate</h3>
            <ul>
              <li><Link to="/voluntariado">Voluntariado</Link></li>
              <li><Link to="/postula">Postula</Link></li>
              <li><Link to="/dona">Dona</Link></li>
              <li><Link to="/empresas">Empresas</Link></li>
            </ul>
          </div>
          <div>
            <h3>Contacto</h3>
            <ul>
              <li><a href={`mailto:${sitio.email}`}>{sitio.email}</a></li>
              <li>{sitio.telefono}</li>
              <li>{sitio.direccion}</li>
              <li>{sitio.horario}</li>
            </ul>
          </div>
        </div>
      </div>
      <div className="pie__legal">
        <div className="contenedor pie__legal-fila">
          <span>© {anio} {sitio.nombre}. Todos los derechos reservados.</span>
          <span>
            Ilustraciones: <strong>{sitio.usuarioRedes}</strong> · <Link to="/admin">Acceso equipo</Link>
          </span>
        </div>
      </div>
    </footer>
  )
}
