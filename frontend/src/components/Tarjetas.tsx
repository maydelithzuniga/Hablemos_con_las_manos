import { Link } from 'react-router-dom'
import { urlArchivo } from '../api/cliente'
import type { Convocatoria, NoticiaResumen, Programa, Proyecto } from '../api/tipos'
import { fotoRelleno, personajeRelleno } from '../data/imagenes'
import { diasRestantes, fecha, nombreArea, nombreEstadoProyecto, nombreModalidad, nombreTipoPrograma, numero } from '../utils/formato'

export function TarjetaPrograma({ programa, indice = 0 }: { programa: Programa; indice?: number }) {
  const imagen = urlArchivo(programa.imagenUrl)
  return (
    <Link to={`/voluntariado/${programa.slug}`} className={`tarjeta tarjeta-programa tarjeta-programa--${indice % 3}`}>
      <div className={`tarjeta__imagen ${imagen ? '' : 'tarjeta__imagen--ilustracion'}`}>
        <img src={imagen ?? personajeRelleno(indice)} alt="" loading="lazy" />
      </div>
      <div className="tarjeta__cuerpo">
        <span className="etiqueta">{nombreTipoPrograma[programa.tipo]}</span>
        <h3>{programa.titulo}</h3>
        <p>{programa.resumen}</p>
        <div className="tarjeta__pie">
          <span>
            {nombreModalidad[programa.modalidad]}
            {programa.duracion ? ` · ${programa.duracion}` : ''}
          </span>
          <span className="enlace-flecha">Conocer más →</span>
        </div>
      </div>
    </Link>
  )
}

export function TarjetaProyecto({ proyecto, indice = 0 }: { proyecto: Proyecto; indice?: number }) {
  return (
    <Link to={`/proyectos/${proyecto.slug}`} className="tarjeta">
      <div className="tarjeta__imagen">
        <img src={urlArchivo(proyecto.imagenUrl) ?? fotoRelleno(indice)} alt="" loading="lazy" />
        <span className="tarjeta__pais">{proyecto.pais.nombre}</span>
      </div>
      <div className="tarjeta__cuerpo">
        <div className="tarjeta__etiquetas">
          <span className="etiqueta etiqueta--amarilla">{nombreArea[proyecto.area]}</span>
          <span className="etiqueta etiqueta--violeta">{nombreEstadoProyecto[proyecto.estado]}</span>
        </div>
        <h3>{proyecto.titulo}</h3>
        <p>{proyecto.resumen}</p>
        <div className="tarjeta__pie">
          <span>
            <strong>{numero(proyecto.beneficiarios)}</strong> personas beneficiadas
          </span>
          <span className="enlace-flecha">Ver proyecto →</span>
        </div>
      </div>
    </Link>
  )
}

export function TarjetaNoticia({ noticia, indice = 0 }: { noticia: NoticiaResumen; indice?: number }) {
  return (
    <Link to={`/noticias/${noticia.slug}`} className="tarjeta">
      <div className="tarjeta__imagen">
        <img src={urlArchivo(noticia.imagenUrl) ?? fotoRelleno(indice + 2)} alt="" loading="lazy" />
      </div>
      <div className="tarjeta__cuerpo">
        {noticia.categoria && <span className="etiqueta">{noticia.categoria}</span>}
        <h3>{noticia.titulo}</h3>
        <p>{noticia.resumen}</p>
        <div className="tarjeta__pie">
          <time dateTime={noticia.fechaPublicacion}>{fecha(noticia.fechaPublicacion)}</time>
          <span className="enlace-flecha">Leer →</span>
        </div>
      </div>
    </Link>
  )
}

export function TarjetaConvocatoria({ convocatoria }: { convocatoria: Convocatoria }) {
  const restantes = diasRestantes(convocatoria.fechaCierre)
  return (
    <article className="tarjeta tarjeta-convocatoria">
      <div className="tarjeta__cuerpo">
        <div className="tarjeta__etiquetas">
          <span className="etiqueta">{convocatoria.programaTitulo}</span>
          {convocatoria.pais && <span className="etiqueta etiqueta--amarilla">{convocatoria.pais.nombre}</span>}
        </div>
        <h3>{convocatoria.titulo}</h3>
        {convocatoria.descripcion && <p>{convocatoria.descripcion}</p>}
        <dl className="tarjeta-convocatoria__datos">
          <div>
            <dt>Cierre</dt>
            <dd>{fecha(convocatoria.fechaCierre)}</dd>
          </div>
          {convocatoria.cupos && (
            <div>
              <dt>Cupos</dt>
              <dd>{convocatoria.cupos}</dd>
            </div>
          )}
          {convocatoria.fechaInicioVoluntariado && (
            <div>
              <dt>Inicio</dt>
              <dd>{fecha(convocatoria.fechaInicioVoluntariado)}</dd>
            </div>
          )}
        </dl>
        {convocatoria.perfiles && (
          <p className="tarjeta-convocatoria__perfiles">
            <strong>Buscamos:</strong> {convocatoria.perfiles}
          </p>
        )}
        <div className="tarjeta__pie">
          <span className="tarjeta-convocatoria__restan">
            {restantes === 0 ? '¡Último día!' : `Quedan ${restantes} días`}
          </span>
          <Link to={`/postula?convocatoria=${convocatoria.id}`} className="boton boton--rosa boton--chico">
            Postular
          </Link>
        </div>
      </div>
    </article>
  )
}
