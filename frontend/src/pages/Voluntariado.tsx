import { useState } from 'react'
import { Link } from 'react-router-dom'
import type { Convocatoria, Programa, TipoPrograma } from '../api/tipos'
import CabeceraPagina from '../components/CabeceraPagina'
import { AvisoRelleno, Cargando, Vacio } from '../components/Estados'
import { TarjetaConvocatoria, TarjetaPrograma } from '../components/Tarjetas'
import TituloSeccion from '../components/TituloSeccion'
import { img } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'
import { nombreTipoPrograma } from '../utils/formato'

const pasos = [
  { titulo: 'Elige', texto: 'Revisa los programas y las convocatorias abiertas.' },
  { titulo: 'Postula', texto: 'Completa el formulario y adjunta tu CV.' },
  { titulo: 'Conversemos', texto: 'Te contactamos para una entrevista.' },
  { titulo: '¡A señar!', texto: 'Recibes formación y comienzas tu voluntariado.' },
]

export default function Voluntariado() {
  const [tipo, setTipo] = useState<TipoPrograma | null>(null)
  const programas = useApi<Programa[]>(tipo ? `/api/programas?tipo=${tipo}` : '/api/programas',
    tipo ? relleno.programas.filter((p) => p.tipo === tipo) : relleno.programas)
  const convocatorias = useApi<Convocatoria[]>('/api/convocatorias', relleno.convocatorias)

  return (
    <>
      <CabeceraPagina
        etiqueta="Voluntariado"
        titulo="Suma tus manos a la inclusión"
        bajada="No necesitas saber lengua de señas para empezar: te formamos y te acompañamos en todo el proceso."
        imagen={img.ninaSaluda}
      >
        <div className="cabecera-pagina__acciones">
          <Link to="/postula" className="boton boton--rosa boton--grande">
            Postula ahora
          </Link>
          <Link to="/seguimiento" className="boton boton--blanco">
            Estado de mi postulación
          </Link>
        </div>
      </CabeceraPagina>

      <section className="seccion seccion--confeti">
        <div className="contenedor">
          <TituloSeccion centro etiqueta="¿Cómo funciona?" titulo="Tu voluntariado en 4 pasos" />
          <ol className="pasos">
            {pasos.map((p, i) => (
              <li key={p.titulo}>
                <span className="pasos__numero">{i + 1}</span>
                <h3>{p.titulo}</h3>
                <p>{p.texto}</p>
              </li>
            ))}
          </ol>
        </div>
      </section>

      <section className="seccion seccion--crema">
        <div className="contenedor">
          <TituloSeccion etiqueta="Programas" titulo="Encuentra tu programa" />
          <div className="filtros" role="group" aria-label="Filtrar por tipo de programa">
            <button className="chip" aria-pressed={tipo === null} onClick={() => setTipo(null)}>
              Todos
            </button>
            {(['PROFESIONAL', 'JUVENIL', 'CORPORATIVO', 'VIRTUAL'] as TipoPrograma[]).map((t) => (
              <button key={t} className="chip" aria-pressed={tipo === t} onClick={() => setTipo(t)}>
                {nombreTipoPrograma[t]}
              </button>
            ))}
          </div>
          <AvisoRelleno visible={programas.deRelleno} />
          {programas.cargando ? (
            <Cargando />
          ) : programas.datos?.length ? (
            <div className="grid grid--3">
              {programas.datos.map((p, i) => (
                <TarjetaPrograma key={p.id} programa={p} indice={i} />
              ))}
            </div>
          ) : (
            <Vacio texto="No hay programas de este tipo por ahora." />
          )}
        </div>
      </section>

      <section className="seccion seccion--amarillo" id="convocatorias">
        <div className="contenedor">
          <TituloSeccion etiqueta="¡Postula!" titulo={<span className="pildora">Convocatorias abiertas</span>} />
          {convocatorias.cargando ? (
            <Cargando />
          ) : convocatorias.datos?.length ? (
            <div className="grid grid--3">
              {convocatorias.datos.map((c) => (
                <TarjetaConvocatoria key={c.id} convocatoria={c} />
              ))}
            </div>
          ) : (
            <div className="caja centro">
              <p>No hay convocatorias abiertas en este momento, pero puedes dejarnos tu postulación general.</p>
              <Link to="/postula" className="boton boton--rosa">
                Postulación general
              </Link>
            </div>
          )}
        </div>
      </section>
    </>
  )
}
