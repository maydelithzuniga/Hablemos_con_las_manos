import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import type { Inicio as TipoInicio } from '../api/tipos'
import Aliados from '../components/Aliados'
import { AvisoRelleno, Cargando } from '../components/Estados'
import Impacto from '../components/Impacto'
import Parque from '../components/Parque'
import Sumate from '../components/Sumate'
import { TarjetaConvocatoria, TarjetaNoticia, TarjetaPrograma, TarjetaProyecto } from '../components/Tarjetas'
import Testimonios from '../components/Testimonios'
import TituloSeccion from '../components/TituloSeccion'
import { datosProblema, proposito } from '../data/contenido'
import { img } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'

const DURACION_DIAPOSITIVA = 7000

function Carrusel({ convocatoria }: { convocatoria?: string }) {
  const [actual, setActual] = useState(0)
  const [pausado, setPausado] = useState(false)
  const total = 3

  useEffect(() => {
    if (pausado) return
    const id = setInterval(() => setActual((a) => (a + 1) % total), DURACION_DIAPOSITIVA)
    return () => clearInterval(id)
  }, [pausado])

  return (
    <section
      className="carrusel"
      aria-roledescription="carrusel"
      aria-label="Destacados"
      onMouseEnter={() => setPausado(true)}
      onMouseLeave={() => setPausado(false)}
      onFocus={() => setPausado(true)}
      onBlur={() => setPausado(false)}
    >
      {/* 1. Bienvenida en el parque */}
      <div className={`diapositiva diapositiva--parque ${actual === 0 ? 'diapositiva--activa' : ''}`} aria-hidden={actual !== 0}>
        <Parque>
          <div className="contenedor diapositiva__parque">
            <div className="diapositiva__texto">
              <p className="diapositiva__saludo">Bienvenidos a</p>
              <h1 className="titulo-contorno">Señas que rompen barreras</h1>
              <p className="diapositiva__bajada">
                Somos <strong>Hablemos con las Manos</strong>: un voluntariado que acerca la Lengua de Señas Peruana a
                escuelas, familias y comunidades.
              </p>
              <div className="diapositiva__acciones">
                <Link to="/voluntariado" className="boton boton--grande" tabIndex={actual === 0 ? 0 : -1}>
                  Quiero ser voluntari@
                </Link>
                <Link to="/dona" className="boton boton--rosa boton--grande" tabIndex={actual === 0 ? 0 : -1}>
                  Dona
                </Link>
              </div>
            </div>
            <div className="diapositiva__personajes" aria-hidden="true">
              <span className="globo globo--izq diapositiva__globo">¡Gusto en conocerl@s!!</span>
              <img src={img.ninoSaluda} alt="" className="personaje personaje--nino" />
              <img src={img.gato} alt="" className="personaje personaje--gato" />
              <img src={img.ninaSaluda} alt="" className="personaje personaje--nina" />
            </div>
          </div>
        </Parque>
      </div>

      {/* 2. Convocatoria abierta (estilo "SESION 2") */}
      <div className={`diapositiva diapositiva--amarilla ${actual === 1 ? 'diapositiva--activa' : ''}`} aria-hidden={actual !== 1}>
        <div className="contenedor diapositiva__fila">
          <img src={img.ninaFeliz} alt="" className="diapositiva__personaje-grande" />
          <div className="diapositiva__pildoras">
            <span className="pildora">Convocatoria abierta:</span>
            <span className="pildora pildora--grande">{convocatoria ?? 'Voluntariado Profesional'}</span>
            <span className="pildora">¡Postula hoy!</span>
            <Link to="/voluntariado#convocatorias" className="boton boton--rosa boton--grande" tabIndex={actual === 1 ? 0 : -1}>
              Ver convocatorias
            </Link>
          </div>
        </div>
      </div>

      {/* 3. Dato de la comunidad sorda */}
      <div className={`diapositiva diapositiva--rosa ${actual === 2 ? 'diapositiva--activa' : ''}`} aria-hidden={actual !== 2}>
        <div className="contenedor diapositiva__fila">
          <img src={img.ninaSenia} alt="" className="diapositiva__personaje-grande" />
          <div className="diapositiva__nube">
            <p>
              Según la Federación Mundial de Sordos, solo el <strong className="destacado">0,1%</strong> de la población
              peruana oyente sabe lengua de señas peruana, a pesar de existir <strong className="destacado">532 mil</strong>{' '}
              personas con discapacidad auditiva.
            </p>
            <Link to="/aprende" className="boton boton--grande" tabIndex={actual === 2 ? 0 : -1}>
              Aprende a comunicarte
            </Link>
          </div>
        </div>
      </div>

      <div className="carrusel__puntos">
        {Array.from({ length: total }).map((_, i) => (
          <button
            key={i}
            className={i === actual ? 'activo' : ''}
            aria-label={`Ver diapositiva ${i + 1}`}
            aria-current={i === actual}
            onClick={() => setActual(i)}
          />
        ))}
      </div>
    </section>
  )
}

export default function Inicio() {
  const { datos, cargando, deRelleno } = useApi<TipoInicio>('/api/inicio', relleno.inicio)

  return (
    <>
      <Carrusel convocatoria={datos?.convocatoriasAbiertas[0]?.titulo} />
      <AvisoRelleno visible={deRelleno} />

      {/* Proposito */}
      <section className="seccion seccion--confeti">
        <div className="mancha mancha--amarilla" aria-hidden="true" />
        <div className="contenedor grid grid--2">
          <div className="proposito__imagen">
            <img src={img.fotoAula} alt="Una docente enseña señas a un grupo de estudiantes" loading="lazy" />
            <img src={img.ninoGuino} alt="" className="proposito__personaje" />
          </div>
          <div>
            <span className="etiqueta">Nuestro propósito</span>
            <h2>
              Persisten muchas <span className="destacado">barreras</span> que la comunidad sorda enfrenta cada día
            </h2>
            <p>{proposito.mision}</p>
            <p>
              Lo hacemos con voluntarias y voluntarios que enseñan, interpretan y acompañan, junto a organizaciones de
              personas sordas en cada territorio.
            </p>
            <Link to="/nosotros" className="boton">
              Conócenos
            </Link>
          </div>
        </div>
      </section>

      {/* Impacto */}
      <section className="seccion seccion--rosa seccion--impacto" aria-labelledby="titulo-impacto">
        <div className="contenedor">
          <h2 id="titulo-impacto" className="centro display display--blanco titulo-impacto">
            Nuestro impacto
          </h2>
          {cargando ? <Cargando /> : datos && <Impacto impacto={datos.impacto} />}
        </div>
      </section>

      {/* Programas */}
      <section className="seccion seccion--confeti">
        <div className="mancha mancha--rosa" aria-hidden="true" />
        <div className="contenedor">
          <TituloSeccion
            etiqueta="Voluntariado"
            titulo="Programas para sumar tus manos"
            texto="Elige cómo quieres participar: como profesional, joven o junto a tu empresa."
            accion={
              <Link to="/voluntariado" className="enlace-flecha">
                Ver todos los programas →
              </Link>
            }
          />
          {cargando ? (
            <Cargando />
          ) : (
            <div className="grid grid--3">
              {datos?.programasDestacados.map((p, i) => <TarjetaPrograma key={p.id} programa={p} indice={i} />)}
            </div>
          )}
        </div>
      </section>

      {/* Convocatorias */}
      {!!datos?.convocatoriasAbiertas.length && (
        <section className="seccion seccion--amarillo">
          <div className="contenedor">
            <TituloSeccion
              etiqueta="¡Postula!"
              titulo={<span className="pildora">Convocatorias abiertas</span>}
              accion={
                <Link to="/voluntariado#convocatorias" className="boton boton--blanco">
                  Ver todas
                </Link>
              }
            />
            <div className="grid grid--3">
              {datos.convocatoriasAbiertas.slice(0, 3).map((c) => (
                <TarjetaConvocatoria key={c.id} convocatoria={c} />
              ))}
            </div>
          </div>
        </section>
      )}

      {/* Datos del problema */}
      <section className="seccion seccion--confeti" aria-labelledby="titulo-datos">
        <div className="mancha mancha--amarilla" aria-hidden="true" />
        <div className="mancha mancha--rosa" aria-hidden="true" />
        <div className="contenedor">
          <TituloSeccion
            centro
            id="titulo-datos"
            etiqueta="¿Por qué existimos?"
            titulo="Problemas que vive la comunidad sorda en el país"
          />
          <div className="grid grid--4 datos">
            {datosProblema.map((d) => (
              <article key={d.cifra} className={`dato dato--${d.color}`}>
                <span className="display dato__cifra">{d.cifra}</span>
                <p>{d.texto}</p>
              </article>
            ))}
          </div>
          <p className="centro">
            <Link to="/aprende" className="boton boton--rosa">
              ¿Cómo interactuamos con una persona sorda?
            </Link>
          </p>
        </div>
      </section>

      {/* Proyectos */}
      <section className="seccion seccion--crema">
        <div className="contenedor">
          <TituloSeccion
            etiqueta="Qué hacemos"
            titulo="Proyectos que rompen barreras"
            accion={
              <Link to="/proyectos" className="enlace-flecha">
                Ver todos los proyectos →
              </Link>
            }
          />
          {cargando ? (
            <Cargando />
          ) : (
            <div className="grid grid--3">
              {datos?.proyectosDestacados.slice(0, 3).map((p, i) => <TarjetaProyecto key={p.id} proyecto={p} indice={i} />)}
            </div>
          )}
        </div>
      </section>

      {/* Testimonios */}
      {!!datos?.testimonios.length && (
        <section className="seccion seccion--testimonios" aria-labelledby="titulo-testimonios">
          <div className="banderines" aria-hidden="true" />
          <div className="contenedor">
            <TituloSeccion centro id="titulo-testimonios" etiqueta="Testimonios" titulo="Historias que nos inspiran" />
            <Testimonios testimonios={datos.testimonios} />
          </div>
        </section>
      )}

      <Sumate />

      {/* Noticias */}
      <section className="seccion seccion--confeti">
        <div className="contenedor">
          <TituloSeccion
            etiqueta="Noticias"
            titulo="Lo último de nuestra comunidad"
            accion={
              <Link to="/noticias" className="enlace-flecha">
                Ver todas las noticias →
              </Link>
            }
          />
          {cargando ? (
            <Cargando />
          ) : (
            <div className="grid grid--3">
              {datos?.ultimasNoticias.map((n, i) => <TarjetaNoticia key={n.id} noticia={n} indice={i} />)}
            </div>
          )}
        </div>
      </section>

      {/* Aliados */}
      {!!datos?.aliados.length && (
        <section className="seccion seccion--crema seccion--aliados">
          <div className="contenedor">
            <TituloSeccion centro etiqueta="Alianzas" titulo="Gracias a quienes nos acompañan" />
            <Aliados aliados={datos.aliados} />
          </div>
        </section>
      )}
    </>
  )
}
