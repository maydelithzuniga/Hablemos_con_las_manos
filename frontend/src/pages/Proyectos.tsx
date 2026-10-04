import { useSearchParams } from 'react-router-dom'
import type { AreaTematica, Pagina, Pais, Proyecto } from '../api/tipos'
import CabeceraPagina from '../components/CabeceraPagina'
import { AvisoRelleno, Cargando, ErrorCarga, Vacio } from '../components/Estados'
import { TarjetaProyecto } from '../components/Tarjetas'
import { img } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'
import { nombreArea } from '../utils/formato'

export default function Proyectos() {
  const [params, setParams] = useSearchParams()
  const pais = params.get('pais') ?? ''
  const area = (params.get('area') ?? '') as AreaTematica | ''
  const pagina = Number(params.get('pagina') ?? 0)

  const paises = useApi<Pais[]>('/api/paises', relleno.paises)
  const consulta = new URLSearchParams({ pagina: String(pagina), tamanio: '9' })
  if (pais) consulta.set('pais', pais)
  if (area) consulta.set('area', area)
  const filtrados = relleno.proyectos.filter((p) => (!pais || p.pais.codigo === pais) && (!area || p.area === area))
  const { datos, cargando, error, deRelleno } = useApi<Pagina<Proyecto>>(`/api/proyectos?${consulta}`, {
    ...relleno.paginaProyectos,
    contenido: filtrados,
    totalElementos: filtrados.length,
  })

  const filtrar = (clave: string, valor: string) => {
    const nuevos = new URLSearchParams(params)
    if (valor) nuevos.set(clave, valor)
    else nuevos.delete(clave)
    nuevos.delete('pagina')
    setParams(nuevos)
  }

  return (
    <>
      <CabeceraPagina
        etiqueta="Qué hacemos"
        titulo="Nuestros proyectos"
        bajada="Trabajamos junto a organizaciones locales de personas sordas en educación, salud, familia e inclusión laboral."
        imagen={img.ninoGuino}
      />
      <section className="seccion seccion--confeti">
        <div className="contenedor">
          <div className="filtros filtros--selects">
            <div className="campo">
              <label htmlFor="filtro-pais">País</label>
              <select id="filtro-pais" value={pais} onChange={(e) => filtrar('pais', e.target.value)}>
                <option value="">Todos los países</option>
                {paises.datos?.map((p) => (
                  <option key={p.codigo} value={p.codigo}>
                    {p.nombre}
                  </option>
                ))}
              </select>
            </div>
            <div className="campo">
              <label htmlFor="filtro-area">Área</label>
              <select id="filtro-area" value={area} onChange={(e) => filtrar('area', e.target.value)}>
                <option value="">Todas las áreas</option>
                {Object.entries(nombreArea).map(([clave, nombre]) => (
                  <option key={clave} value={clave}>
                    {nombre}
                  </option>
                ))}
              </select>
            </div>
          </div>
          <AvisoRelleno visible={deRelleno} />
          {cargando ? (
            <Cargando />
          ) : error ? (
            <ErrorCarga mensaje={error} />
          ) : datos?.contenido.length ? (
            <>
              <div className="grid grid--3">
                {datos.contenido.map((p, i) => (
                  <TarjetaProyecto key={p.id} proyecto={p} indice={i} />
                ))}
              </div>
              {datos.totalPaginas > 1 && (
                <nav className="paginacion" aria-label="Paginación">
                  <button className="boton boton--blanco boton--chico" disabled={pagina === 0} onClick={() => filtrar('pagina', String(pagina - 1))}>
                    ← Anterior
                  </button>
                  <span>
                    Página {pagina + 1} de {datos.totalPaginas}
                  </span>
                  <button className="boton boton--blanco boton--chico" disabled={datos.ultima} onClick={() => filtrar('pagina', String(pagina + 1))}>
                    Siguiente →
                  </button>
                </nav>
              )}
            </>
          ) : (
            <Vacio texto="No encontramos proyectos con esos filtros." />
          )}
        </div>
      </section>
    </>
  )
}
