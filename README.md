#  SistemaParqueadero (Java/Servlets/JSP)

**Sistema de gestión de parqueadero completo, desarrollado en Java con arquitectura MVC y MySQL.**

---

##  Resumen del Proyecto
Aplicación web diseñada para la administración de vehículos y tarifas en un parqueadero. Desarrollada en **Java (Servlets + JSP)** siguiendo el patrón **MVC**, utiliza **MySQL** como base de datos y conexión **JDBC**.

### Características Principales:
- **CRUD completo** para vehículos y tarifas (Registro, Consulta, Edición y Eliminación).
- **Cálculo automático de cobro** basado en el tiempo de permanencia (precisión decimal).
- Arquitectura limpia con **separación de capas** (Modelo, DAO, Servicio, Servlet).
- Interfaz de usuario dinámica con **JSP, HTML5, CSS3 y JavaScript**.

---

##  Requisitos
Para ejecutar este proyecto, necesitarás los siguientes componentes instalados y configurados:

- **JDK 17** o superior
- **Apache Maven**
- **Servidor de Aplicaciones:** Apache Tomcat 10+ (o similar)
- **Base de Datos:** MySQL o MariaDB
- **Conector JDBC:** Se recomienda usar la versión `mysql-connector-j-9.4.0.jar` (Maven lo gestiona, pero podría requerir ser agregado al classpath de Tomcat).

---

##  Instalación Rápida

Sigue estos pasos para configurar y ejecutar el proyecto localmente:

### 1. Clonar Repositorio y Crear Base de Datos
Clona el proyecto y utiliza el script SQL provisto para crear la base de datos `parkingDB` con las tablas necesarias.

```bash
# Reemplaza [URL_DEL_REPOSITORIO] con la URL real
git clone [URL_DEL_REPOSITORIO] 
cd SistemaParqueadero
mysql -u root -p < parkingDB.sql
````

### 2\. Configurar Credenciales de Conexión

Edita el archivo de configuración para establecer las credenciales de tu base de datos:

```
src/main/java/com/example/parking/util/DBUtil.java
```

Ajusta las variables estáticas **`USER`**, **`PASS`** y **`URL`** según tu entorno de MySQL.

### 3\. Construir y Empaquetar el Proyecto

Utiliza Maven para limpiar, compilar y generar el archivo de despliegue (`.war`):

```bash
mvn clean package
```

### 4\. Desplegar en el Servidor de Aplicaciones

Copia el archivo WAR generado en la carpeta de despliegue de tu servidor (ej. Tomcat):

```bash
cp target/SistemaParqueadero.war <TOMCAT_HOME>/webapps/
```

### 5\. Ejecutar

Inicia o reinicia tu servidor Tomcat. Accede a la aplicación en el navegador:

```arduino
http://localhost:8080/SistemaParqueadero/
```

-----

##  Estructura del Proyecto

El proyecto sigue el patrón MVC con una clara separación de responsabilidades:

```css
SistemaParqueadero/
│
├── pom.xml                   # Configuración de dependencias (Maven)
├── parkingDB.sql             # Script de la base de datos
├── README.md
│
└── src/
    └── main/
        ├── java/com/example/parking/
        │   ├── **model/** → Clases de entidades (Vehiculo, Tarifa)
        │   ├── **dao/** → Data Access Objects (Implementación JDBC)
        │   ├── **service/** → Lógica de negocio/Servicios
        │   └── **servlet/** → Controladores (Manejo de peticiones HTTP)
        │
        └── webapp/
            ├── **WEB-INF/** → Archivos de configuración y JSP
            └── **... (JSP, CSS, JS)** → Vistas y recursos estáticos
```

-----

##  Base de Datos y Lógica de Negocio

El script `parkingDB.sql` establece las siguientes tablas:

| Tabla | Propósito | Relación |
| :--- | :--- | :--- |
| `vehiculo` | Almacena datos de entrada/salida, placa y tipo. | N |
| `tarifa` | Registra los tipos de tarifa y sus valores por hora. | 1 |

**Cálculo de Cobro:**
El cobro se determina mediante la fórmula:
$$ \text{Total} = \text{Tarifa por Hora} \times \text{Horas Totales} $$
*Las horas totales se calculan con precisión decimal (minutos).*

-----

##  Buenas Prácticas Implementadas

  - **Patrón de Diseño:** Implementación de DAO y MVC.
  - **Manejo de Recursos:** Uso de *try-with-resources* en conexiones JDBC.
  - **Seguridad Básica:** Validaciones en formularios y control de errores en Servlets.
  - **Mantenibilidad:** Código limpio, con comentarios relevantes y separación de lógica.

-----

## Notas del Autor

  - **Credenciales:** Para un entorno de producción, se recomienda encarecidamente usar variables de entorno o archivos de configuración externos para las credenciales de la base de datos, en lugar de codificarlas directamente.
  - **Proyecto académico:** Sistema de Gestión de Parqueadero (Java - Maven - MySQL)
  - **Autor:** Kevin David
  - **Versión:** 1.0
  - **Licencia:** MIT
