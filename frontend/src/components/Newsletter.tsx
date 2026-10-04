import { useState, type FormEvent } from 'react'
import { api } from '../api/cliente'
import { img } from '../data/imagenes'

export default function Newsletter() {
  const [email, setEmail] = useState('')
  const [estado, setEstado] = useState<{ tipo: 'exito' | 'error'; texto: string } | null>(null)
  const [enviando, setEnviando] = useState(false)

  async function enviar(e: FormEvent) {
    e.preventDefault()
    setEnviando(true)
    try {
      const r = await api<{ mensaje: string }>('/api/newsletter', { method: 'POST', body: { email } })
      setEstado({ tipo: 'exito', texto: r.mensaje })
      setEmail('')
    } catch (err) {
      setEstado({ tipo: 'error', texto: (err as Error).message })
    } finally {
      setEnviando(false)
    }
  }

  return (
    <section className="newsletter" aria-labelledby="titulo-newsletter">
      <div className="contenedor newsletter__fila">
        <img src={img.gatoFeliz} alt="" className="newsletter__gato" width={150} height={150} />
        <div>
          <h2 id="titulo-newsletter">¡Recibe nuestras novedades!</h2>
          <p>Convocatorias, talleres de LSP e historias de la comunidad, directo a tu correo.</p>
        </div>
        <form className="newsletter__form" onSubmit={enviar}>
          <label htmlFor="newsletter-email" className="sr-only">
            Tu correo electrónico
          </label>
          <input
            id="newsletter-email"
            type="email"
            required
            placeholder="tucorreo@ejemplo.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <button className="boton boton--rosa" disabled={enviando}>
            {enviando ? 'Enviando…' : 'Suscribirme'}
          </button>
          {estado && (
            <p className={`newsletter__estado newsletter__estado--${estado.tipo}`} role="status">
              {estado.texto}
            </p>
          )}
        </form>
      </div>
    </section>
  )
}
