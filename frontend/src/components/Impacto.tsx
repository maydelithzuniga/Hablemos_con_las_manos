import { useEffect, useRef, useState } from 'react'
import type { Impacto as TipoImpacto } from '../api/tipos'
import { numero } from '../utils/formato'

const iconos: Record<string, string> = {
  users: '🙌',
  heart: '💛',
  globe: '🌎',
  calendar: '📅',
}

/** Cuenta de 0 al valor cuando el numero aparece en pantalla. */
function Contador({ valor }: { valor: number }) {
  const ref = useRef<HTMLSpanElement>(null)
  const [actual, setActual] = useState(0)

  useEffect(() => {
    const el = ref.current
    if (!el) return
    const reducido = window.matchMedia('(prefers-reduced-motion: reduce)').matches
    if (reducido || !('IntersectionObserver' in window)) {
      setActual(valor)
      return
    }
    let frame = 0
    const observador = new IntersectionObserver(([entrada]) => {
      if (!entrada.isIntersecting) return
      observador.disconnect()
      const inicio = performance.now()
      const paso = (t: number) => {
        const p = Math.min(1, (t - inicio) / 1400)
        setActual(Math.round(valor * (1 - Math.pow(1 - p, 3))))
        if (p < 1) frame = requestAnimationFrame(paso)
      }
      frame = requestAnimationFrame(paso)
    })
    observador.observe(el)
    return () => {
      observador.disconnect()
      cancelAnimationFrame(frame)
    }
  }, [valor])

  return <span ref={ref}>{numero(actual)}</span>
}

export default function Impacto({ impacto }: { impacto: TipoImpacto }) {
  const cifras = impacto.cifras.length
    ? impacto.cifras
    : [
        { id: 1, etiqueta: 'Países', valor: impacto.paisesActivos, icono: 'globe', orden: 1 },
        { id: 2, etiqueta: 'Proyectos en curso', valor: impacto.proyectosEnCurso, icono: 'heart', orden: 2 },
        { id: 3, etiqueta: 'Personas beneficiadas', valor: impacto.beneficiarios, icono: 'users', orden: 3 },
      ]
  return (
    <ul className="impacto">
      {cifras.map((c, i) => (
        <li key={c.id} className={`impacto__item impacto__item--${i % 4}`}>
          <span className="impacto__icono" aria-hidden="true">
            {iconos[c.icono ?? ''] ?? '🤟'}
          </span>
          <span className="impacto__valor display">
            {c.prefijo}
            <Contador valor={c.valor} />
            {c.sufijo}
          </span>
          <span className="impacto__etiqueta">{c.etiqueta}</span>
        </li>
      ))}
    </ul>
  )
}
