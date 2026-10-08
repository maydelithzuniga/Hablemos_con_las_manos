# ---- Etapa 1: compilar la aplicación ----
FROM node:22-alpine AS build
WORKDIR /app

# Instalar dependencias (capa cacheada mientras no cambie el lockfile)
COPY package.json package-lock.json ./
RUN npm ci

# Vite incrusta las variables VITE_* en tiempo de compilación
ARG VITE_API_URL=http://localhost:8080
ENV VITE_API_URL=$VITE_API_URL

COPY . .
RUN npm run build

# ---- Etapa 2: servir los archivos estáticos con nginx ----
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
