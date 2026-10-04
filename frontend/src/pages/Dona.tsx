import { useState, type FormEvent } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { api } from '../api/cliente'
import type { DonacionCreada, Pagina, Proyecto, TipoDonacion } from '../api/tipos'
import { AlertaError } from '../components/Alerta'
import TituloSeccion from '../components/TituloSeccion'
import { img } from '../data/imagenes'
import * as relleno from '../data/relleno'
import { useApi } from '../hooks/useApi'

const montos: Record<TipoDonacion, number[]> = {
  MENSUAL: [20, 35, 50, 100],
  UNICA: [50, 100, 200, 500],
}

const queLogra = [
  { monto: 20, texto: 'materiales para un taller de LSP en una escuela' },
  { monto: 50, texto: 'un mes de clases de señas para una familia' },
  { monto: 100, texto: 'una jornada de intérprete en un centro de salud' },
]

export default function Dona() {
  const [params] = useSearchParams()
  const proyectos = useApi<Pagina<Proyecto>>('/api/proyectos?tamanio=50', relleno.paginaProyectos)
  const [tipo, setTipo] = useState<TipoDonacion>('MENSUAL')
  const [monto, setMonto] = useState<number | ''>(35)
  const [otro, setOtro] = useState(false)
  const [moneda, setMoneda] = useState('PEN')
  const [proyectoId, setProyectoId] = useState(params.get('proyecto') ?? '')
  const [datos, setDatos] = useState({ nombre: '', email: '', telefono: '', documentoIdentidad: '', mensaje: '', anonima: false, suscribirBoletin: true })
  const [enviando, setEnviando] = useState(false)
  const [error, setError] = useState<unknown>(null)

  const simbolo = moneda === 'USD' ? 'US$' : 'S/'

  async function donar(e: FormEvent) {
    e.preventDefault()
    setError(null)
    setEnviando(true)
    try {
      const r = await api<DonacionCreada>('/api/donaciones', {
        method: 'POST',
        body: { ...datos, tipo, monto, moneda, pais: 'Perú', proyectoId: proyectoId ? Number(proyectoId) : null },
      })
      window.location.href = r.urlPago
    } catch (err) {
      setError(err)
      setEnviando(false)
    }
  }

  const cambiarTipo = (t: TipoDonacion) => {
    setTipo(t)
    setOtro(false)
    setMonto(montos[t][1])
  }

  return (
    <section className="seccion dona">
      <div className="mancha mancha--amarilla" aria-hidden="true" />
      <div className="mancha mancha--rosa" aria-hidden="true" />
      <div className="contenedor dona__grid">
        <div className="dona__texto">
          <span className="pildora">Dona</span>
          <h1 className="display dona__titulo">Tu aporte rompe barreras</h1>
          <p>
            Con tu donación llevamos la Lengua de Señas Peruana a más escuelas, familias, centros de salud y espacios de
            trabajo. Hazte <strong>socio mensual</strong> y acompáñanos todo el año.
          </p>
          <ul className="dona__logros">
            {queLogra.map((q) => (
              <li key={q.monto}>
                <strong>S/ {q.monto}</strong> {q.texto}
              </li>
            ))}
          </ul>
          <img src={img.gatoFeliz} alt="" className="dona__gato flota" />
        </div>

        <form className="caja caja--borde formulario dona__form" onSubmit={donar}>
          <div className="pestanas" role="tablist" aria-label="Tipo de donación">
            {(['MENSUAL', 'UNICA'] as TipoDonacion[]).map((t) => (
              <button key={t} type="button" role="tab" aria-selected={tipo === t} onClick={() => cambiarTipo(t)}>
                {t === 'MENSUAL' ? 'Hazte socio mensual' : 'Donación única'}
              </button>
            ))}
          </div>

          <div className="campo">
            <span className="campo__titulo">Elige un monto</span>
            <div className="opciones">
              {montos[tipo].map((m) => (
                <label key={m} className="opcion">
                  <input type="radio" name="monto" checked={!otro && monto === m} onChange={() => { setOtro(false); setMonto(m) }} />
                  <span>{simbolo} {m}</span>
                </label>
              ))}
              <label className="opcion">
                <input type="radio" name="monto" checked={otro} onChange={() => { setOtro(true); setMonto('') }} />
                <span>Otro monto</span>
              </label>
            </div>
          </div>
          <div className="formulario__fila">
            {otro && (
              <div className="campo">
                <label htmlFor="monto-otro">Monto</label>
                <input id="monto-otro" type="number" min={1} step="0.01" required value={monto} onChange={(e) => setMonto(e.target.value ? Number(e.target.value) : '')} />
              </div>
            )}
            <div className="campo">
              <label htmlFor="moneda">Moneda</label>
              <select id="moneda" value={moneda} onChange={(e) => setMoneda(e.target.value)}>
                <option value="PEN">Soles (S/)</option>
                <option value="USD">Dólares (US$)</option>
              </select>
            </div>
            <div className="campo">
              <label htmlFor="proyecto">Destino</label>
              <select id="proyecto" value={proyectoId} onChange={(e) => setProyectoId(e.target.value)}>
                <option value="">Donde más se necesite</option>
                {proyectos.datos?.contenido.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.titulo}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="formulario__fila">
            <div className="campo">
              <label htmlFor="d-nombre">Nombre completo *</label>
              <input id="d-nombre" required maxLength={120} value={datos.nombre} onChange={(e) => setDatos({ ...datos, nombre: e.target.value })} autoComplete="name" />
            </div>
            <div className="campo">
              <label htmlFor="d-email">Correo electrónico *</label>
              <input id="d-email" type="email" required value={datos.email} onChange={(e) => setDatos({ ...datos, email: e.target.value })} autoComplete="email" />
            </div>
          </div>
          <div className="formulario__fila">
            <div className="campo">
              <label htmlFor="d-doc">DNI / RUC (para tu certificado)</label>
              <input id="d-doc" maxLength={30} value={datos.documentoIdentidad} onChange={(e) => setDatos({ ...datos, documentoIdentidad: e.target.value })} />
            </div>
            <div className="campo">
              <label htmlFor="d-tel">Teléfono</label>
              <input id="d-tel" type="tel" maxLength={30} value={datos.telefono} onChange={(e) => setDatos({ ...datos, telefono: e.target.value })} autoComplete="tel" />
            </div>
          </div>
          <div className="campo">
            <label htmlFor="d-msg">Mensaje (opcional)</label>
            <textarea id="d-msg" maxLength={500} rows={3} value={datos.mensaje} onChange={(e) => setDatos({ ...datos, mensaje: e.target.value })} />
          </div>
          <label className="check">
            <input type="checkbox" checked={datos.anonima} onChange={(e) => setDatos({ ...datos, anonima: e.target.checked })} />
            <span>Quiero que mi donación sea anónima</span>
          </label>
          <label className="check">
            <input type="checkbox" checked={datos.suscribirBoletin} onChange={(e) => setDatos({ ...datos, suscribirBoletin: e.target.checked })} />
            <span>Quiero recibir el boletín de novedades</span>
          </label>
          <AlertaError error={error} />
          <button className="boton boton--rosa boton--grande" disabled={enviando || !monto}>
            {enviando ? 'Procesando…' : `Donar ${monto ? `${simbolo} ${monto}` : ''}${tipo === 'MENSUAL' ? ' al mes' : ''}`}
          </button>
          <p className="campo__ayuda centro">🔒 Pago seguro. Te enviaremos un comprobante por correo.</p>
        </form>
      </div>

      <div className="contenedor">
        <TituloSeccion centro titulo="Otras formas de ayudar" />
        <div className="grid grid--3">
          <div className="caja">
            <h3>Transferencia bancaria</h3>
            <p>Banco Ejemplo · Cuenta corriente en soles N° 000-0000000-0-00 · CCI 000-000-000000000000-00 (dato de relleno).</p>
          </div>
          <div className="caja">
            <h3>Yape / Plin</h3>
            <p>Escanea nuestro QR o yapea al 999 999 999 (dato de relleno).</p>
          </div>
          <div className="caja">
            <h3>Empresas</h3>
            <p>Conoce nuestras alianzas de responsabilidad social y voluntariado corporativo.</p>
            <Link to="/empresas" className="enlace-flecha">
              Ver alianzas →
            </Link>
          </div>
        </div>
      </div>
    </section>
  )
}
