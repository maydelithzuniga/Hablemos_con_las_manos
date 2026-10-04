import { useEffect } from 'react'
import { Outlet, useLocation } from 'react-router-dom'
import Cabecera from './Cabecera'
import Pie from './Pie'

/** Al cambiar de pagina vuelve arriba, o baja hasta el #ancla si la hay. */
function Desplazamiento() {
  const { pathname, hash } = useLocation()
  useEffect(() => {
    if (hash) {
      const el = document.getElementById(hash.slice(1))
      if (el) {
        setTimeout(() => el.scrollIntoView({ behavior: 'smooth', block: 'start' }), 50)
        return
      }
    }
    window.scrollTo(0, 0)
  }, [pathname, hash])
  return null
}

export default function Layout() {
  return (
    <>
      <a href="#contenido" className="saltar">
        Saltar al contenido
      </a>
      <Desplazamiento />
      <Cabecera />
      <main id="contenido">
        <Outlet />
      </main>
      <Pie />
    </>
  )
}
