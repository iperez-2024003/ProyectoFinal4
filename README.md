# 🎮 Juego del Ahorcado - Proyecto Final

## 📝 Descripción Corta (Git)
Juego interactivo del Ahorcado con API REST en Spring Boot, base de datos MySQL y aplicación web Java/JSP. Incluye autenticación de usuarios, sistema de pistas y seguimiento de estadísticas de juego.

---

## 📋 Descripción del Proyecto

Este proyecto es una implementación completa del clásico juego del Ahorcado con arquitectura moderna. Consta de dos componentes principales:

1. **API REST (Spring Boot)**: Backend que gestiona la lógica del juego, autenticación de usuarios y operaciones con la base de datos.
2. **Aplicación Web (Java/JSP)**: Interfaz de usuario interactiva para jugar al ahorcado con JavaScript y CSS.

El sistema permite a los usuarios registrarse, autenticarse, jugar partidas del ahorcado con palabras aleatorias y pistas, y mantener un registro de sus estadísticas (partidas jugadas, ganadas y porcentaje de victorias).

---

## 🎯 Problema que Resuelve

- **Aprendizaje interactivo**: Proporciona una forma entretenida de aprender y reforzar vocabulario técnico relacionado con programación y tecnología.
- **Gestión de usuarios**: Soluciona la necesidad de tener un sistema de autenticación y perfil de usuario para personalizar la experiencia de juego.
- **Seguimiento de progreso**: Permite a los usuarios monitorear su mejora a través de estadísticas detalladas de partidas.
- **Arquitectura escalable**: Implementa una separación clara entre frontend y backend, facilitando futuras expansiones.

---

## 🚀 Tecnologías Utilizadas

### Backend
- **Java 21**
- **Spring Boot 4.0.0-SNAPSHOT**
- **Spring Data JPA**
- **MySQL Connector**
- **Lombok**

### Frontend
- **Java Server Pages (JSP)**
- **JavaScript**
- **CSS**
- **HTML5**

### Base de Datos
- **MySQL 8.0+**
- Stored Procedures
- Triggers
- Funciones personalizadas

---

## 📁 Estructura del Proyecto

```
ProyectoFinal4/
├── AhorcadoIverson/              # Aplicación web Java/JSP
│   ├── src/
│   │   ├── java/                # Servlets y lógica Java
│   │   └── conf/                # Configuración
│   ├── web/                     # Recursos web
│   │   ├── Ahorcado.jsp         # Página principal del juego
│   │   ├── index.jsp            # Página de inicio
│   │   ├── Css/                 # Estilos
│   │   └── Js/                  # Lógica JavaScript
│   └── build.xml                # Configuración de build (Ant)
│
├── ProyectoFinalAhorcado/       # API REST Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/            # Controladores, modelos, servicios
│   │   │   └── resources/       # Configuración application.properties
│   └── pom.xml                  # Dependencias Maven
│
└── BaseDeDatosAhorcadoFinal.sql # Script de base de datos
```

---

## ⚙️ Requisitos Previos

- **Java Development Kit (JDK) 21**
- **Apache Maven** (para el proyecto Spring Boot)
- **Apache Tomcat** (para la aplicación JSP)
- **MySQL Server 8.0+**
- **IDE recomendado**: IntelliJ IDEA o NetBeans

---

## 🛠️ Instalación y Configuración

### 1. Configurar la Base de Datos MySQL

Ejecuta el script SQL en tu servidor MySQL:

```bash
mysql -u root -p < BaseDeDatosAhorcadoFinal.sql
```

O ejecuta el contenido del archivo directamente en tu cliente MySQL (phpMyAdmin, MySQL Workbench, etc.).

**Nota**: El script crea:
- Base de datos: `DB_ahorcado`
- Tablas: `palabras`, `usuarios`, `partidas`
- Stored procedures para operaciones CRUD
- Triggers para actualización automática de estadísticas
- Datos de prueba (usuarios y palabras)

### 2. Configurar la API REST (Spring Boot)

Navega al directorio del proyecto Spring Boot:

```bash
cd ProyectoFinalAhorcado
```

Configura las credenciales de MySQL en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/DB_ahorcado?createDatabaseIfNotExist=true&useSSL=false
spring.datasource.username=tu_usuario_mysql
spring.datasource.password=tu_password_mysql
```

Ejecuta la aplicación:

```bash
./mvnw spring-boot:run
```

La API estará disponible en: `http://localhost:8080`

### 3. Configurar la Aplicación Web (JSP)

Este proyecto utiliza Apache Ant para el build. Asegúrate de tener un servidor Tomcat configurado en tu IDE.

- Abre el proyecto `AhorcadoIverson` en NetBeans o IntelliJ IDEA
- Configura el servidor Tomcat
- Ejecuta el proyecto
- La aplicación estará disponible en: `http://localhost:8080/AhorcadoIverson`

---

## 🎮 Cómo Jugar

1. **Registro**: Crea una cuenta de usuario con username, email y contraseña.
2. **Login**: Inicia sesión con tus credenciales.
3. **Nueva Partida**: Inicia una nueva partida del ahorcado.
4. **Jugar**: 
   - Adivina letras de la palabra oculta
   - Usa pistas si necesitas ayuda (máximo 3 por palabra)
   - Tienes 6 intentos antes de perder
5. **Estadísticas**: Revisa tu historial de partidas y porcentaje de victorias.

---

## 📡 Endpoints de la API

La API REST expone los siguientes endpoints principales:

- **POST /api/auth/register** - Registro de usuarios
- **POST /api/auth/login** - Autenticación de usuarios
- **GET /api/palabras** - Obtener todas las palabras
- **GET /api/palabras/random** - Obtener palabra aleatoria
- **POST /api/partidas** - Crear nueva partida
- **PUT /api/partidas/{id}** - Actualizar partida
- **GET /api/usuarios/{id}/estadisticas** - Obtener estadísticas de usuario

---

## 🧪 Datos de Prueba

El script SQL incluye usuarios de prueba:
- **Username**: admin / **Password**: admin123
- **Username**: player1 / **Password**: pass123

Palabras de prueba relacionadas con tecnología y programación.

---

## 📄 Commit Message Sugerido

```
feat: agregar documentación completa del proyecto en README.md

- Descripción detallada del proyecto y sus componentes
- Instrucciones de instalación y configuración
- Guía de uso y endpoints de la API
- Estructura del proyecto y tecnologías utilizadas
- Problema que resuelve el sistema
```

---

## 👤 Autor

**Iverson Pérez**
- Proyecto Final 4to Bimestre
- Ingeniería de Software

---

## 📝 Licencia

Este proyecto es desarrollado con fines educativos.
