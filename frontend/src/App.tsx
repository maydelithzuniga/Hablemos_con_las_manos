import { BrowserRouter, Route, Routes } from 'react-router-dom'
import AdminLayout from './admin/AdminLayout'
import Donaciones from './admin/Donaciones'
import Mensajes from './admin/Mensajes'
import NoticiasAdmin from './admin/NoticiasAdmin'
import Postulaciones from './admin/Postulaciones'
import Resumen from './admin/Resumen'
import { ProveedorSesion } from './admin/Sesion'
import TestimoniosAdmin from './admin/TestimoniosAdmin'
import Layout from './components/Layout'
import Aprende from './pages/Aprende'
import BajaNewsletter from './pages/BajaNewsletter'
import Contacto from './pages/Contacto'
import Dona from './pages/Dona'
import DonaGracias from './pages/DonaGracias'
import DondeEstamos from './pages/DondeEstamos'
import Empresas from './pages/Empresas'
import Inicio from './pages/Inicio'
import NoEncontrado from './pages/NoEncontrado'
import Nosotros from './pages/Nosotros'
import NoticiaDetalle from './pages/NoticiaDetalle'
import Noticias from './pages/Noticias'
import PaisDetalle from './pages/PaisDetalle'
import Postula from './pages/Postula'
import ProgramaDetalle from './pages/ProgramaDetalle'
import ProyectoDetalle from './pages/ProyectoDetalle'
import Proyectos from './pages/Proyectos'
import Seguimiento from './pages/Seguimiento'
import Transparencia from './pages/Transparencia'
import Voluntariado from './pages/Voluntariado'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route index element={<Inicio />} />
          <Route path="nosotros" element={<Nosotros />} />
          <Route path="aprende" element={<Aprende />} />
          <Route path="voluntariado" element={<Voluntariado />} />
          <Route path="voluntariado/:slug" element={<ProgramaDetalle />} />
          <Route path="postula" element={<Postula />} />
          <Route path="seguimiento" element={<Seguimiento />} />
          <Route path="proyectos" element={<Proyectos />} />
          <Route path="proyectos/:slug" element={<ProyectoDetalle />} />
          <Route path="donde-estamos" element={<DondeEstamos />} />
          <Route path="donde-estamos/:codigo" element={<PaisDetalle />} />
          <Route path="noticias" element={<Noticias />} />
          <Route path="noticias/:slug" element={<NoticiaDetalle />} />
          <Route path="dona" element={<Dona />} />
          {/* La API devuelve /donar/gracias como URL de retorno del pago */}
          <Route path="donar/gracias" element={<DonaGracias />} />
          <Route path="dona/gracias" element={<DonaGracias />} />
          <Route path="empresas" element={<Empresas />} />
          <Route path="transparencia" element={<Transparencia />} />
          <Route path="contacto" element={<Contacto />} />
          <Route path="newsletter/baja" element={<BajaNewsletter />} />
          <Route path="*" element={<NoEncontrado />} />
        </Route>
        <Route
          path="admin"
          element={
            <ProveedorSesion>
              <AdminLayout />
            </ProveedorSesion>
          }
        >
          <Route index element={<Resumen />} />
          <Route path="postulaciones" element={<Postulaciones />} />
          <Route path="mensajes" element={<Mensajes />} />
          <Route path="donaciones" element={<Donaciones />} />
          <Route path="noticias" element={<NoticiasAdmin />} />
          <Route path="testimonios" element={<TestimoniosAdmin />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}
