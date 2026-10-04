import { useParams } from 'react-router-dom'
import type { PaisDetalle as TipoPaisDetalle } from '../api/tipos'
import CabeceraPagina from '../components/CabeceraPagina'
import { Cargando, ErrorCarga, Vacio } from '../components/Estados'
import { TarjetaConvocatoria, TarjetaProyecto } from '../components/Tarjetas'
import TituloSeccion from '../components/TituloSeccion'
import { img } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'

export default function PaisDetalle() {
  const { codigo = '' } = useParams()
  const pais = relleno.paises.find((p) => p.codigo === codigo.toUpperCase())
  const { datos, cargando, error } = useApi<TipoPaisDetalle>(
    `/api/paises/${codigo}`,
    pais && {
      pais,
      proyectos: relleno.proyectos.filter((p) => p.pais.codigo === pais.codigo),
      convocatoriasAbiertas: relleno.convocatorias.filter((c) => c.pais?.codigo === pais.codigo),
    },
  )

  if (cargando) return <Cargando />
  if (error || !datos) return <ErrorCarga mensaje={error ?? 'No encontramos este país.'} />

  return (
    <>
      <CabeceraPagina etiqueta="Dónde estamos" titulo={datos.pais.nombre} bajada={datos.pais.descripcion} imagen={img.ninoSaluda} />
      <section className="seccion seccion--confeti">
        <div className="contenedor">
          <TituloSeccion etiqueta="Proyectos" titulo={`Proyectos en ${datos.pais.nombre}`} />
          {datos.proyectos.length ? (
            <div className="grid grid--3">
              {datos.proyectos.map((p, i) => (
                <TarjetaProyecto key={p.id} proyecto={p} indice={i} />
              ))}
            </div>
          ) : (
            <Vacio texto="Pronto tendremos proyectos aquí." />
          )}
        </div>
      </section>
      {!!datos.convocatoriasAbiertas.length && (
        <section className="seccion seccion--amarillo">
          <div className="contenedor">
            <TituloSeccion titulo={<span className="pildora">Convocatorias abiertas</span>} />
            <div className="grid grid--3">
              {datos.convocatoriasAbiertas.map((c) => (
                <TarjetaConvocatoria key={c.id} convocatoria={c} />
              ))}
            </div>
          </div>
        </section>
      )}
    </>
  )
}
