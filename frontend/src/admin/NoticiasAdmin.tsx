import { useState, type FormEvent } from 'react'
import { api, urlArchivo } from '../api/cliente'
import type { Archivo, Noticia, NoticiaResumen, Pagina } from '../api/tipos'
import { AlertaError } from '../components/Alerta'
import { Cargando, ErrorCarga } from '../components/Estados'
import { fecha } from '../utils/formato'
import { useAdmin } from './useAdmin'

const vacia = { titulo: '', resumen: '', contenido: '', imagenUrl: '', categoria: '', autor: '', publicada: true, destacada: false }

export default function NoticiasAdmin() {
  const { datos, error, cargando, recargar } = useAdmin<Pagina<NoticiaResumen>>('/api/admin/noticias?tamanio=100')
  const [editando, setEditando] = useState<number | 'nueva' | null>(null)
  const [form, setForm] = useState(vacia)
  const [errorForm, setErrorForm] = useState<unknown>(null)
  const [guardando, setGuardando] = useState(false)

  async function editar(id: number | 'nueva') {
    setErrorForm(null)
    if (id === 'nueva') {
      setForm(vacia)
    } else {
      const n = await api<Noticia>(`/api/admin/noticias/${id}`, { auth: true })
      setForm({
        titulo: n.titulo,
        resumen: n.resumen,
        contenido: n.contenido,
        imagenUrl: n.imagenUrl ?? '',
        categoria: n.categoria ?? '',
        autor: n.autor ?? '',
        publicada: n.publicada,
        destacada: n.destacada,
      })
    }
    setEditando(id)
  }

  async function subirImagen(archivo: File) {
    const datos = new FormData()
    datos.append('archivo', archivo)
    try {
      const r = await api<Archivo>('/api/admin/archivos/imagenes', { method: 'POST', body: datos, auth: true })
      setForm((f) => ({ ...f, imagenUrl: r.url }))
    } catch (e) {
      setErrorForm(e)
    }
  }

  async function guardar(e: FormEvent) {
    e.preventDefault()
    setGuardando(true)
    setErrorForm(null)
    try {
      await api(editando === 'nueva' ? '/api/admin/noticias' : `/api/admin/noticias/${editando}`, {
        method: editando === 'nueva' ? 'POST' : 'PUT',
        auth: true,
        body: { ...form, imagenUrl: form.imagenUrl || null },
      })
      setEditando(null)
      recargar()
    } catch (err) {
      setErrorForm(err)
    } finally {
      setGuardando(false)
    }
  }

  async function eliminar(n: NoticiaResumen) {
    if (!confirm(`¿Eliminar "${n.titulo}"?`)) return
    await api(`/api/admin/noticias/${n.id}`, { method: 'DELETE', auth: true })
    recargar()
  }

  const campo = (k: 'titulo' | 'resumen' | 'contenido' | 'categoria' | 'autor') => ({
    value: form[k],
    onChange: (e: { target: { value: string } }) => setForm((f) => ({ ...f, [k]: e.target.value })),
  })

  return (
    <>
      <div className="admin__encabezado">
        <h1>Noticias</h1>
        <button className="boton boton--rosa boton--chico" onClick={() => editar('nueva')}>
          + Nueva noticia
        </button>
      </div>
      {editando !== null && (
        <form className="caja caja--borde formulario" onSubmit={guardar}>
          <h2>{editando === 'nueva' ? 'Nueva noticia' : 'Editar noticia'}</h2>
          <div className="campo">
            <label htmlFor="n-titulo">Título *</label>
            <input id="n-titulo" required maxLength={200} {...campo('titulo')} />
          </div>
          <div className="campo">
            <label htmlFor="n-resumen">Resumen *</label>
            <input id="n-resumen" required maxLength={500} {...campo('resumen')} />
          </div>
          <div className="campo">
            <label htmlFor="n-contenido">Contenido * (separa párrafos con una línea en blanco)</label>
            <textarea id="n-contenido" required rows={10} {...campo('contenido')} />
          </div>
          <div className="formulario__fila">
            <div className="campo">
              <label htmlFor="n-cat">Categoría</label>
              <input id="n-cat" maxLength={60} {...campo('categoria')} />
            </div>
            <div className="campo">
              <label htmlFor="n-autor">Autor</label>
              <input id="n-autor" maxLength={100} {...campo('autor')} />
            </div>
          </div>
          <div className="campo">
            <label htmlFor="n-img">Imagen</label>
            <input id="n-img" type="file" accept="image/png,image/jpeg,image/webp,image/gif" onChange={(e) => e.target.files?.[0] && subirImagen(e.target.files[0])} />
            {form.imagenUrl && <img src={urlArchivo(form.imagenUrl)} alt="" className="admin__miniatura" />}
          </div>
          <label className="check">
            <input type="checkbox" checked={form.publicada} onChange={(e) => setForm({ ...form, publicada: e.target.checked })} />
            <span>Publicada</span>
          </label>
          <label className="check">
            <input type="checkbox" checked={form.destacada} onChange={(e) => setForm({ ...form, destacada: e.target.checked })} />
            <span>Destacada</span>
          </label>
          <AlertaError error={errorForm} />
          <div className="diapositiva__acciones">
            <button className="boton boton--rosa" disabled={guardando}>
              {guardando ? 'Guardando…' : 'Guardar'}
            </button>
            <button type="button" className="boton boton--blanco" onClick={() => setEditando(null)}>
              Cancelar
            </button>
          </div>
        </form>
      )}
      {cargando && !datos ? (
        <Cargando />
      ) : error ? (
        <ErrorCarga mensaje={error} />
      ) : (
        <div className="tabla-envoltura">
          <table className="tabla">
            <thead>
              <tr>
                <th>Título</th>
                <th>Estado</th>
                <th>Fecha</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {datos?.contenido.map((n) => (
                <tr key={n.id}>
                  <td>{n.titulo}</td>
                  <td>
                    <span className={`estado estado--${n.publicada ? 'completada' : 'pendiente'}`}>{n.publicada ? 'publicada' : 'borrador'}</span>
                  </td>
                  <td>{fecha(n.fechaPublicacion, { day: '2-digit', month: 'short', year: 'numeric' })}</td>
                  <td className="tabla__acciones">
                    <button className="boton boton--blanco boton--chico" onClick={() => editar(n.id)}>
                      Editar
                    </button>
                    <button className="boton boton--blanco boton--chico" onClick={() => eliminar(n)}>
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  )
}
