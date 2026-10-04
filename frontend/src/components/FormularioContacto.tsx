import { useState, type FormEvent } from 'react'
import { api } from '../api/cliente'
import type { TipoMensaje } from '../api/tipos'
import { nombreTipoMensaje } from '../utils/formato'
import { AlertaError } from './Alerta'

interface Props {
  tipoInicial?: TipoMensaje
  mostrarOrganizacion?: boolean
}

export default function FormularioContacto({ tipoInicial = 'GENERAL', mostrarOrganizacion }: Props) {
  const vacio = { nombre: '', email: '', telefono: '', organizacion: '', tipo: tipoInicial, asunto: '', mensaje: '' }
  const [form, setForm] = useState(vacio)
  const [enviando, setEnviando] = useState(false)
  const [error, setError] = useState<unknown>(null)
  const [exito, setExito] = useState<string | null>(null)

  const campo = (nombre: keyof typeof form) => ({
    value: form[nombre],
    onChange: (e: { target: { value: string } }) => setForm((f) => ({ ...f, [nombre]: e.target.value })),
  })

  async function enviar(e: FormEvent) {
    e.preventDefault()
    setEnviando(true)
    setError(null)
    try {
      const r = await api<{ mensaje: string }>('/api/contacto', { method: 'POST', body: form })
      setExito(r.mensaje)
      setForm(vacio)
    } catch (err) {
      setError(err)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <form className="caja caja--borde formulario" onSubmit={enviar}>
      <div className="formulario__fila">
        <div className="campo">
          <label htmlFor="c-nombre">Nombre *</label>
          <input id="c-nombre" required maxLength={120} autoComplete="name" {...campo('nombre')} />
        </div>
        <div className="campo">
          <label htmlFor="c-email">Correo electrónico *</label>
          <input id="c-email" type="email" required autoComplete="email" {...campo('email')} />
        </div>
      </div>
      <div className="formulario__fila">
        <div className="campo">
          <label htmlFor="c-tel">Teléfono</label>
          <input id="c-tel" type="tel" maxLength={30} autoComplete="tel" {...campo('telefono')} />
        </div>
        {mostrarOrganizacion ? (
          <div className="campo">
            <label htmlFor="c-org">Empresa u organización</label>
            <input id="c-org" maxLength={150} autoComplete="organization" {...campo('organizacion')} />
          </div>
        ) : (
          <div className="campo">
            <label htmlFor="c-tipo">Motivo</label>
            <select id="c-tipo" {...campo('tipo')}>
              {Object.entries(nombreTipoMensaje).map(([k, v]) => (
                <option key={k} value={k}>
                  {v}
                </option>
              ))}
            </select>
          </div>
        )}
      </div>
      <div className="campo">
        <label htmlFor="c-asunto">Asunto *</label>
        <input id="c-asunto" required maxLength={150} {...campo('asunto')} />
      </div>
      <div className="campo">
        <label htmlFor="c-mensaje">Mensaje *</label>
        <textarea id="c-mensaje" required maxLength={3000} {...campo('mensaje')} />
      </div>
      <AlertaError error={error} />
      {exito && (
        <p className="alerta alerta--exito" role="status">
          {exito}
        </p>
      )}
      <button className="boton boton--rosa" disabled={enviando}>
        {enviando ? 'Enviando…' : 'Enviar mensaje'}
      </button>
    </form>
  )
}
