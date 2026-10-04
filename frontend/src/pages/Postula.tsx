import { useState, type ChangeEvent, type FormEvent } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { api } from '../api/cliente'
import type { Archivo, Convocatoria, Programa } from '../api/tipos'
import { AlertaError } from '../components/Alerta'
import CabeceraPagina from '../components/CabeceraPagina'
import { img } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'

const inicial = {
  nombres: '',
  apellidos: '',
  email: '',
  telefono: '',
  documentoIdentidad: '',
  fechaNacimiento: '',
  paisResidencia: 'Perú',
  ciudad: '',
  profesion: '',
  nivelLenguaSenas: 'NINGUNO',
  experiencia: '',
  motivacion: '',
  disponibilidad: '',
  aceptaPoliticaDatos: false,
}

export default function Postula() {
  const [params] = useSearchParams()
  const convocatorias = useApi<Convocatoria[]>('/api/convocatorias', relleno.convocatorias)
  const programas = useApi<Programa[]>('/api/programas', relleno.programas)
  const [destino, setDestino] = useState(() =>
    params.get('convocatoria') ? `c-${params.get('convocatoria')}` : params.get('programa') ? `p-${params.get('programa')}` : '',
  )
  const [form, setForm] = useState(inicial)
  const [cv, setCv] = useState<File | null>(null)
  const [enviando, setEnviando] = useState(false)
  const [error, setError] = useState<unknown>(null)
  const [codigo, setCodigo] = useState<string | null>(null)

  const cambiar = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value, type } = e.target
    setForm((f) => ({ ...f, [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value }))
  }

  async function enviar(e: FormEvent) {
    e.preventDefault()
    setError(null)
    if (cv && cv.size > 5 * 1024 * 1024) {
      setError(new Error('El CV no puede pesar más de 5 MB'))
      return
    }
    setEnviando(true)
    try {
      let cvUrl: string | undefined
      if (cv) {
        const datos = new FormData()
        datos.append('archivo', cv)
        cvUrl = (await api<Archivo>('/api/archivos/cv', { method: 'POST', body: datos })).url
      }
      const [tipo, id] = destino.split('-')
      const r = await api<{ codigo: string }>('/api/postulaciones', {
        method: 'POST',
        body: {
          ...form,
          fechaNacimiento: form.fechaNacimiento || null,
          convocatoriaId: tipo === 'c' ? Number(id) : null,
          programaId: tipo === 'p' ? Number(id) : null,
          cvUrl,
        },
      })
      setCodigo(r.codigo)
      window.scrollTo({ top: 0, behavior: 'smooth' })
    } catch (err) {
      setError(err)
    } finally {
      setEnviando(false)
    }
  }

  if (codigo) {
    return (
      <section className="seccion seccion--confeti exito">
        <div className="contenedor exito__fila">
          <img src={img.ninoGracias} alt="" />
          <div className="caja caja--borde">
            <h1 className="display">¡Gracias!</h1>
            <p>
              Recibimos tu postulación, <strong>{form.nombres}</strong>. Te enviamos un correo de confirmación.
            </p>
            <p>Guarda tu código de seguimiento:</p>
            <p className="exito__codigo">{codigo}</p>
            <div className="diapositiva__acciones">
              <Link to={`/seguimiento?codigo=${codigo}`} className="boton boton--rosa">
                Ver estado
              </Link>
              <Link to="/" className="boton boton--blanco">
                Volver al inicio
              </Link>
            </div>
          </div>
        </div>
      </section>
    )
  }

  return (
    <>
      <CabeceraPagina
        etiqueta="Postula"
        titulo="Quiero ser voluntari@"
        bajada="Completa el formulario. Te tomará unos 10 minutos."
        imagen={img.ninoSaluda}
      />
      <section className="seccion seccion--confeti">
        <div className="contenedor contenedor--angosto">
          <form className="caja formulario" onSubmit={enviar}>
            <fieldset className="formulario__grupo">
              <legend>¿A qué postulas?</legend>
              <div className="campo">
                <label htmlFor="destino">Convocatoria o programa</label>
                <select id="destino" value={destino} onChange={(e) => setDestino(e.target.value)}>
                  <option value="">Postulación general</option>
                  {!!convocatorias.datos?.length && (
                    <optgroup label="Convocatorias abiertas">
                      {convocatorias.datos.map((c) => (
                        <option key={c.id} value={`c-${c.id}`}>
                          {c.titulo}
                        </option>
                      ))}
                    </optgroup>
                  )}
                  {!!programas.datos?.length && (
                    <optgroup label="Programas">
                      {programas.datos.map((p) => (
                        <option key={p.id} value={`p-${p.id}`}>
                          {p.titulo}
                        </option>
                      ))}
                    </optgroup>
                  )}
                </select>
              </div>
            </fieldset>

            <fieldset className="formulario__grupo">
              <legend>Tus datos</legend>
              <div className="formulario__fila">
                <div className="campo">
                  <label htmlFor="nombres">Nombres *</label>
                  <input id="nombres" name="nombres" required maxLength={80} value={form.nombres} onChange={cambiar} autoComplete="given-name" />
                </div>
                <div className="campo">
                  <label htmlFor="apellidos">Apellidos *</label>
                  <input id="apellidos" name="apellidos" required maxLength={80} value={form.apellidos} onChange={cambiar} autoComplete="family-name" />
                </div>
              </div>
              <div className="formulario__fila">
                <div className="campo">
                  <label htmlFor="email">Correo electrónico *</label>
                  <input id="email" name="email" type="email" required value={form.email} onChange={cambiar} autoComplete="email" />
                </div>
                <div className="campo">
                  <label htmlFor="telefono">Teléfono / WhatsApp</label>
                  <input id="telefono" name="telefono" type="tel" maxLength={30} value={form.telefono} onChange={cambiar} autoComplete="tel" />
                </div>
              </div>
              <div className="formulario__fila">
                <div className="campo">
                  <label htmlFor="documentoIdentidad">DNI / Documento</label>
                  <input id="documentoIdentidad" name="documentoIdentidad" maxLength={30} value={form.documentoIdentidad} onChange={cambiar} />
                </div>
                <div className="campo">
                  <label htmlFor="fechaNacimiento">Fecha de nacimiento</label>
                  <input id="fechaNacimiento" name="fechaNacimiento" type="date" value={form.fechaNacimiento} onChange={cambiar} />
                </div>
              </div>
              <div className="formulario__fila">
                <div className="campo">
                  <label htmlFor="paisResidencia">País de residencia</label>
                  <input id="paisResidencia" name="paisResidencia" maxLength={80} value={form.paisResidencia} onChange={cambiar} />
                </div>
                <div className="campo">
                  <label htmlFor="ciudad">Ciudad</label>
                  <input id="ciudad" name="ciudad" maxLength={80} value={form.ciudad} onChange={cambiar} />
                </div>
              </div>
            </fieldset>

            <fieldset className="formulario__grupo">
              <legend>Tu perfil</legend>
              <div className="formulario__fila">
                <div className="campo">
                  <label htmlFor="profesion">Profesión u ocupación</label>
                  <input id="profesion" name="profesion" maxLength={120} value={form.profesion} onChange={cambiar} />
                </div>
                <div className="campo">
                  <label htmlFor="disponibilidad">Disponibilidad</label>
                  <input id="disponibilidad" name="disponibilidad" maxLength={300} placeholder="Ej. fines de semana, tiempo completo…" value={form.disponibilidad} onChange={cambiar} />
                </div>
              </div>
              <div className="campo">
                <span className="campo__titulo">¿Cuánta lengua de señas sabes?</span>
                <div className="opciones" role="radiogroup">
                  {[
                    ['NINGUNO', 'Nada aún'],
                    ['BASICO', 'Básico'],
                    ['INTERMEDIO', 'Intermedio'],
                    ['AVANZADO', 'Avanzado'],
                  ].map(([valor, texto]) => (
                    <label key={valor} className="opcion">
                      <input type="radio" name="nivelLenguaSenas" value={valor} checked={form.nivelLenguaSenas === valor} onChange={cambiar} />
                      <span>{texto}</span>
                    </label>
                  ))}
                </div>
              </div>
              <div className="campo">
                <label htmlFor="experiencia">Experiencia en voluntariado o con la comunidad sorda</label>
                <textarea id="experiencia" name="experiencia" maxLength={2000} value={form.experiencia} onChange={cambiar} />
              </div>
              <div className="campo">
                <label htmlFor="motivacion">¿Por qué quieres ser voluntari@? *</label>
                <textarea id="motivacion" name="motivacion" required maxLength={2000} value={form.motivacion} onChange={cambiar} />
              </div>
              <div className="campo">
                <label htmlFor="cv">CV (PDF, máximo 5 MB)</label>
                <input id="cv" type="file" accept="application/pdf" onChange={(e) => setCv(e.target.files?.[0] ?? null)} />
                <span className="campo__ayuda">Opcional, pero nos ayuda a conocerte mejor.</span>
              </div>
            </fieldset>

            <label className="check">
              <input type="checkbox" name="aceptaPoliticaDatos" checked={form.aceptaPoliticaDatos} onChange={cambiar} required />
              <span>
                Acepto la <Link to="/transparencia">política de tratamiento de datos personales</Link> *
              </span>
            </label>

            <AlertaError error={error} />
            <button className="boton boton--rosa boton--grande" disabled={enviando}>
              {enviando ? 'Enviando…' : 'Enviar postulación'}
            </button>
          </form>
        </div>
      </section>
    </>
  )
}
