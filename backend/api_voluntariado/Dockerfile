# ---- Etapa 1: compilar el JAR con Maven ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Descargar dependencias (capa cacheada mientras no cambie el pom.xml)
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q package -DskipTests

# ---- Etapa 2: ejecutar con solo el JRE ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Carpeta de archivos subidos (CV, imagenes)
RUN mkdir -p /app/uploads
ENV UPLOADS_DIR=/app/uploads

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
