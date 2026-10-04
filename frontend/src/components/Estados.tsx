import { img } from '../data/imagenes'

export function Cargando({ texto = 'Cargando…' }: { texto?: string }) {
  return (
    <div className="cargando" role="status">
      {texto}
    </div>
  )
}

export function ErrorCarga({ mensaje }: { mensaje: string }) {
  return (
    <div className="vacio" role="alert">
      <img src={img.gatoCurioso} alt="" width={160} height={160} />
      <p>
        <strong>¿Miau?</strong> {mensaje}
      </p>
    </div>
  )
}

export function Vacio({ texto }: { texto: string }) {
  return (
    <div className="vacio">
      <img src={img.gatoSentado} alt="" width={160} height={160} />
      <p>{texto}</p>
    </div>
  )
}

/** Aviso discreto cuando se muestra contenido de ejemplo porque la API no esta encendida. */
export function AvisoRelleno({ visible }: { visible: boolean }) {
  if (!visible) return null
  return (
    <p className="aviso-relleno" role="note">
      Estás viendo contenido de ejemplo: enciende la API para ver los datos reales.
    </p>
  )
}
