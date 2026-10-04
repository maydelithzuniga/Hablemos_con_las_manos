import type { ReactNode } from 'react'

interface Props {
  etiqueta?: string
  titulo: ReactNode
  texto?: ReactNode
  centro?: boolean
  accion?: ReactNode
  id?: string
}

export default function TituloSeccion({ etiqueta, titulo, texto, centro, accion, id }: Props) {
  return (
    <div className={`titulo-seccion ${centro ? 'centro' : ''}`}>
      <div className={accion ? 'titulo-seccion__fila' : undefined}>
        <div>
          {etiqueta && <span className="etiqueta">{etiqueta}</span>}
          <h2 id={id}>{titulo}</h2>
        </div>
        {accion}
      </div>
      {texto && <p>{texto}</p>}
    </div>
  )
}
