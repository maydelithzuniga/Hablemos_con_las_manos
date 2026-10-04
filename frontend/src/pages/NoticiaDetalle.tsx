import { Link, useParams } from 'react-router-dom'
import { urlArchivo } from '../api/cliente'
import type { Noticia } from '../api/tipos'
import { Cargando, ErrorCarga } from '../components/Estados'
import { fotoRelleno } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'
import { fecha } from '../utils/formato'

export default function NoticiaDetalle() {
  const { slug } = useParams()
  const { datos: n, cargando, error } = useApi<Noticia>(`/api/noticias/${slug}`, relleno.noticias.find((x) => x.slug === slug))

  if (cargando) return <Cargando />
  if (error || !n) return <ErrorCarga mensaje={error ?? 'No encontramos esta noticia.'} />

  const compartir = encodeURIComponent(window.location.href)

  return (
    <article>
      <section className="portada-foto">
        <img src={urlArchivo(n.imagenUrl) ?? fotoRelleno(n.id + 1)} alt="" />
        <div className="contenedor portada-foto__texto">
          {n.categoria && <span className="etiqueta">{n.categoria}</span>}
          <h1>{n.titulo}</h1>
          <p>
            {n.autor && <>Por {n.autor} · </>}
            <time dateTime={n.fechaPublicacion}>{fecha(n.fechaPublicacion)}</time>
          </p>
        </div>
      </section>
      <section className="seccion seccion--confeti">
        <div className="contenedor contenedor--angosto">
          <div className="caja contenido-rico">
            <p className="noticia__bajada">{n.resumen}</p>
            {n.contenido.split(/\n{2,}/).map((parrafo, i) => (
              <p key={i}>{parrafo}</p>
            ))}
            <hr />
            <p className="noticia__compartir">
              Compartir:{' '}
              <a href={`https://wa.me/?text=${compartir}`} target="_blank" rel="noreferrer">
                WhatsApp
              </a>{' '}
              ·{' '}
              <a href={`https://www.facebook.com/sharer/sharer.php?u=${compartir}`} target="_blank" rel="noreferrer">
                Facebook
              </a>{' '}
              ·{' '}
              <a href={`https://www.linkedin.com/sharing/share-offsite/?url=${compartir}`} target="_blank" rel="noreferrer">
                LinkedIn
              </a>
            </p>
          </div>
          <p>
            <Link to="/noticias" className="enlace-flecha">
              ← Volver a noticias
            </Link>
          </p>
        </div>
      </section>
    </article>
  )
}
