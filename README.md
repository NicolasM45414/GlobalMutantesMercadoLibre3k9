# 🧬 Mutant Detector — API REST en Spring Boot

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat&logo=spring)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue?style=flat&logo=gradle)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> Proyecto basado en el desafío técnico de MercadoLibre

Sistema para detectar mutantes mediante análisis de secuencias de ADN. Incluye algoritmo optimizado, API REST, validaciones, persistencia en H2, estadísticas, tests automáticos y despliegue en Render.

---

## 🌐 URL del Deploy en Render

**👉 Deployment:** [`https://globalmutantesmercadolibre3k9.onrender.com`](https://globalmutantesmercadolibre3k9.onrender.com)

---

## 📑 Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Funcionalidades Implementadas](#-funcionalidades-implementadas)
- [Arquitectura y Diseño](#️-arquitectura-y-diseño)
- [Algoritmo isMutant](#-algoritmo-ismutant-detalles-técnicos)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Persistencia](#-persistencia--h2-database)
- [Cómo Ejecutar el Proyecto](#️-cómo-ejecutar-el-proyecto)
- [Tests y Cobertura](#-tests-y-cobertura)
- [Docker](#-docker-opcional)
- [Despliegue en Render](#️-despliegue-en-render)
- [Mejoras Futuras](#-mejoras-futuras)
- [Autor](#-autor)

---

## 🧾 Descripción General

Este proyecto implementa un sistema que permite determinar si una secuencia de ADN pertenece a un mutante. El análisis se basa en la presencia de **más de una secuencia de cuatro bases nitrogenadas iguales consecutivas** (A, T, C, G), ya sea en:

- ✔ **Horizontal**
- ✔ **Vertical**
- ✔ **Diagonal** (↘ y ↗)

### La solución incluye:

- Algoritmo `isMutant(String[] dna)` altamente optimizado
- API REST full RESTful
- Persistencia en H2 con hash para evitar duplicados
- Endpoint de estadísticas `/stats`
- Suite completa de tests unitarios e integración
- JaCoCo > 80%
- Despliegue en Render

---

## 🔬 Funcionalidades Implementadas

### ✔ Nivel 1 — Lógica de detección

- Validación de matriz NxN
- Caracteres válidos: `A`, `T`, `C`, `G`
- Conversión a `char[][]` para rendimiento
- Búsqueda en 4 direcciones
- "Early return" al detectar más de una secuencia

### ✔ Nivel 2 — API REST

**Endpoint:** `POST /mutant`

**Devuelve:**
- `200 OK` → mutante
- `403 Forbidden` → humano
- `400 Bad Request` → ADN inválido (mal formado, no NxN, caracteres inválidos)

Manejo de excepciones centralizado

### ✔ Nivel 3 — Persistencia + Estadísticas

- Base de datos H2 en memoria
- Registro único por ADN mediante hash SHA-256
- Endpoint `GET /stats` mostrando:
  - Total mutantes
  - Total humanos
  - Ratio mutantes/humanos (manejo de división por cero)

---

## 🏗️ Arquitectura y Diseño

### 📌 Capas del sistema

```
controller/       # Endpoints /mutant y /stats
service/          # Lógica de negocio
detector/         # Algoritmo isMutant
repository/       # Spring Data JPA
entity/           # Modelo DnaRecord
dto/              # DnaRequest, StatsResponse
config/           # Swagger + configuración
exception/        # Manejo global de errores
```

### 📌 Principios aplicados

- **Separación de responsabilidades (SoC)**
- **Clean Architecture**
- **SRP** (Single Responsibility Principle)
- DTOs para entrada/salida
- Control de errores centralizado
- Repositorio con consultas específicas

---

## 🧠 Algoritmo isMutant (Detalles Técnicos)

El algoritmo realiza:

### 1. Validación del ADN
- No nulo o vacío
- NxN estricto
- Solo caracteres válidos
- Ninguna fila con longitud distinta

### 2. Conversión a matriz de caracteres
```java
char[][] matrix = new char[n][n];
```

### 3. Búsqueda en 4 direcciones
- Horizontal →
- Vertical ↓
- Diagonal ↘
- Diagonal ↗

### 4. Secuencia objetivo
`XXXX` donde todas las letras coinciden

### ⚡ Optimizaciones

- Corte anticipado cuando `count > 1`
- Sin estructuras extras → O(1) memoria
- **Complejidad:** O(N²)
- Iteraciones mínimas por validaciones previas

---

## 📡 Endpoints de la API

### 🔹 POST `/mutant`

Analiza si un ADN es mutante.

**Request:**
```json
{
  "dna": [
    "ATGCGA",
    "CAGTGC",
    "TTATGT",
    "AGAAGG",
    "CCCCTA",
    "TCACTG"
  ]
}
```

**Respuestas:**

| Código | Descripción |
|--------|-------------|
| `200 OK` | Mutante detectado |
| `403 Forbidden` | No mutante |
| `400 Bad Request` | ADN inválido |

---

### 🔹 GET `/stats`

Devuelve estadísticas actuales.

**Ejemplo de respuesta:**
```json
{
  "count_mutant_dna": 12,
  "count_human_dna": 28,
  "ratio": 0.42
}
```

---

## 💾 Persistencia — H2 Database

### La entidad guardada es:

```java
@Entity
public class DnaRecord {
    @Id
    private String dnaHash;  // SHA-256 del ADN completo
    private boolean mutant;
    private LocalDateTime createdAt;
}
```

### Configuración principal

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

**Consola H2:** [`http://localhost:8080/h2-console`](http://localhost:8080/h2-console)

---

## 🛠️ Cómo Ejecutar el Proyecto

### 1. Clonar

```bash
git clone <URL_DEL_REPO>
cd <proyecto>
```

### 2. Ejecutar con Gradle Wrapper

```bash
./gradlew bootRun
```

### 3. Probar endpoints

- **POST** `http://localhost:8080/mutant`
- **GET** `http://localhost:8080/stats`
- **GET** `http://localhost:8080/h2-console`

---

## 🧪 Tests y Cobertura

### Incluye:

- ✔ Tests unitarios (algoritmo, servicios)
- ✔ Tests de integración (controladores)
- ✔ Mockito para mocks
- ✔ JaCoCo > 80% como pide la prueba

### Ejecutar:

```bash
./gradlew test
./gradlew jacocoTestReport
```

### Reporte:

```
/build/reports/jacoco/test/html/index.html
```

---

## 🐳 Docker (Opcional)

### Build

```bash
docker build -t mutant-detector .
```

### Run

```bash
docker run -p 8080:8080 mutant-detector
```

---

## ☁️ Despliegue en Render

El proyecto puede desplegarse en Render con:

- **Build:** `./gradlew build`
- **Start:** `java -jar build/libs/*.jar`
- **Env:** Java 17

### 📌 Pegar tu URL aquí:

**👉** [`https://globalmutantesmercadolibre3k9.onrender.com`](https://globalmutantesmercadolibre3k9.onrender.com)

---

## 🚀 Mejoras Futuras

- [ ] Integrar PostgreSQL para despliegues productivos
- [ ] Cacheo de respuestas frecuentes
- [ ] Autenticación JWT
- [ ] Monitoreo con Prometheus + Grafana
- [ ] Soporte de concurrencia con CompletableFuture

---

## 👤 Autor

**Nico Moreno**

Proyecto realizado como desafío técnico basándose en las consignas del enunciado original de MercadoLibre.

---

<div align="center">
</div>

