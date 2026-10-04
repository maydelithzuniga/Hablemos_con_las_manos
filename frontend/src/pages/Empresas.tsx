import type { Aliado } from '../api/tipos'
import Aliados from '../components/Aliados'
import CabeceraPagina from '../components/CabeceraPagina'
import { AvisoRelleno, Cargando } from '../components/Estados'
import FormularioContacto from '../components/FormularioContacto'
import TituloSeccion from '../components/TituloSeccion'
import { img } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'

const formas = [
  { titulo: 'Voluntariado corporativo', texto: 'Jornadas para que tus equipos aprendan LSP y participen en proyectos.', imagen: img.fotoTrabajo },
  { titulo: 'Talleres de inclusión', texto: 'Capacitamos a tu personal de atención para recibir a clientes sordos.', imagen: img.fotoAula },
  { titulo: 'Donaciones y patrocinio', texto: 'Financia un proyecto, un aula o la formación de intérpretes.', imagen: img.fotoInterprete },
]

export default function Empresas() {
  const { datos, cargando, deRelleno } = useApi<Aliado[]>('/api/aliados', relleno.aliados)
  return (
    <>
      <CabeceraPagina
        color="rosa"
        etiqueta="Empresas y alianzas"
        titulo="Empresas que se comunican con todos"
        bajada="Construyamos juntos espacios de trabajo y atención accesibles para la comunidad sorda."
        imagen={img.ninoSaluda}
      />
      <section className="seccion seccion--confeti">
        <div className="contenedor">
          <TituloSeccion centro etiqueta="¿Cómo sumarte?" titulo="Formas de alianza" />
          <div className="grid grid--3">
            {formas.map((f) => (
              <article key={f.titulo} className="tarjeta">
                <div className="tarjeta__imagen">
                  <img src={f.imagen} alt="" loading="lazy" />
                </div>
                <div className="tarjeta__cuerpo">
                  <h3>{f.titulo}</h3>
                  <p>{f.texto}</p>
                </div>
              </article>
            ))}
          </div>
        </div>
      </section>
      <section className="seccion seccion--crema seccion--aliados">
        <div className="contenedor">
          <TituloSeccion centro etiqueta="Ellos ya se sumaron" titulo="Nuestros aliados" />
          <AvisoRelleno visible={deRelleno} />
          {cargando ? <Cargando /> : <Aliados aliados={datos ?? []} />}
        </div>
      </section>
      <section className="seccion seccion--amarillo" id="formulario">
        <div className="contenedor contenedor--angosto">
          <TituloSeccion centro titulo={<span className="pildora">Conversemos</span>} />
          <FormularioContacto tipoInicial="EMPRESAS" mostrarOrganizacion />
        </div>
      </section>
    </>
  )
}
