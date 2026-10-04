# API Voluntariado — Hablemos con las Manos

Backend (API REST) del sitio web de la organización de voluntariado
**Hablemos con las Manos**, pensado para un sitio del estilo de
[América Solidaria](https://americasolidaria.org/): programas de voluntariado,
convocatorias y postulaciones, proyectos por país, noticias, donaciones,
alianzas, equipo, transparencia y un panel de administración.

**Tecnologías:** Java 17 · Spring Boot 4 · Spring Data JPA · Spring Security (JWT) ·
H2 (desarrollo) / PostgreSQL (producción) · Lombok.

## Cómo ejecutarlo

```bash
cd backend/api_voluntariado
./mvnw spring-boot:run        # http://localhost:8080
./mvnw test                   # pruebas de integración
```

Al primer arranque se crea:

- El **usuario administrador** `admin@hablemosconlasmanos.org` / `Admin12345`
  (cámbialo con `ADMIN_EMAIL` / `ADMIN_PASSWORD`, o desde el panel).
- **Datos de ejemplo** (países, programas, proyectos, convocatorias, noticias,
  testimonios, cifras) para que el frontend tenga contenido desde el día 1.
  Se desactiva con `DATOS_EJEMPLO=false`.

En desarrollo la base de datos es H2 guardada en `./data` (consola en
`/h2-console`, JDBC URL `jdbc:h2:file:./data/voluntariadodb`).

### Producción

```bash
docker compose up -d                          # PostgreSQL local (opcional)
SPRING_PROFILES_ACTIVE=prod \
DB_URL=jdbc:postgresql://host:5432/voluntariado DB_USERNAME=... DB_PASSWORD=... \
JWT_SECRET=un-secreto-largo-de-al-menos-32-caracteres \
ADMIN_PASSWORD=... CORS_ALLOWED_ORIGINS=https://tu-dominio.org \
java -jar target/api_voluntariado-0.0.1-SNAPSHOT.jar
```

| Variable | Para qué sirve | Valor por defecto |
|---|---|---|
| `JWT_SECRET` | Firma de los tokens del panel (**cambiar**) | secreto de desarrollo |
| `ADMIN_EMAIL` / `ADMIN_PASSWORD` | Administrador inicial | `admin@hablemosconlasmanos.org` / `Admin12345` |
| `CORS_ALLOWED_ORIGINS` | URL(s) del frontend | `http://localhost:5173,http://localhost:3000` |
| `DB_URL` / `DB_USERNAME` / `DB_PASSWORD` | Base de datos | H2 en archivo |
| `UPLOADS_DIR` / `UPLOADS_URL` | Carpeta y URL pública de archivos subidos | `./uploads` |
| `CORREO_HABILITADO`, `MAIL_HOST`, `MAIL_USERNAME`, `MAIL_PASSWORD` | Envío de correos (si está deshabilitado solo se registran en el log) | deshabilitado |
| `CORREO_NOTIFICACIONES` | Correo del equipo que recibe avisos de postulaciones y contacto | `contacto@...` |
| `DONACION_URL_RETORNO` | Página del frontend a la que vuelve el donante | `http://localhost:5173/donar/gracias` |
| `DONACION_WEBHOOK_SECRET` | Secreto compartido con la pasarela de pago | secreto de desarrollo |
| `NEWSLETTER_URL_BAJA` | Página del frontend para darse de baja del boletín | `http://localhost:5173/newsletter/baja` |

## Arquitectura

```
controller/        Endpoints públicos del sitio
controller/admin/  Endpoints del panel de administración (/api/admin/**)
service/           Reglas de negocio
repository/        Repositorios Spring Data JPA
entity/            Entidades JPA (modelo de datos)
dto/               Objetos de entrada (Request, con validaciones) y salida (DTO)
security/          Emisión de JWT y límite de envíos por IP
config/            Seguridad, CORS, archivos estáticos y datos iniciales
exception/         Manejo global de errores (respuestas JSON uniformes)
util/              Slugs, códigos aleatorios y CSV
```

## Secciones del sitio y sus endpoints públicos

| Sección del sitio | Método y ruta | Descripción |
|---|---|---|
| Portada | `GET /api/inicio` | Programas y proyectos destacados, convocatorias abiertas, últimas noticias, testimonios, aliados y cifras, en una sola llamada |
| Cifras de impacto | `GET /api/impacto` | Cifras editables y calculadas (países, proyectos, beneficiarios, voluntarios aceptados, socios) |
| Programas | `GET /api/programas?tipo=` · `GET /api/programas/{slug}` | Voluntariado profesional, juvenil, corporativo... |
| Dónde estamos | `GET /api/paises` · `GET /api/paises/{codigo}` | País con sus proyectos y convocatorias abiertas |
| Proyectos | `GET /api/proyectos?pais=&area=&estado=&pagina=&tamanio=` · `GET /api/proyectos/destacados` · `GET /api/proyectos/{slug}` | Listado paginado con filtros |
| Convocatorias | `GET /api/convocatorias` · `GET /api/convocatorias/{id}` | Solo las abiertas hoy |
| Postula | `POST /api/archivos/cv` (multipart, campo `archivo`, PDF ≤ 5 MB) → `POST /api/postulaciones` | Devuelve un código de seguimiento y envía un correo de confirmación |
| Estado de mi postulación | `GET /api/postulaciones/seguimiento/{codigo}?email=` | Requiere el código y el email |
| Noticias | `GET /api/noticias?q=&categoria=&pagina=` · `GET /api/noticias/categorias` · `GET /api/noticias/{slug}` | Blog |
| Dona / Hazte socio | `POST /api/donaciones` · `GET /api/donaciones/{referencia}` | Donación única o mensual; devuelve `urlPago` |
| Webhook de pagos | `POST /api/donaciones/webhook` (header `X-Webhook-Secret`) | La pasarela confirma el pago |
| Newsletter | `POST /api/newsletter` · `DELETE /api/newsletter/{token}` | Suscripción y baja |
| Contacto / Empresas | `POST /api/contacto` | Tipos: `GENERAL`, `VOLUNTARIADO`, `EMPRESAS`, `PRENSA`, `DONACIONES` |
| Quiénes somos | `GET /api/equipo?area=` | Equipo, directorio y consejo asesor |
| Alianzas | `GET /api/aliados?tipo=` | Logos de empresas y organizaciones aliadas |
| Testimonios | `GET /api/testimonios` · `POST /api/testimonios` | Los enviados desde la web quedan pendientes de aprobación |
| Transparencia | `GET /api/transparencia?tipo=` | Memorias, estados financieros, auditorías |

Los archivos públicos (imágenes, PDF de transparencia) se sirven en `/uploads/...`.
Los CV se guardan en una carpeta **privada** y solo se descargan desde el panel.

## Panel de administración

1. `POST /api/auth/login` con `{"email": "...", "password": "..."}` → `{"token": "..."}`.
2. Enviar `Authorization: Bearer <token>` en cada petición a `/api/admin/**`.
3. `GET /api/auth/yo` devuelve el usuario actual; `PUT /api/auth/password` cambia la contraseña.

Roles:

- **EDITOR**: gestiona el contenido del sitio (programas, países, proyectos,
  convocatorias, noticias, aliados, equipo, testimonios, transparencia, cifras e imágenes).
- **ADMIN**: todo lo anterior y además los datos personales: postulaciones,
  donaciones, mensajes de contacto, suscriptores, usuarios y dashboard.

| Recurso | Rutas | Rol |
|---|---|---|
| Dashboard | `GET /api/admin/dashboard` | ADMIN |
| Programas, países, convocatorias, aliados, equipo, transparencia, cifras | `GET/POST /api/admin/{recurso}` · `PUT/DELETE /api/admin/{recurso}/{id}` (`programas`, `paises`, `convocatorias`, `aliados`, `equipo`, `transparencia`, `cifras`) | EDITOR |
| Proyectos | `GET /api/admin/proyectos` (paginado) · `GET/PUT/DELETE /{id}` · `POST` | EDITOR |
| Noticias (incluye borradores) | `GET /api/admin/noticias` · `GET/PUT/DELETE /{id}` · `POST` | EDITOR |
| Testimonios | `GET /api/admin/testimonios?aprobado=false` · `PATCH /{id}/aprobacion?aprobado=true` · `POST` · `PUT` · `DELETE` | EDITOR |
| Imágenes y documentos | `POST /api/admin/archivos/imagenes` · `POST /api/admin/archivos/documentos` | EDITOR |
| Postulaciones | `GET /api/admin/postulaciones?estado=&convocatoriaId=&q=` · `GET /{id}` · `PATCH /{id}/estado` · `DELETE /{id}` · `GET /exportar` (CSV) | ADMIN |
| CV de postulantes | `GET /api/admin/archivos/cv/{nombre}` | ADMIN |
| Donaciones | `GET /api/admin/donaciones?estado=&tipo=` · `PATCH /{id}/estado?estado=COMPLETADA` · `GET /exportar` (CSV) | ADMIN |
| Mensajes de contacto | `GET /api/admin/mensajes?tipo=&leido=` · `GET /{id}` · `PATCH /{id}` · `DELETE /{id}` | ADMIN |
| Suscriptores | `GET /api/admin/suscriptores` · `GET /exportar` (CSV) · `DELETE /{id}` | ADMIN |
| Usuarios del panel | `GET/POST /api/admin/usuarios` · `PUT/DELETE /{id}` | ADMIN |

Proceso de selección de una postulación:
`RECIBIDA → EN_REVISION → ENTREVISTA → ACEPTADA / RECHAZADA` (o `RETIRADA`).
Con `"notificar": true` en `PATCH /estado` se avisa al postulante por correo.

## Donaciones y pasarela de pago

1. El frontend envía `POST /api/donaciones`; se guarda la donación como `PENDIENTE`
   y se responde con `urlPago`.
2. El donante paga en la pasarela.
3. La pasarela llama a `POST /api/donaciones/webhook` con el header
   `X-Webhook-Secret`; la donación pasa a `COMPLETADA` y se envía un correo de agradecimiento.

Por defecto se usa `PasarelaPagoSimulada` (no cobra). Para conectar Mercado Pago,
Culqi, Niubiz, Stripe o PayPal, crea una clase que implemente
`PasarelaPagoService` (marcada con `@Primary`) y adapta `NotificacionPagoRequest`
al formato de notificación de esa pasarela. Las transferencias bancarias se
pueden confirmar a mano desde el panel.

## Seguridad incluida

- Contraseñas con BCrypt; tokens JWT (HS256) con expiración configurable.
- Límite de 20 envíos por minuto por IP en los formularios públicos y el login (respuesta 429).
- Validación de todos los formularios, con mensajes en español.
- Los archivos subidos se validan por su contenido real (no por la extensión); no se aceptan SVG.
- CV privados; el seguimiento de una postulación exige el código y el email.
- Exportaciones CSV protegidas contra inyección de fórmulas en Excel.
- Siempre debe quedar al menos un administrador activo.

## Formato de errores

```json
{
  "fecha": "2026-10-04T10:15:30",
  "estado": 400,
  "error": "Bad Request",
  "mensaje": "Error de validacion en los datos enviados",
  "ruta": "/api/contacto",
  "detalles": ["El email no es valido"]
}
```
