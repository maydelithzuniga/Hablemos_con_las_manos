# Hablemos con las Manos

Sitio web de la organización de voluntariado **Hablemos con las Manos** —
*Señas que rompen barreras*.

- [`frontend`](frontend/README.md): sitio web en React + TypeScript + Vite.
- [`backend/api_voluntariado`](backend/api_voluntariado/README.md): API REST en Spring Boot
  (programas, convocatorias y postulaciones, proyectos por país, noticias,
  donaciones, newsletter, contacto, equipo, alianzas, transparencia y panel de administración).

## Levantar todo con Docker

```bash
docker compose up -d --build
```

Arranca en orden **base de datos (PostgreSQL) → backend → frontend**: cada
servicio espera a que el anterior esté sano (*healthcheck*) antes de iniciar.

| Servicio | URL |
|---|---|
| Frontend | http://localhost:3000 |
| API | http://localhost:8080 |

Admin por defecto: `admin@hablemosconlasmanos.org` / `Admin12345`.
Para producción cambia `JWT_SECRET`, la contraseña de la base de datos y las URLs
`localhost` en `docker-compose.yml`. Para detener: `docker compose down`
(agrega `-v` para borrar también los datos).
