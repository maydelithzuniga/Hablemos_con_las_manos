import { ApiError } from '../api/cliente'

/** Muestra el error de la API, incluyendo los detalles de validacion. */
export function AlertaError({ error }: { error: unknown }) {
  if (!error) return null
  const detalles = error instanceof ApiError ? error.detalles : []
  return (
    <div className="alerta alerta--error" role="alert">
      {(error as Error).message}
      {detalles.length > 0 && (
        <ul>
          {detalles.map((d) => (
            <li key={d}>{d}</li>
          ))}
        </ul>
      )}
    </div>
  )
}
