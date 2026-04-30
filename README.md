# Civa App — Backend
 
API REST para gestionar la información de buses de la empresa Civa. Expone endpoints para listar y consultar buses desde una base de datos PostgreSQL.
 
---
 
## Tecnologías
 
- Java 21
- Spring Boot 3.5
- Spring Data JPA
- PostgreSQL
- Lombok
- MapStruct
---
 ## Estructura del proyecto
 
```
src/main/java/com/civa/app/
├── controller/   ← BusController (endpoints REST)
├── domain/       ← Entidades: Bus, MarcaBus, Status
├── dto/          ← BusResponseDTO
├── mapper/       ← BusMapper (MapStruct)
├── repository/   ← BusRepository (JPA)
└── service/      ← IBusService, BusService
```
 
---
 
## Requisitos previos
 
- Java 21
- Maven 3.8+
- PostgreSQL 14+
---
 
## Configuración de la base de datos
 
Crea la base de datos en PostgreSQL:
 
```sql
CREATE DATABASE civa_buses_db;
```
 
Luego edita `src/main/resources/application.properties` con tus credenciales:
 
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/civa_buses_db
spring.datasource.username=postgres
spring.datasource.password=root
```
 
---
 
## Ejecución
 
```bash
./mvnw spring-boot:run
```
 
El servidor corre en `http://localhost:8080`
 
---
 
## Endpoints
 
| Método | Ruta     | Descripción               |
|--------|----------|---------------------------|
| GET    | `/bus`   | Obtiene todos los buses   |
| GET    | `/bus/{id}`  | Obtiene un bus por su ID  |
 
**Ejemplo de respuesta `GET /bus`:**
```json
[
  {
    "id": 1,
    "numberBus": "644",
    "plate": "ABC-123",
    "attributes": "Aire acondicionado, WiFi",
    "status": "ACTIVO",
    "marcaBus": "Mercedes",
    "createdAt": "2025-04-29T08:00:00"
  }
]
```
 
**Ejemplo de respuesta `GET /1`:**
```json
{
  "id": 1,
  "numberBus": "644",
  "plate": "ABC-123",
  "attributes": "Aire acondicionado, WiFi",
  "status": "ACTIVO",
  "marcaBus": "Mercedes",
  "createdAt": "2025-04-29T08:00:00"
}
```

## Ramas

| Rama  | Descripción                        |
|-------|------------------------------------|
| main  | Versión estable lista para probar  |
| dev   | Desarrollo, puede ser inestable    |

> Para ejecutar el proyecto use la rama `main`.
