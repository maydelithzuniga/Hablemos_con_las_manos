# Web — Hablemos con las Manos

Frontend en **React + TypeScript + Vite** del sitio de la organización de
voluntariado. Sigue la estructura de [americasolidaria.org](https://americasolidaria.org/)
(nosotros, qué hacemos, voluntariado, súmate/dona, noticias, contacto) con la
identidad visual del material **“Señas que rompen barreras”** (@LSP_EDUCACION):
colores, tipografías redondeadas, manchas y confeti de fondo, y los personajes
Leo, Sofi y Michi.

## Cómo ejecutarlo

```bash
# 1) Backend (en otra terminal)
cd ../backend/api_voluntariado && ./mvnw spring-boot:run

# 2) Frontend
cd frontend
cp .env.example .env      # VITE_API_URL=http://localhost:8080
npm install
npm run dev               # http://localhost:5173
npm run build             # versión de producción en dist/
```

Si la API no está encendida, las páginas muestran **contenido de ejemplo**
(`src/data/relleno.ts`) con un aviso, para que el sitio nunca se vea vacío.

## Páginas

| Ruta | Sección |
|---|---|
| `/` | Inicio: carrusel, propósito, impacto, programas, convocatorias, datos, proyectos, testimonios, súmate, noticias, aliados |
| `/nosotros` | Historia, misión/visión/valores, línea de tiempo de la LSP, personajes, equipo |
| `/aprende` | ¿Cómo interactuamos con una persona sorda? (consejos, términos, barreras) |
| `/voluntariado`, `/voluntariado/:slug` | Programas, pasos y convocatorias abiertas |
| `/postula`, `/seguimiento` | Formulario de postulación (con CV en PDF) y estado de la postulación |
| `/proyectos`, `/proyectos/:slug` | Proyectos con filtros por país y área |
| `/donde-estamos`, `/donde-estamos/:codigo` | Países |
| `/noticias`, `/noticias/:slug` | Noticias con buscador y categorías |
| `/dona`, `/donar/gracias` | Donación única o socio mensual |
| `/empresas` | Alianzas y formulario para empresas |
| `/transparencia`, `/contacto`, `/newsletter/baja` | Documentos, contacto y baja del boletín |
| `/admin` | Panel del equipo: resumen, postulaciones, mensajes, donaciones, noticias, testimonios |

## Dónde editar

- `src/styles/global.css`: colores y tipografías (variables al inicio del archivo).
- `src/data/sitio.ts`: nombre, correo, teléfono, redes y menú.
- `src/data/contenido.ts`: textos institucionales, datos de la comunidad sorda, consejos y personajes.
- `src/assets/img/`: ilustraciones extraídas del material (WebP).

Los textos marcados como “relleno” (dirección, cuentas bancarias, nombres del
equipo, nombres de los personajes) deben reemplazarse por los datos reales.
