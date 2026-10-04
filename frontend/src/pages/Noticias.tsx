import { useState, type FormEvent } from 'react'
import { useSearchParams } from 'react-router-dom'
import type { NoticiaResumen, Pagina } from '../api/tipos'
import CabeceraPagina from '../components/CabeceraPagina'
import { AvisoRelleno, Cargando, ErrorCarga, Vacio } from '../components/Estados'
import { TarjetaNoticia } from '../components/Tarjetas'
import { img } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'

export default function Noticias() {
  const [params, setParams] = useSearchParams()
  const q = params.get('q') ?? ''
  const categoria = params.get('categoria') ?? ''
  const pagina = Number(params.get('pagina') ?? 0)
  const [busqueda, setBusqueda] = useState(q)

  const categorias = useApi<string[]>('/api/noticias/categorias', ['Comunidad', 'Alianzas', 'Convocatorias'])
  const consulta = new URLSearchParams({ pagina: String(pagina), tamanio: '9' })
  if (q) consulta.set('q', q)
  if (categoria) consulta.set('categoria', categoria)
  const filtradas = relleno.noticiasResumen.filter(
    (n) => (!categoria || n.categoria === categoria) && (!q || n.titulo.toLowerCase().includes(q.toLowerCase())),
  )
  const { datos, cargando, error, deRelleno } = useApi<Pagina<NoticiaResumen>>(`/api/noticias?${consulta}`, {
    ...relleno.paginaNoticias,
    contenido: filtradas,
  })

  const actualizar = (cambios: Record<string, string>) => {
    const nuevos = new URLSearchParams(params)
    Object.entries(cambios).forEach(([k, v]) => (v ? nuevos.set(k, v) : nuevos.delete(k)))
    if (!('pagina' in cambios)) nuevos.delete('pagina')
    setParams(nuevos)
  }

  const buscar = (e: FormEvent) => {
    e.preventDefault()
    actualizar({ q: busqueda.trim() })
  }

  return (
    <>
      <CabeceraPagina
        color="rosa"
        etiqueta="Noticias"
        titulo="Novedades de la comunidad"
        bajada="Historias, logros, convocatorias y todo lo que pasa en Hablemos con las Manos."
        imagen={img.gatoFeliz}
      />
      <section className="seccion seccion--confeti">
        <div className="contenedor">
          <form className="buscador" onSubmit={buscar} role="search">
            <label htmlFor="buscar-noticias" className="sr-only">
              Buscar noticias
            </label>
            <input id="buscar-noticias" placeholder="Buscar noticias…" value={busqueda} onChange={(e) => setBusqueda(e.target.value)} />
            <button className="boton boton--chico">Buscar</button>
          </form>
          <div className="filtros" role="group" aria-label="Categorías">
            <button className="chip" aria-pressed={!categoria} onClick={() => actualizar({ categoria: '' })}>
              Todas
            </button>
            {categorias.datos?.map((c) => (
              <button key={c} className="chip" aria-pressed={categoria === c} onClick={() => actualizar({ categoria: c })}>
                {c}
              </button>
            ))}
          </div>
          <AvisoRelleno visible={deRelleno} />
          {cargando ? (
            <Cargando />
          ) : error ? (
            <ErrorCarga mensaje={error} />
          ) : datos?.contenido.length ? (
            <>
              <div className="grid grid--3">
                {datos.contenido.map((n, i) => (
                  <TarjetaNoticia key={n.id} noticia={n} indice={i} />
                ))}
              </div>
              {datos.totalPaginas > 1 && (
                <nav className="paginacion" aria-label="Paginación">
                  <button className="boton boton--blanco boton--chico" disabled={pagina === 0} onClick={() => actualizar({ pagina: String(pagina - 1) })}>
                    ← Anterior
                  </button>
                  <span>
                    Página {pagina + 1} de {datos.totalPaginas}
                  </span>
                  <button className="boton boton--blanco boton--chico" disabled={datos.ultima} onClick={() => actualizar({ pagina: String(pagina + 1) })}>
                    Siguiente →
                  </button>
                </nav>
              )}
            </>
          ) : (
            <Vacio texto="No encontramos noticias con esa búsqueda." />
          )}
        </div>
      </section>
    </>
  )
}
