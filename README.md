# 🚗 SistemaParqueadero - Backend API

**API REST profesional para gestión de parqueaderos con Java, JPA/Hibernate y MySQL**

---

## 📋 Descripción

Backend RESTful desarrollado con arquitectura limpia siguiendo principios SOLID y patrones de diseño empresariales. Proporciona endpoints completos para la gestión de vehículos, tarifas, clientes e historial de transacciones.

### Características Principales

- ✅ **API REST completa** con arquitectura RESTful
- ✅ **JPA/Hibernate** como ORM con repositorios
- ✅ **Separación de capas**: Entity → Repository → Service → Controller
- ✅ **DTOs y Mappers** para transferencia de datos
- ✅ **CORS configurado** para frontend externo
- ✅ **Documentación OpenAPI/Swagger**
- ✅ **Health Check endpoint**

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 21 | Lenguaje principal |
| Jakarta Servlet | 5.0 | Controladores HTTP |
| JPA/Hibernate | 6.2.7 | ORM |
| MySQL | 8.0+ | Base de datos |
| Maven | 3.8+ | Gestión de dependencias |
| Jetty | 11 | Servidor de desarrollo |

---

## 🚀 Instalación

### Requisitos Previos

- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Puerto 9090 disponible (configurable)

### Pasos de Instalación

```bash
# 1. Clonar repositorio
git clone <URL_REPOSITORIO>
cd SistemaParqueadero

# 2. Crear base de datos
mysql -u root -p < parkingDB.sql

# 3. Configurar credenciales
# Editar: src/main/resources/application.properties
# Ajustar: db.url, db.user, db.password

# 4. Compilar proyecto
mvn clean package

# 5. Ejecutar con Jetty (desarrollo)
mvn jetty:run

# 6. O desplegar en Tomcat (producción)
cp target/SistemaParqueadero.war $TOMCAT_HOME/webapps/
```

### Verificar Instalación

```bash
# Health Check
curl http://localhost:9090/SistemaParqueadero/health

# Listar vehículos
curl http://localhost:9090/SistemaParqueadero/api/vehiculos
```

---

## 📡 Endpoints de la API

### Base URL

```
http://localhost:9090/SistemaParqueadero
```

### 🚗 Vehículos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/vehiculos` | Listar todos los vehículos |
| POST | `/api/vehiculos` | Registrar entrada de vehículo |
| DELETE | `/api/vehiculos?placa={placa}` | Eliminar vehículo |

#### Ejemplo POST - Registrar Vehículo

```bash
curl -X POST http://localhost:9090/SistemaParqueadero/api/vehiculos \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "placa=ABC123&modelo=Toyota+Corolla&tipo=Carro"
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Vehículo registrado correctamente",
  "data": {
    "id": 1,
    "placa": "ABC123",
    "modelo": "Toyota Corolla",
    "tipo": "Carro",
    "ingreso": "2025-01-15T10:30:00",
    "activo": true,
    "totalPagado": 0.0
  }
}
```

### 💲 Tarifas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/tarifas` | Listar todas las tarifas |
| POST | `/api/tarifas` | Crear nueva tarifa |

### 👤 Clientes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/clientes` | Listar todos los clientes |
| POST | `/api/clientes` | Registrar nuevo cliente |
| DELETE | `/api/clientes?id={id}` | Eliminar cliente |

### 💵 Operaciones

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/cobro?placa={placa}` | Registrar salida y cobro |

#### Ejemplo POST - Cobro

```bash
curl -X POST "http://localhost:9090/SistemaParqueadero/cobro?placa=ABC123"
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Cobro realizado exitosamente",
  "total": 10000.0,
  "horas": 2.0,
  "precioPorHora": 5000.0,
  "vehiculo": "ABC123"
}
```

---

## 📂 Estructura del Proyecto

```
SistemaParqueadero/
├── src/main/java/com/example/parking/
│   ├── entity/              # Entidades JPA (VehiculoEntity, TarifaEntity...)
│   ├── dto/                 # Data Transfer Objects
│   ├── mapper/              # Conversores Entity ↔ DTO
│   ├── repository/          # Repositorios JPA
│   ├── service/             # Lógica de negocio
│   ├── servlet/             # Controladores REST (API Servlets)
│   ├── filter/              # Filtros (CORS, UTF8)
│   └── util/                # Utilidades (JPAUtil, DBUtil)
│
├── src/main/resources/
│   ├── META-INF/
│   │   └── persistence.xml  # Configuración JPA
│   └── application.properties
│
├── parkingDB.sql            # Script de base de datos
├── pom.xml                  # Dependencias Maven
└── README.md
```

---

## 🔧 Configuración

### Base de Datos

Editar `src/main/resources/application.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/parkingDB
db.user=root
db.password=tu_password
```

### Persistencia JPA

Editar `src/main/resources/META-INF/persistence.xml` si necesitas cambiar configuración de Hibernate.

### Puerto del Servidor

En `pom.xml`, sección del plugin Jetty:

```xml
<httpConnector>
    <port>9090</port>  <!-- Cambiar aquí -->
</httpConnector>
```

---

## 🧪 Testing

### Probar Health Check

```bash
curl http://localhost:9090/SistemaParqueadero/health
```

### Probar API con curl

```bash
# GET - Listar vehículos
curl http://localhost:9090/SistemaParqueadero/api/vehiculos

# POST - Registrar vehículo
curl -X POST http://localhost:9090/SistemaParqueadero/api/vehiculos \
  -d "placa=TEST123&tipo=Carro"

# DELETE - Eliminar vehículo
curl -X DELETE "http://localhost:9090/SistemaParqueadero/api/vehiculos?placa=TEST123"
```

---

## 🔐 CORS

El backend tiene CORS habilitado para desarrollo. En producción, editar `CORSFilter.java`:

```java
// Cambiar esto:
res.setHeader("Access-Control-Allow-Origin", "*");

// Por esto:
res.setHeader("Access-Control-Allow-Origin", "https://tu-frontend.com");
```

---

## 📚 Documentación

- **OpenAPI JSON**: `http://localhost:9090/SistemaParqueadero/openapi.json`
- **Health Check**: `http://localhost:9090/SistemaParqueadero/health`

---

## 🏗️ Arquitectura

### Patrón de Capas

```
┌─────────────────────────────────┐
│   Servlet (Controller Layer)    │ ← API REST Endpoints
├─────────────────────────────────┤
│     Service (Business Logic)    │ ← Lógica de negocio
├─────────────────────────────────┤
│   Repository (Data Access)      │ ← JPA/Hibernate
├─────────────────────────────────┤
│    Entity (Domain Models)       │ ← Modelos de dominio
└─────────────────────────────────┘
```

### Principios Aplicados

- **SOLID**: Responsabilidad única, abierto/cerrado, etc.
- **Clean Code**: Nombres descriptivos, funciones cortas
- **DRY**: No repetir código (uso de mappers)
- **Repository Pattern**: Abstracción de acceso a datos
- **DTO Pattern**: Separación de modelo interno y API

---

## 🐛 Troubleshooting

### Error: No se puede conectar a MySQL

```
Verificar:
1. MySQL está corriendo: systemctl status mysql
2. Credenciales en application.properties
3. Base de datos existe: mysql -e "SHOW DATABASES;"
```

### Error: Puerto 9090 en uso

```bash
# Ver qué proceso usa el puerto
lsof -i :9090

# Cambiar puerto en pom.xml o matar proceso
kill -9 <PID>
```

### Error: Hibernate/JPA

```
Verificar:
1. persistence.xml tiene las credenciales correctas
2. Entidades tienen @Entity
3. Revisar logs en consola
```

---

## 📦 Despliegue

### Desarrollo (Jetty)

```bash
mvn jetty:run
```

### Producción (Tomcat)

```bash
# 1. Compilar WAR
mvn clean package

# 2. Copiar a Tomcat
cp target/SistemaParqueadero.war $TOMCAT_HOME/webapps/

# 3. Reiniciar Tomcat
$TOMCAT_HOME/bin/shutdown.sh
$TOMCAT_HOME/bin/startup.sh
```

---

## 🤝 Contribuir

1. Fork el proyecto
2. Crear rama: `git checkout -b feature/nueva-funcionalidad`
3. Commit: `git commit -m 'Agregar nueva funcionalidad'`
4. Push: `git push origin feature/nueva-funcionalidad`
5. Pull Request

---

## 📄 Licencia

MIT License - Ver archivo LICENSE

---

## 👨‍💻 Autor

**Kevin David**  
Proyecto Académico - Sistema de Gestión de Parqueadero  
Versión: 2.0 (Backend Separado)

---

## 🔗 Enlaces

- **Frontend Web**: [SistemaParqueaderoFrontendWeb](../SistemaParqueaderoFrontendWeb)
- **Documentación API**: `http://localhost:9090/SistemaParqueadero/api-docs`