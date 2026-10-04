import { urlArchivo } from '../api/cliente'
import type { Aliado } from '../api/tipos'
import { nombreTipoAliado } from '../utils/formato'

/** Franja de logos de aliados (si no hay logo se muestra el nombre). */
export default function Aliados({ aliados }: { aliados: Aliado[] }) {
  if (!aliados.length) return null
  return (
    <ul className="aliados">
      {aliados.map((a) => {
        const contenido = a.logoUrl ? (
          <img src={urlArchivo(a.logoUrl)} alt={a.nombre} loading="lazy" />
        ) : (
          <span className="aliados__nombre">
            <strong>{a.nombre}</strong>
            <small>{nombreTipoAliado[a.tipo]}</small>
          </span>
        )
        return (
          <li key={a.id} className="aliados__item">
            {a.sitioWeb ? (
              <a href={a.sitioWeb} target="_blank" rel="noreferrer">
                {contenido}
              </a>
            ) : (
              contenido
            )}
          </li>
        )
      })}
    </ul>
  )
}
