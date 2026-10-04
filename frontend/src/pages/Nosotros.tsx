import { Link } from 'react-router-dom'
import { urlArchivo } from '../api/cliente'
import type { MiembroEquipo } from '../api/tipos'
import CabeceraPagina from '../components/CabeceraPagina'
import { AvisoRelleno, Cargando } from '../components/Estados'
import TituloSeccion from '../components/TituloSeccion'
import { historiaLsp, personajes, proposito } from '../data/contenido'
import { img, personajeRelleno } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'

const areas = [
  { clave: 'EQUIPO', titulo: 'Equipo' },
  { clave: 'DIRECTORIO', titulo: 'Directorio' },
  { clave: 'CONSEJO_ASESOR', titulo: 'Consejo asesor' },
] as const

export default function Nosotros() {
  const { datos: equipo, cargando, deRelleno } = useApi<MiembroEquipo[]>('/api/equipo', relleno.equipo)

  return (
    <>
      <CabeceraPagina
        etiqueta="Quiénes somos"
        titulo="Hablemos con las Manos"
        bajada="Un voluntariado que nace para que la comunidad sorda pueda comunicarse, aprender y participar en su propia lengua."
        imagen={img.ninoGracias}
      />

      <section className="seccion seccion--confeti">
        <div className="mancha mancha--rosa" aria-hidden="true" />
        <div className="contenedor grid grid--2">
          <div className="pila">
            <TituloSeccion etiqueta="Nuestra historia" titulo="Todo empezó con un taller" />
            <p>
              Hablemos con las Manos nació del taller <strong>“Señas que rompen barreras”</strong>, un espacio para que
              niñas, niños y adultos oyentes descubran la Lengua de Señas Peruana y aprendan a relacionarse con respeto con
              la comunidad sorda.
            </p>
            <p>
              Hoy reunimos a voluntarias y voluntarios, intérpretes, docentes y familias que creen que la comunicación es un
              derecho. <em>(Texto de relleno: reemplázalo con la historia real de la organización.)</em>
            </p>
          </div>
          <img src={img.parque} alt="Leo, Michi y Sofi saludando en un parque" className="imagen-redonda" loading="lazy" />
        </div>
      </section>

      <section className="seccion seccion--amarillo">
        <div className="contenedor grid grid--3 proposito-cajas">
          <article className="caja caja--borde">
            <h3>Misión</h3>
            <p>{proposito.mision}</p>
          </article>
          <article className="caja caja--borde">
            <h3>Visión</h3>
            <p>{proposito.vision}</p>
          </article>
          <article className="caja caja--borde">
            <h3>Valores</h3>
            <ul className="lista-check">
              {proposito.valores.map((v) => (
                <li key={v}>{v}</li>
              ))}
            </ul>
          </article>
        </div>
      </section>

      <section className="seccion seccion--confeti">
        <div className="contenedor">
          <TituloSeccion centro etiqueta="La LSP en el Perú" titulo="Un camino que recién empieza" />
          <ol className="linea-tiempo">
            {historiaLsp.map((h) => (
              <li key={h.anio}>
                <span className="display linea-tiempo__anio">{h.anio}</span>
                <h3>{h.titulo}</h3>
                <p>{h.texto}</p>
              </li>
            ))}
          </ol>
        </div>
      </section>

      <section className="seccion seccion--crema">
        <div className="contenedor">
          <TituloSeccion
            centro
            etiqueta="Nuestros personajes"
            titulo="¡Conoce a la pandilla!"
            texto="Ellos nos acompañan en cada taller para enseñar la LSP de forma divertida."
          />
          <div className="grid grid--3">
            {personajes.map((p) => (
              <article key={p.nombre} className="personaje-tarjeta">
                <img src={p.imagen} alt="" loading="lazy" />
                <h3>{p.nombre}</h3>
                <p>{p.descripcion}</p>
              </article>
            ))}
          </div>
        </div>
      </section>

      <section className="seccion seccion--confeti" id="equipo">
        <div className="contenedor">
          <TituloSeccion centro etiqueta="Personas" titulo="Nuestro equipo" />
          <AvisoRelleno visible={deRelleno} />
          {cargando ? (
            <Cargando />
          ) : (
            areas.map((area) => {
              const miembros = equipo?.filter((m) => m.area === area.clave) ?? []
              if (!miembros.length) return null
              return (
                <div key={area.clave} className="equipo">
                  <h3 className="equipo__titulo">
                    <span className="pildora">{area.titulo}</span>
                  </h3>
                  <div className="grid grid--4">
                    {miembros.map((m, i) => (
                      <article key={m.id} className="miembro">
                        <img src={urlArchivo(m.fotoUrl) ?? personajeRelleno(i)} alt="" loading="lazy" />
                        <h4>{m.nombre}</h4>
                        <p>{m.cargo}</p>
                        {m.linkedinUrl && (
                          <a href={m.linkedinUrl} target="_blank" rel="noreferrer">
                            LinkedIn
                          </a>
                        )}
                      </article>
                    ))}
                  </div>
                </div>
              )
            })
          )}
          <p className="centro">
            <Link to="/transparencia" className="boton">
              Ver transparencia
            </Link>
          </p>
        </div>
      </section>
    </>
  )
}
