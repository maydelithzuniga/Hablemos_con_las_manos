import { urlArchivo } from '../api/cliente'
import type { DocumentoTransparencia } from '../api/tipos'
import CabeceraPagina from '../components/CabeceraPagina'
import { AvisoRelleno, Cargando, Vacio } from '../components/Estados'
import { img } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'
import { nombreTipoDocumento } from '../utils/formato'

export default function Transparencia() {
  const { datos, cargando, deRelleno } = useApi<DocumentoTransparencia[]>('/api/transparencia', relleno.transparencia)
  const anios = [...new Set(datos?.map((d) => d.anio))].sort((a, b) => b - a)

  return (
    <>
      <CabeceraPagina
        etiqueta="Transparencia"
        titulo="Cuentas claras"
        bajada="Publicamos nuestras memorias, estados financieros y políticas para que sepas cómo usamos cada aporte."
        imagen={img.ninoGuino}
      />
      <section className="seccion seccion--confeti">
        <div className="contenedor contenedor--angosto">
          <AvisoRelleno visible={deRelleno} />
          {cargando ? (
            <Cargando />
          ) : !datos?.length ? (
            <Vacio texto="Pronto publicaremos nuestros documentos." />
          ) : (
            anios.map((anio) => (
              <div key={anio} className="documentos">
                <h2 className="display">{anio}</h2>
                <ul>
                  {datos
                    .filter((d) => d.anio === anio)
                    .map((d) => (
                      <li key={d.id} className="documento">
                        <span className="documento__icono" aria-hidden="true">PDF</span>
                        <div>
                          <h3>{d.titulo}</h3>
                          <p>
                            {nombreTipoDocumento[d.tipo]}
                            {d.descripcion ? ` · ${d.descripcion}` : ''}
                          </p>
                        </div>
                        <a href={urlArchivo(d.archivoUrl)} target="_blank" rel="noreferrer" className="boton boton--chico">
                          Descargar
                        </a>
                      </li>
                    ))}
                </ul>
              </div>
            ))
          )}
        </div>
      </section>
    </>
  )
}
