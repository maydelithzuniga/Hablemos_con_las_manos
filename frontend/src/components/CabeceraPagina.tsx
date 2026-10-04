import type { ReactNode } from 'react'

interface Props {
  etiqueta?: string
  titulo: ReactNode
  bajada?: ReactNode
  imagen?: string
  color?: 'amarillo' | 'rosa' | 'celeste'
  children?: ReactNode
}

/** Encabezado de las paginas interiores, con el estilo de las diapositivas del material. */
export default function CabeceraPagina({ etiqueta, titulo, bajada, imagen, color = 'amarillo', children }: Props) {
  return (
    <section className={`cabecera-pagina cabecera-pagina--${color}`}>
      <div className="contenedor cabecera-pagina__fila">
        <div className="cabecera-pagina__texto">
          {etiqueta && <span className="pildora cabecera-pagina__etiqueta">{etiqueta}</span>}
          <h1>{titulo}</h1>
          {bajada && <p className="cabecera-pagina__bajada">{bajada}</p>}
          {children}
        </div>
        {imagen && <img className="cabecera-pagina__imagen flota" src={imagen} alt="" />}
      </div>
    </section>
  )
}
