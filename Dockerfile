# ==================== Dockerfile ====================
# Multi-stage build para optimizar tamaño de imagen

# ETAPA 1: BUILD
FROM eclipse-temurin:17-alpine AS build

# Copiar código fuente
WORKDIR /app
COPY . .

# Dar permisos de ejecución a gradlew
RUN chmod +x ./gradlew

# Compilar proyecto y generar JAR
RUN ./gradlew bootJar --no-daemon

# ETAPA 2: RUNTIME
FROM eclipse-temurin:17-alpine

# Exponer puerto 8080
EXPOSE 8080

# Copiar JAR desde etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Ejecutar aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]