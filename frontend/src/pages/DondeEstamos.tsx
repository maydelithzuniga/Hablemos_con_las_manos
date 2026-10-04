import { Link } from 'react-router-dom'
import type { Pais } from '../api/tipos'
import CabeceraPagina from '../components/CabeceraPagina'
import { AvisoRelleno, Cargando } from '../components/Estados'
import { img } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'

/** Bandera como emoji a partir del codigo ISO del pais. */
function bandera(codigo: string) {
  return codigo.toUpperCase().replace(/./g, (c) => String.fromCodePoint(127397 + c.charCodeAt(0)))
}

export default function DondeEstamos() {
  const { datos, cargando, deRelleno } = useApi<Pais[]>('/api/paises', relleno.paises)
  return (
    <>
      <CabeceraPagina
        color="celeste"
        etiqueta="Dónde estamos"
        titulo="Presentes en América Latina"
        bajada="Cada país tiene su propia lengua de señas. Trabajamos junto a las comunidades sordas de cada territorio."
        imagen={img.ninaSaluda}
      />
      <section className="seccion seccion--confeti">
        <div className="mancha mancha--amarilla" aria-hidden="true" />
        <div className="contenedor">
          <AvisoRelleno visible={deRelleno} />
          {cargando ? (
            <Cargando />
          ) : (
            <div className="grid grid--4">
              {datos?.map((p, i) => (
                <Link key={p.codigo} to={`/donde-estamos/${p.codigo}`} className={`tarjeta pais pais--${i % 4}`}>
                  <span className="pais__bandera" aria-hidden="true">
                    {bandera(p.codigo)}
                  </span>
                  <h2>{p.nombre}</h2>
                  <p>{p.descripcion}</p>
                  <span className="enlace-flecha">Ver proyectos →</span>
                </Link>
              ))}
            </div>
          )}
        </div>
      </section>
    </>
  )
}
