import { useEffect, useState } from 'react'
import { Link, NavLink, useLocation } from 'react-router-dom'
import { img } from '../data/imagenes'
import { menu, sitio } from '../data/sitio'

export default function Cabecera() {
  const [abierto, setAbierto] = useState(false)
  const [submenu, setSubmenu] = useState<string | null>(null)
  const [scroll, setScroll] = useState(false)
  const { pathname, hash } = useLocation()

  useEffect(() => {
    setAbierto(false)
    setSubmenu(null)
  }, [pathname, hash])

  useEffect(() => {
    const alScroll = () => setScroll(window.scrollY > 10)
    alScroll()
    window.addEventListener('scroll', alScroll, { passive: true })
    return () => window.removeEventListener('scroll', alScroll)
  }, [])

  return (
    <>
      <div className="barra-superior">
        <div className="contenedor barra-superior__fila">
          <span>
            ¡Hola! <strong>Aprende con nosotros</strong> a romper barreras con las manos 🤟
          </span>
          <nav aria-label="Accesos rapidos" className="barra-superior__enlaces">
            <Link to="/seguimiento">Estado de mi postulación</Link>
            <a href={`mailto:${sitio.email}`}>{sitio.email}</a>
          </nav>
        </div>
      </div>
      <header className={`cabecera ${scroll ? 'cabecera--scroll' : ''}`}>
        <div className="contenedor cabecera__fila">
          <Link to="/" className="marca" aria-label={`${sitio.nombre}, ir al inicio`}>
            <img src={img.logo} alt="" width={56} height={56} />
            <span className="marca__texto">
              <strong>Hablemos</strong>
              <span>con las Manos</span>
            </span>
          </Link>

          <button
            className="cabecera__hamburguesa"
            aria-expanded={abierto}
            aria-controls="menu-principal"
            onClick={() => setAbierto((a) => !a)}
          >
            <span className="sr-only">{abierto ? 'Cerrar menú' : 'Abrir menú'}</span>
            <span aria-hidden="true" />
          </button>

          <nav id="menu-principal" className={`menu ${abierto ? 'menu--abierto' : ''}`} aria-label="Menú principal">
            <ul>
              {menu.map((item) =>
                item.hijos ? (
                  <li
                    key={item.titulo}
                    className={`menu__item menu__item--padre ${submenu === item.titulo ? 'menu__item--abierto' : ''}`}
                    onMouseEnter={() => setSubmenu(item.titulo)}
                    onMouseLeave={() => setSubmenu(null)}
                  >
                    <button
                      className="menu__enlace"
                      aria-expanded={submenu === item.titulo}
                      onClick={() => setSubmenu((s) => (s === item.titulo ? null : item.titulo))}
                    >
                      {item.titulo}
                      <svg width="12" height="12" viewBox="0 0 12 12" aria-hidden="true">
                        <path d="M2 4l4 4 4-4" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
                      </svg>
                    </button>
                    <ul className="menu__sub">
                      {item.hijos.map((h) => (
                        <li key={h.ruta}>
                          <Link to={h.ruta}>{h.titulo}</Link>
                        </li>
                      ))}
                    </ul>
                  </li>
                ) : (
                  <li key={item.titulo} className="menu__item">
                    <NavLink to={item.ruta!} className="menu__enlace">
                      {item.titulo}
                    </NavLink>
                  </li>
                ),
              )}
            </ul>
            <div className="menu__acciones">
              <Link to="/postula" className="boton boton--blanco boton--chico">
                Postula
              </Link>
              <Link to="/dona" className="boton boton--rosa boton--chico">
                ♥ Dona
              </Link>
            </div>
          </nav>
        </div>
      </header>
    </>
  )
}
