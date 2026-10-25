# Civa Backend API

API REST para gestionar buses, conductores y categorías, autenticar usuarios y consultar viajes entre ciudades. Desarrollada con Java y Spring Boot, con persistencia en PostgreSQL.

Este documento describe las funcionalidades y la configuración incluidas en esta versión del proyecto.

## Contenido

- [Tecnologías](#tecnologías)
- [Funcionalidades implementadas](#funcionalidades-implementadas)
- [Requisitos](#requisitos)
- [Instalación y ejecución local](#instalación-y-ejecución-local)
- [Datos de demostración](#datos-de-demostración)
- [Autenticación y permisos](#autenticación-y-permisos)
- [Documentación de la API](#documentación-de-la-api)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Pruebas y empaquetado](#pruebas-y-empaquetado)
- [Estado actual y pendientes](#estado-actual-y-pendientes)

## Tecnologías

- Java 21 y Spring Boot 3.5.14.
- Maven, con Maven Wrapper incluido.
- Spring Web, Spring Data JPA y Hibernate.
- PostgreSQL.
- Spring Security, BCrypt y JWT mediante JJWT 0.13.0.
- Jakarta Bean Validation.
- MapStruct 1.6.3 y Lombok 1.18.36.
- Springdoc OpenAPI 2.8.13 y Swagger UI.
- JUnit 5, Mockito y Spring Security Test. El proyecto también declara dependencias de Testcontainers.

## Funcionalidades implementadas

- CRUD de buses con marca, categoría y conductores asociados.
- Listado paginado de buses, filtro por `numberBus` y ordenamiento.
- CRUD de categorías y conductores con permisos por rol.
- Registro de usuarios e inicio de sesión con token JWT.
- Búsqueda paginada de viajes programados por ciudad de origen, destino y fecha de salida.
- Conversión entre entidades y DTO mediante MapStruct.
- Validación de solicitudes y manejo centralizado de errores.
- Carga automática de datos de demostración.
- Endpoints para comparar consultas de buses con carga de relaciones y `JOIN FETCH`.
- Documentación OpenAPI, configuración CORS y registros mediante SLF4J/Logback.

## Requisitos

- JDK 21 y `JAVA_HOME` configurado.
- PostgreSQL accesible y una base de datos creada para la aplicación.
- Git. Maven instalado es opcional si se usa el wrapper.

## Instalación y ejecución local

```bash
git clone https://github.com/DinoMatheow/civa-backend-api.git
cd civa-backend-api
git switch dev
cp .env.template .env
```

En una instalación local de PostgreSQL, crea una base de datos vacía antes del primer arranque:

```bash
createdb -U postgres civa
```

El comando requiere que PostgreSQL esté iniciado y que el usuario indicado tenga permiso para crear bases de datos. También puedes crearla desde pgAdmin. Usa ese nombre de base de datos en `CIVA_DB_URL` y configura las credenciales de PostgreSQL en las variables siguientes.

Completa `.env` en la raíz del proyecto con tus valores locales:

```properties
CIVA_DB_URL=jdbc:postgresql://localhost:5432/civa
CIVA_DB_USER=tu_usuario
CIVA_DB_PASSWORD=tu_password
JWT_SECRET_KEY=reemplaza_por_una_clave_aleatoria_de_al_menos_64_bytes
JWT_EXPIRATION=3600000
```

| Variable | Descripción |
| --- | --- |
| `CIVA_DB_URL` | URL JDBC de la base de datos PostgreSQL. |
| `CIVA_DB_USER` | Usuario de conexión a PostgreSQL. |
| `CIVA_DB_PASSWORD` | Contraseña del usuario de PostgreSQL. |
| `JWT_SECRET_KEY` | Clave utilizada para firmar y validar los tokens JWT. |
| `JWT_EXPIRATION` | Tiempo de validez del token en milisegundos; `3600000` equivale a una hora. |

`JWT_EXPIRATION` se expresa en milisegundos. La firma usa HS512, por lo que `JWT_SECRET_KEY` debe contener al menos 64 bytes en UTF-8. Puedes generar una clave con `openssl rand -hex 64`. El perfil `dev` importa el archivo `.env` automáticamente; no publiques sus valores.

Inicia la aplicación:

```bash
bash mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

La API estará disponible en `http://localhost:8080`. El perfil predeterminado es `dev`.

### Perfiles y persistencia

| Perfil | Configuración actual |
| --- | --- |
| `dev` | Importa `.env`, configura PostgreSQL y permite acceso público a Swagger. |
| `prod` | Lee las variables de configuración del entorno y exige autenticación para Swagger. |

**Ambos perfiles tienen `spring.jpa.hibernate.ddl-auto=create`: al iniciar se recrea el esquema y pueden perderse los datos existentes.** Utiliza una base de datos de desarrollo descartable. El cargador de datos se ejecuta sin restricción de perfil.

Para seleccionar otro perfil:

```bash
bash mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

En `prod` debes proporcionar `CIVA_DB_URL`, `CIVA_DB_USER`, `CIVA_DB_PASSWORD`, `JWT_SECRET_KEY` y `JWT_EXPIRATION` como variables de entorno o configuración externa.

## Datos de demostración

Sobre una base vacía, `DataLoader` crea:

- Roles `ROLE_ADMIN` y `ROLE_USER`.
- Dos usuarios de prueba.
- Marcas Volvo, Scania y Mercedes-Benz.
- Categorías Económico, Semi cama y Cama.
- Cuatro conductores y 60 buses.
- Ciudades Lima, Arequipa, Trujillo y Cusco.
- 126 viajes programados para los siete días siguientes al arranque, con rutas desde Lima a las otras ciudades y sus retornos, salidas a las 08:00, 14:00 y 21:00, y 40 asientos disponibles por viaje.

| Usuario | Contraseña de demostración | Roles |
| --- | --- | --- |
| `admin` | `admin` | ADMIN y USER |
| `user` | `user12345` | USER |

Estas cuentas forman parte de la carga de demostración y deben revisarse antes de un despliegue real.

## Autenticación y permisos

El inicio de sesión devuelve `accessToken`. Envía el token en las solicitudes protegidas:

```http
Authorization: Bearer <accessToken>
```

La aplicación usa sesiones sin estado y contraseñas almacenadas con BCrypt. El registro asigna `ROLE_USER` cuando no se indican roles. Actualmente también acepta nombres de roles existentes en el campo `roles` de la solicitud; esta asignación requiere revisión antes de exponer el registro públicamente.

### Endpoints

Las rutas se muestran respecto de `http://localhost:8080`.

| Método | Ruta | Función | Acceso |
| --- | --- | --- | --- |
| POST | `/api/v1/auth/register` | Registrar usuario | Público |
| POST | `/api/v1/auth/login` | Obtener JWT | Público |
| GET | `/api/v1/bus` | Listar y filtrar buses | Público |
| GET | `/api/v1/bus/{id}` | Consultar bus | Público |
| POST | `/api/v1/bus` | Crear bus | ADMIN o USER |
| PUT | `/api/v1/bus/{id}` | Actualizar bus | ADMIN o USER |
| DELETE | `/api/v1/bus/{id}` | Eliminar bus | ADMIN o USER |
| GET | `/api/v1/bus/problematic` | Consulta de relaciones sin optimización | ADMIN o USER |
| GET | `/api/v1/bus/optimize` | Consulta optimizada con JOIN FETCH | ADMIN o USER |
| GET | `/api/v1/bus/optimize-all-details` | Consulta optimizada de detalles | ADMIN o USER |
| GET | `/api/v1/categories` | Listar categorías | ADMIN o USER |
| GET | `/api/v1/categories/{id}` | Consultar categoría | ADMIN o USER |
| POST | `/api/v1/categories` | Crear categoría | ADMIN o USER |
| PUT | `/api/v1/categories/{id}` | Actualizar categoría | ADMIN |
| DELETE | `/api/v1/categories/{id}` | Eliminar categoría | ADMIN |
| GET | `/api/v1/drivers` | Listar conductores | ADMIN |
| GET | `/api/v1/drivers/{id}` | Consultar conductor | ADMIN |
| POST | `/api/v1/drivers` | Crear conductor | ADMIN |
| PUT | `/api/v1/drivers/{id}` | Actualizar conductor | ADMIN |
| DELETE | `/api/v1/drivers/{id}` | Eliminar conductor | ADMIN |
| GET | `/api/v1/trip` | Buscar viajes programados | Público |

Aunque el filtro HTTP permite los GET de buses, las tres rutas de comparación de consultas tienen restricciones adicionales mediante `@PreAuthorize`.

### Ejemplos de uso

Inicio de sesión con la cuenta de demostración:

```bash
curl -X POST 'http://localhost:8080/api/v1/auth/login' \
  -H 'Content-Type: application/json' \
  -d '{"username":"user","password":"user12345"}'
```

Para consultar categorías, copia el valor `accessToken` de la respuesta y úsalo en el encabezado de autorización:

```bash
curl 'http://localhost:8080/api/v1/categories' \
  -H 'Authorization: Bearer <accessToken>'
```

Registro de usuario con el rol predeterminado:

```bash
curl -X POST 'http://localhost:8080/api/v1/auth/register' \
  -H 'Content-Type: application/json' \
  -d '{"username":"viajero","password":"clave_de_prueba","email":"viajero@example.com","name":"Viajero"}'
```

Listado de buses con filtro y paginación:

```bash
curl --get 'http://localhost:8080/api/v1/bus' \
  --data-urlencode 'numberBus=Bus' \
  --data-urlencode 'page=0' \
  --data-urlencode 'size=5' \
  --data-urlencode 'sort=numberBus,asc'
```

Los buses se listan por defecto desde la página `0`, con tamaño `5` y orden por `numberBus`.

Búsqueda de viajes:

```bash
curl 'http://localhost:8080/api/v1/trip?originCityId=1&destinationCityId=2&departureDate=2026-10-25&page=0&size=10'
```

Sustituye los identificadores por los de tu base de datos y la fecha por una con viajes cargados. Los tres filtros son obligatorios; la fecha usa el formato `YYYY-MM-DD`. La consulta devuelve viajes en estado `PROGRAMADO` cuya salida corresponde a ese día. La paginación predeterminada es de 10 elementos, ordenados por `id`.

## Documentación de la API

Con el perfil `dev` activo:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`.
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`.

Para probar rutas protegidas desde Swagger, inicia sesión y utiliza el botón **Authorize** con el token recibido.

CORS permite los orígenes locales `http://localhost:3000`, `http://localhost:4200` y `http://localhost:5173`.

## Estructura del proyecto

```text
src/main/java/com/civa/app/
├── controller/     Controladores REST
├── data/           Carga de datos de demostración
├── domain/         Entidades JPA y enumeraciones
├── dto/            Solicitudes y respuestas
├── exception/      Excepciones y manejo centralizado de errores
├── mapper/         Conversión entre DTO y entidades
├── repository/     Repositorios Spring Data JPA
├── security/       Configuración de seguridad, autenticación y JWT
└── service/        Lógica de negocio
src/main/resources/ Configuración por perfil y Logback
src/test/           Pruebas y configuración de pruebas
```

## Pruebas y empaquetado

Ejecutar las pruebas existentes:

```bash
bash mvnw test
```

Hay pruebas del servicio de buses, del controlador de buses y de carga del contexto de Spring. La prueba de contexto utiliza el perfil predeterminado `dev`, requiere PostgreSQL y las variables correspondientes, y puede recrear el esquema y ejecutar la carga de datos. Utiliza una base de datos descartable también al ejecutar las pruebas. El archivo `application-test.properties` no se activa automáticamente por estar en `src/test/resources`.

Generar el JAR:

```bash
bash mvnw clean package
java -jar target/app-0.0.1-SNAPSHOT.jar
```

## Estado actual y pendientes

- Viajes dispone de búsqueda pública; todavía no hay endpoints para crear, actualizar o eliminar viajes.
- Ciudades y marcas tienen entidades y repositorios, pero no controladores REST propios.
- Hay archivos `Dockerfile` y `docker-compose.yml`, cuya configuración requiere correcciones antes de usarse: el Dockerfile repite el alias de etapa `build` y copia desde `/APP` en lugar de `/app`; Compose usa `JWT_SECRET`, mientras la aplicación espera `JWT_SECRET_KEY`, y no incluye un servicio PostgreSQL.
- Antes de desplegar, es necesario revisar la recreación automática del esquema, la carga de cuentas de demostración y la asignación de roles durante el registro.
- La cobertura de pruebas actual se concentra en buses; no se documenta cobertura equivalente para los demás módulos.
