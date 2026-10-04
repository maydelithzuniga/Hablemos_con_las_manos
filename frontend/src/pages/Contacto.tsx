import { useSearchParams } from 'react-router-dom'
import type { TipoMensaje } from '../api/tipos'
import CabeceraPagina from '../components/CabeceraPagina'
import FormularioContacto from '../components/FormularioContacto'
import { img } from '../data/imagenes'
import { sitio } from '../data/sitio'

export default function Contacto() {
  const [params] = useSearchParams()
  const tipo = (params.get('tipo') as TipoMensaje | null) ?? 'GENERAL'
  return (
    <>
      <CabeceraPagina
        color="celeste"
        etiqueta="Contacto"
        titulo="¡Hablemos!"
        bajada="Escríbenos para consultas, talleres, prensa o alianzas. Te respondemos pronto."
        imagen={img.ninaSaluda}
      />
      <section className="seccion seccion--confeti">
        <div className="mancha mancha--amarilla" aria-hidden="true" />
        <div className="contenedor contacto__grid">
          <FormularioContacto tipoInicial={tipo} />
          <aside className="contacto__datos">
            <div className="caja">
              <h2>Encuéntranos</h2>
              <dl>
                <dt>Correo</dt>
                <dd><a href={`mailto:${sitio.email}`}>{sitio.email}</a></dd>
                <dt>Teléfono</dt>
                <dd>{sitio.telefono}</dd>
                <dt>Dirección</dt>
                <dd>{sitio.direccion}</dd>
                <dt>Horario</dt>
                <dd>{sitio.horario}</dd>
              </dl>
            </div>
            <span className="globo contacto__globo">¡Te esperamos!</span>
            <img src={img.gatoSentado} alt="" className="contacto__gato" />
          </aside>
        </div>
      </section>
    </>
  )
}
