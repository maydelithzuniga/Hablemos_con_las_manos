import { Link, useParams } from 'react-router-dom'
import { urlArchivo } from '../api/cliente'
import type { Programa } from '../api/tipos'
import CabeceraPagina from '../components/CabeceraPagina'
import { Cargando, ErrorCarga } from '../components/Estados'
import { personajeRelleno } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'
import { nombreModalidad, nombreTipoPrograma } from '../utils/formato'

export default function ProgramaDetalle() {
  const { slug } = useParams()
  const { datos: p, cargando, error } = useApi<Programa>(`/api/programas/${slug}`, relleno.programas.find((x) => x.slug === slug))

  if (cargando) return <Cargando />
  if (error || !p) return <ErrorCarga mensaje={error ?? 'No encontramos este programa.'} />

  const lineas = (texto?: string) => texto?.split(/(?<=\.)\s+|\n/).filter(Boolean) ?? []

  return (
    <>
      <CabeceraPagina
        color="rosa"
        etiqueta={`Voluntariado ${nombreTipoPrograma[p.tipo]}`}
        titulo={p.titulo}
        bajada={p.resumen}
        imagen={urlArchivo(p.imagenUrl) ?? personajeRelleno(p.id)}
      >
        <div className="cabecera-pagina__acciones">
          <Link to={`/postula?programa=${p.id}`} className="boton boton--grande">
            Postular a este programa
          </Link>
        </div>
      </CabeceraPagina>
      <section className="seccion seccion--confeti">
        <div className="contenedor detalle">
          <article className="caja contenido-rico">
            <h2>Sobre el programa</h2>
            <p>{p.descripcion}</p>
            {p.requisitos && (
              <>
                <h3>Requisitos</h3>
                <ul className="lista-check">
                  {lineas(p.requisitos).map((l) => (
                    <li key={l}>{l}</li>
                  ))}
                </ul>
              </>
            )}
            {p.beneficios && (
              <>
                <h3>Lo que recibes</h3>
                <ul className="lista-check">
                  {lineas(p.beneficios).map((l) => (
                    <li key={l}>{l}</li>
                  ))}
                </ul>
              </>
            )}
          </article>
          <aside className="caja caja--borde detalle__lateral">
            <dl>
              <dt>Tipo</dt>
              <dd>{nombreTipoPrograma[p.tipo]}</dd>
              <dt>Modalidad</dt>
              <dd>{nombreModalidad[p.modalidad]}</dd>
              {p.duracion && (
                <>
                  <dt>Duración</dt>
                  <dd>{p.duracion}</dd>
                </>
              )}
            </dl>
            <Link to={`/postula?programa=${p.id}`} className="boton boton--rosa">
              Postular
            </Link>
            <Link to="/voluntariado#convocatorias" className="enlace-flecha">
              Ver convocatorias abiertas →
            </Link>
          </aside>
        </div>
      </section>
    </>
  )
}
