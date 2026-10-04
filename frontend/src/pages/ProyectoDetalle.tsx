import { Link, useParams } from 'react-router-dom'
import { urlArchivo } from '../api/cliente'
import type { Proyecto } from '../api/tipos'
import { Cargando, ErrorCarga } from '../components/Estados'
import { fotoRelleno } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'
import { fecha, nombreArea, nombreEstadoProyecto, numero } from '../utils/formato'

export default function ProyectoDetalle() {
  const { slug } = useParams()
  const { datos: p, cargando, error } = useApi<Proyecto>(`/api/proyectos/${slug}`, relleno.proyectos.find((x) => x.slug === slug))

  if (cargando) return <Cargando />
  if (error || !p) return <ErrorCarga mensaje={error ?? 'No encontramos este proyecto.'} />

  return (
    <>
      <section className="portada-foto">
        <img src={urlArchivo(p.imagenUrl) ?? fotoRelleno(p.id - 1)} alt="" />
        <div className="contenedor portada-foto__texto">
          <div className="tarjeta__etiquetas">
            <span className="etiqueta etiqueta--amarilla">{nombreArea[p.area]}</span>
            <span className="etiqueta">{p.pais.nombre}</span>
          </div>
          <h1>{p.titulo}</h1>
          <p>{p.resumen}</p>
        </div>
      </section>
      <section className="seccion seccion--confeti">
        <div className="contenedor detalle">
          <article className="caja contenido-rico">
            <h2>Sobre el proyecto</h2>
            <p>{p.descripcion}</p>
          </article>
          <aside className="caja caja--borde detalle__lateral">
            <p className="detalle__cifra">
              <span className="display">{numero(p.beneficiarios)}</span>
              personas beneficiadas
            </p>
            <dl>
              <dt>Estado</dt>
              <dd>{nombreEstadoProyecto[p.estado]}</dd>
              <dt>País</dt>
              <dd>
                <Link to={`/donde-estamos/${p.pais.codigo}`}>{p.pais.nombre}</Link>
              </dd>
              {p.programaTitulo && (
                <>
                  <dt>Programa</dt>
                  <dd>{p.programaTitulo}</dd>
                </>
              )}
              {p.socioLocal && (
                <>
                  <dt>Socio local</dt>
                  <dd>{p.socioLocal}</dd>
                </>
              )}
              {p.fechaInicio && (
                <>
                  <dt>Inicio</dt>
                  <dd>{fecha(p.fechaInicio)}</dd>
                </>
              )}
            </dl>
            <Link to={`/dona?proyecto=${p.id}`} className="boton boton--rosa">
              Apoya este proyecto
            </Link>
            <Link to="/voluntariado" className="boton boton--blanco">
              Sé voluntari@
            </Link>
          </aside>
        </div>
      </section>
    </>
  )
}
