import { Link } from 'react-router-dom'
import { img } from '../data/imagenes'

/** Franja "Sumate" con las tres formas de participar (como en americasolidaria.org). */
export default function Sumate() {
  const opciones = [
    {
      titulo: 'Sé voluntari@',
      texto: 'Comparte tu tiempo y tu profesión con la comunidad sorda.',
      boton: 'Postula',
      ruta: '/voluntariado',
      imagen: img.ninaSaluda,
      clase: 'sumate__opcion--rosa',
    },
    {
      titulo: 'Dona',
      texto: 'Con tu aporte mensual llevamos la LSP a más escuelas y familias.',
      boton: 'Quiero donar',
      ruta: '/dona',
      imagen: img.gatoFeliz,
      clase: 'sumate__opcion--amarilla',
    },
    {
      titulo: 'Empresas',
      texto: 'Voluntariado corporativo y alianzas para espacios de trabajo inclusivos.',
      boton: 'Hablemos',
      ruta: '/empresas',
      imagen: img.ninoSaluda,
      clase: 'sumate__opcion--violeta',
    },
  ]
  return (
    <section className="seccion sumate ondas" aria-labelledby="titulo-sumate">
      <div className="contenedor">
        <h2 id="titulo-sumate" className="centro">
          <span className="pildora sumate__titulo">¡Súmate y rompe barreras!</span>
        </h2>
        <div className="grid grid--3 sumate__grid">
          {opciones.map((o) => (
            <article key={o.titulo} className={`sumate__opcion ${o.clase}`}>
              <img src={o.imagen} alt="" loading="lazy" />
              <h3>{o.titulo}</h3>
              <p>{o.texto}</p>
              <Link to={o.ruta} className="boton boton--blanco">
                {o.boton}
              </Link>
            </article>
          ))}
        </div>
      </div>
    </section>
  )
}
