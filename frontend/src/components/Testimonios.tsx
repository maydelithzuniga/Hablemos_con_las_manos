import { useState } from 'react'
import { urlArchivo } from '../api/cliente'
import type { Testimonio } from '../api/tipos'
import { personajeRelleno } from '../data/imagenes'

export default function Testimonios({ testimonios }: { testimonios: Testimonio[] }) {
  const [actual, setActual] = useState(0)
  if (!testimonios.length) return null
  const t = testimonios[actual % testimonios.length]
  const ir = (paso: number) => setActual((a) => (a + paso + testimonios.length) % testimonios.length)

  return (
    <div className="testimonios">
      <img
        className="testimonios__personaje"
        src={urlArchivo(t.fotoUrl) ?? personajeRelleno(actual + 1)}
        alt=""
        key={t.id}
      />
      <figure className="testimonios__globo" aria-live="polite">
        <blockquote>“{t.texto}”</blockquote>
        <figcaption>
          <strong>{t.nombre}</strong>
          {t.rol && <span>{t.rol}</span>}
        </figcaption>
      </figure>
      {testimonios.length > 1 && (
        <div className="testimonios__controles">
          <button className="boton boton--blanco boton--chico" onClick={() => ir(-1)} aria-label="Testimonio anterior">
            ←
          </button>
          <span>
            {actual + 1} / {testimonios.length}
          </span>
          <button className="boton boton--blanco boton--chico" onClick={() => ir(1)} aria-label="Testimonio siguiente">
            →
          </button>
        </div>
      )}
    </div>
  )
}
