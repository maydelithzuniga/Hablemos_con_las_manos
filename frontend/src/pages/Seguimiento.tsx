import { useState, type FormEvent } from 'react'
import { useSearchParams } from 'react-router-dom'
import { api } from '../api/cliente'
import type { EstadoPostulacion, Seguimiento as TipoSeguimiento } from '../api/tipos'
import { AlertaError } from '../components/Alerta'
import CabeceraPagina from '../components/CabeceraPagina'
import { img } from '../data/imagenes'
import { fecha, nombreEstadoPostulacion } from '../utils/formato'

const etapas: EstadoPostulacion[] = ['RECIBIDA', 'EN_REVISION', 'ENTREVISTA', 'ACEPTADA']

export default function Seguimiento() {
  const [params] = useSearchParams()
  const [codigo, setCodigo] = useState(params.get('codigo') ?? '')
  const [email, setEmail] = useState('')
  const [resultado, setResultado] = useState<TipoSeguimiento | null>(null)
  const [error, setError] = useState<unknown>(null)
  const [cargando, setCargando] = useState(false)

  async function consultar(e: FormEvent) {
    e.preventDefault()
    setCargando(true)
    setError(null)
    setResultado(null)
    try {
      setResultado(
        await api<TipoSeguimiento>(
          `/api/postulaciones/seguimiento/${encodeURIComponent(codigo.trim())}?email=${encodeURIComponent(email.trim())}`,
        ),
      )
    } catch (err) {
      setError(err)
    } finally {
      setCargando(false)
    }
  }

  const indice = resultado ? etapas.indexOf(resultado.estado) : -1
  const cerrada = resultado && (resultado.estado === 'RECHAZADA' || resultado.estado === 'RETIRADA')

  return (
    <>
      <CabeceraPagina
        color="celeste"
        etiqueta="Seguimiento"
        titulo="Estado de mi postulación"
        bajada="Ingresa el código que recibiste por correo y el email con el que postulaste."
        imagen={img.gatoCurioso}
      />
      <section className="seccion seccion--confeti">
        <div className="contenedor contenedor--angosto pila">
          <form className="caja formulario" onSubmit={consultar}>
            <div className="formulario__fila">
              <div className="campo">
                <label htmlFor="codigo">Código de seguimiento</label>
                <input id="codigo" required placeholder="HM-XXXXXXXX" value={codigo} onChange={(e) => setCodigo(e.target.value)} />
              </div>
              <div className="campo">
                <label htmlFor="email-seg">Correo electrónico</label>
                <input id="email-seg" type="email" required value={email} onChange={(e) => setEmail(e.target.value)} />
              </div>
            </div>
            <AlertaError error={error} />
            <button className="boton boton--rosa" disabled={cargando}>
              {cargando ? 'Consultando…' : 'Consultar'}
            </button>
          </form>

          {resultado && (
            <div className="caja caja--borde" aria-live="polite">
              <h2>Hola, {resultado.nombres} 👋</h2>
              <p>
                Postulaste a <strong>{resultado.postulacionA}</strong> el {fecha(resultado.fechaPostulacion)}.
              </p>
              {cerrada ? (
                <p className="alerta alerta--error">
                  Estado: <strong>{nombreEstadoPostulacion[resultado.estado]}</strong>. ¡Gracias por tu interés! Te invitamos a
                  postular a próximas convocatorias.
                </p>
              ) : (
                <ol className="etapas">
                  {etapas.map((e, i) => (
                    <li key={e} className={i < indice ? 'hecha' : i === indice ? 'actual' : ''}>
                      <span>{i + 1}</span>
                      {nombreEstadoPostulacion[e]}
                    </li>
                  ))}
                </ol>
              )}
              <p className="campo__ayuda">Última actualización: {fecha(resultado.ultimaActualizacion)}</p>
            </div>
          )}
        </div>
      </section>
    </>
  )
}
