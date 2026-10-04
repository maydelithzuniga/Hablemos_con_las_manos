import { useState, type FormEvent } from 'react'
import { Link, NavLink, Outlet } from 'react-router-dom'
import { AlertaError } from '../components/Alerta'
import { Cargando } from '../components/Estados'
import { img } from '../data/imagenes'
import { useSesion } from './Sesion'

function Login() {
  const { ingresar } = useSesion()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<unknown>(null)
  const [enviando, setEnviando] = useState(false)

  async function enviar(e: FormEvent) {
    e.preventDefault()
    setEnviando(true)
    setError(null)
    try {
      await ingresar(email, password)
    } catch (err) {
      setError(err)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <section className="seccion seccion--confeti exito">
      <div className="mancha mancha--amarilla" aria-hidden="true" />
      <div className="contenedor exito__fila">
        <img src={img.gatoSentado} alt="" />
        <form className="caja caja--borde formulario" onSubmit={enviar}>
          <h1>Panel del equipo</h1>
          <div className="campo">
            <label htmlFor="adm-email">Correo</label>
            <input id="adm-email" type="email" required value={email} onChange={(e) => setEmail(e.target.value)} autoComplete="username" />
          </div>
          <div className="campo">
            <label htmlFor="adm-pass">Contraseña</label>
            <input id="adm-pass" type="password" required value={password} onChange={(e) => setPassword(e.target.value)} autoComplete="current-password" />
          </div>
          <AlertaError error={error} />
          <button className="boton boton--rosa" disabled={enviando}>
            {enviando ? 'Ingresando…' : 'Ingresar'}
          </button>
          <Link to="/" className="enlace-flecha">
            ← Volver al sitio
          </Link>
        </form>
      </div>
    </section>
  )
}

export default function AdminLayout() {
  const { usuario, cargando, salir } = useSesion()
  if (cargando) return <Cargando />
  if (!usuario) return <Login />

  const esAdmin = usuario.rol === 'ADMIN'
  return (
    <div className="admin">
      <aside className="admin__menu">
        <Link to="/" className="marca">
          <img src={img.logo} alt="" width={44} height={44} />
          <span className="marca__texto">
            <strong>Panel</strong>
            <span>Hablemos con las Manos</span>
          </span>
        </Link>
        <nav aria-label="Panel">
          {esAdmin && <NavLink to="/admin" end>Resumen</NavLink>}
          {esAdmin && <NavLink to="/admin/postulaciones">Postulaciones</NavLink>}
          {esAdmin && <NavLink to="/admin/mensajes">Mensajes</NavLink>}
          {esAdmin && <NavLink to="/admin/donaciones">Donaciones</NavLink>}
          <NavLink to="/admin/noticias">Noticias</NavLink>
          <NavLink to="/admin/testimonios">Testimonios</NavLink>
        </nav>
        <div className="admin__usuario">
          <strong>{usuario.nombre}</strong>
          <span>{usuario.rol === 'ADMIN' ? 'Administrador' : 'Editor'}</span>
          <button className="boton boton--blanco boton--chico" onClick={salir}>
            Cerrar sesión
          </button>
        </div>
      </aside>
      <div className="admin__contenido">
        <Outlet />
      </div>
    </div>
  )
}
