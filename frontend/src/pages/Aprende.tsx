import { Link } from 'react-router-dom'
import CabeceraPagina from '../components/CabeceraPagina'
import TituloSeccion from '../components/TituloSeccion'
import { barreras, consejos, datosProblema, historiaLsp, terminos } from '../data/contenido'
import { img } from '../data/imagenes'

export default function Aprende() {
  return (
    <>
      <CabeceraPagina
        color="celeste"
        etiqueta="Aprende LSP"
        titulo="¿Cómo interactuamos con una persona sorda?"
        bajada="Pequeños gestos que hacen una gran diferencia. Lo aprendimos en el taller “Señas que rompen barreras”."
        imagen={img.ninaSenia}
      />

      <section className="seccion seccion--confeti">
        <div className="mancha mancha--rosa-arriba" aria-hidden="true" />
        <div className="contenedor grid grid--2">
          <img src={img.gatoSentado} alt="" className="aprende__gato" loading="lazy" />
          <div>
            <h2 className="titulo-consejos">Consejos</h2>
            <p className="aprende__intro">Usar los términos correctos, ya que todos merecemos ser tratados con respeto.</p>
            <ul className="terminos">
              {terminos.map((t) => (
                <li key={t.incorrecto}>
                  <del>{t.incorrecto}</del>
                  <span aria-hidden="true">→</span>
                  <strong>{t.correcto}</strong>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </section>

      <section className="seccion seccion--crema">
        <div className="banderines" aria-hidden="true" />
        <div className="contenedor">
          <div className="grid grid--3">
            {consejos.map((c, i) => (
              <article key={c.titulo} className="tarjeta consejo">
                <div className="tarjeta__imagen">
                  <img src={c.imagen} alt="" loading="lazy" />
                </div>
                <div className="tarjeta__cuerpo">
                  <span className="consejo__numero display">{i + 1}</span>
                  <h3>{c.titulo}</h3>
                  <p>{c.texto}</p>
                </div>
              </article>
            ))}
          </div>
        </div>
      </section>

      <section className="seccion seccion--confeti">
        <div className="contenedor">
          <TituloSeccion
            centro
            etiqueta="La realidad"
            titulo="Barreras para acceder a servicios"
            texto="Escasean los profesionales capacitados en LSP y los intérpretes. Estas son algunas de las barreras más frecuentes."
          />
          <div className="grid grid--4">
            {barreras.map((b) => (
              <article key={b.titulo} className="tarjeta">
                <div className="tarjeta__imagen">
                  <img src={b.imagen} alt="" loading="lazy" />
                </div>
                <div className="tarjeta__cuerpo">
                  <h3>{b.titulo}</h3>
                  <p>{b.texto}</p>
                </div>
              </article>
            ))}
          </div>
        </div>
      </section>

      <section className="seccion seccion--rosa">
        <div className="contenedor grid grid--4 datos">
          {datosProblema.map((d) => (
            <article key={d.cifra} className="dato dato--blanco">
              <span className="display dato__cifra">{d.cifra}</span>
              <p>{d.texto}</p>
            </article>
          ))}
        </div>
      </section>

      <section className="seccion seccion--confeti">
        <div className="contenedor">
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

      <section className="seccion ondas rompecabezas">
        <div className="contenedor rompecabezas__fila">
          <img src={img.gatoCurioso} alt="" />
          <div className="caja caja--borde">
            <h2 className="display display--rojo">¡Rómpete la cabeza!</h2>
            <p>
              ¿Quieres llevar el taller “Señas que rompen barreras” a tu colegio, empresa o comunidad? Armamos grupos,
              jugamos y aprendemos juntos.
            </p>
            <Link to="/contacto?tipo=GENERAL" className="boton boton--rosa">
              Solicitar un taller
            </Link>
          </div>
        </div>
      </section>
    </>
  )
}
