// Datos generales de la organizacion. Lo que no se conoce es contenido de relleno: editalo aqui.
export const sitio = {
  nombre: 'Hablemos con las Manos',
  lema: 'Señas que rompen barreras',
  usuarioRedes: '@LSP_EDUCACION',
  email: 'contacto@hablemosconlasmanos.org',
  telefono: '+51 999 999 999',
  direccion: 'Av. Ejemplo 123, Lima, Perú',
  horario: 'Lunes a viernes de 9:00 a 18:00',
  redes: [
    { nombre: 'Instagram', url: 'https://instagram.com/lsp_educacion' },
    { nombre: 'Facebook', url: 'https://facebook.com/' },
    { nombre: 'TikTok', url: 'https://tiktok.com/' },
    { nombre: 'YouTube', url: 'https://youtube.com/' },
  ],
}

/** Menu principal (misma estructura que americasolidaria.org). */
export const menu = [
  {
    titulo: 'Nosotros',
    hijos: [
      { titulo: 'Quiénes somos', ruta: '/nosotros' },
      { titulo: 'Equipo', ruta: '/nosotros#equipo' },
      { titulo: 'Transparencia', ruta: '/transparencia' },
    ],
  },
  {
    titulo: 'Qué hacemos',
    hijos: [
      { titulo: 'Proyectos', ruta: '/proyectos' },
      { titulo: 'Dónde estamos', ruta: '/donde-estamos' },
      { titulo: 'Aprende LSP', ruta: '/aprende' },
    ],
  },
  {
    titulo: 'Voluntariado',
    hijos: [
      { titulo: 'Programas', ruta: '/voluntariado' },
      { titulo: 'Convocatorias abiertas', ruta: '/voluntariado#convocatorias' },
      { titulo: 'Postula', ruta: '/postula' },
      { titulo: 'Estado de mi postulación', ruta: '/seguimiento' },
    ],
  },
  {
    titulo: 'Súmate',
    hijos: [
      { titulo: 'Dona', ruta: '/dona' },
      { titulo: 'Empresas y alianzas', ruta: '/empresas' },
    ],
  },
  { titulo: 'Noticias', ruta: '/noticias' },
  { titulo: 'Contacto', ruta: '/contacto' },
]
