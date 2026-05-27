# Proyecto GTICS - Sistema de Gestión de Farmacia y Ventas en Línea

## Descripción General
Este repositorio contiene el código fuente de un sistema integral diseñado para administrar la operatividad comercial, logística y de atención al cliente de una red de farmacias. Su arquitectura centralizada permite la gestión eficiente de múltiples sedes, un riguroso control de inventarios, procesamiento de ventas a través de comercio electrónico y asistencia médica remota, garantizando una experiencia de usuario segura e intuitiva.

El sistema fue concebido utilizando prácticas modernas de desarrollo bajo el framework Spring Boot, favoreciendo la escalabilidad, la robustez en la manipulación de transacciones y una integración continua con APIs de terceros.

## Arquitectura y Tecnologías
La plataforma está estructurada como un sistema monolítico tradicional con renderizado del lado del servidor (SSR), apoyado por los siguientes componentes tecnológicos:

### Backend y Lógica de Negocio
* **Java 17:** Lenguaje principal de desarrollo, utilizando las últimas características del JDK.
* **Spring Boot 3.2.4:** Framework base para el despliegue del servidor embebido, inyección de dependencias y configuración general.
* **Spring Web (MVC):** Manejo de solicitudes HTTP y controladores REST.

### Persistencia y Base de Datos
* **MySQL 8:** Motor de base de datos relacional para almacenamiento estructurado.
* **Spring Data JPA / Hibernate:** Mapeo objeto-relacional (ORM) para simplificar las transacciones y la abstracción de la base de datos.
* **Spring Session JDBC:** Gestión persistente de sesiones de usuario directamente en la base de datos para soportar escalabilidad horizontal.

### Frontend
* **Thymeleaf:** Motor de plantillas principal para construir vistas dinámicas en el servidor.
* **HTML5, CSS3 y JavaScript (Vanilla):** Utilizados para interactividad del cliente, validaciones locales y estilos de las interfaces web.

### Seguridad y Control de Acceso
* **Spring Security:** Autenticación integral del sistema y autorización basada en roles (RBAC).

### Herramientas e Integraciones de Terceros
* **WebSockets (Spring WebSocket):** Habilita canales de comunicación full-duplex y asíncronos para el módulo de chat en tiempo real.
* **Apache HttpClient & OpenAI API:** Implementación de un agente virtual inteligente que asesora y asiste a los usuarios a lo largo del proceso de compra.
* **iTextPDF:** Generador dinámico de reportes y facturas en formato PDF exportable.
* **JavaMailSender:** Automatización del envío de notificaciones y correos transaccionales (confirmaciones de orden, recuperación de contraseñas).

## Módulos Principales del Sistema

### 1. Gestión de Roles y Usuarios (RBAC)
La aplicación asegura la integridad de los datos implementando acceso restringido a cuatro perfiles principales:
- **Paciente:** Puede visualizar el catálogo, interactuar con el chat bot o farmacista, y ejecutar órdenes de compra.
- **Farmacista:** Atiende consultas de pacientes mediante chat en vivo y asiste de forma técnica.
- **Administrador de Sede:** Controla el inventario, gestiona el reabastecimiento logístico y autoriza órdenes para una ubicación física particular.
- **Superadmin:** Visión global del sistema, gestión de cuentas de administrador, auditoría y control de todas las sedes.

### 2. Comercio Electrónico y Control de Inventarios
- **Catálogo de Productos:** Presentación categorizada de medicamentos con filtrado dinámico.
- **Carrito de Compras:** Almacenamiento temporal de productos y cálculos de totales.
- **Procesamiento de Órdenes:** Flujo completo de checkout que asocia la compra con ubicaciones físicas (Sedes) para recojo presencial o envío.
- **Trazabilidad:** Monitoreo del estado de una orden (Pendiente, Procesada, Completada, etc.).

### 3. Sistema de Comunicación Médica (Chat)
- Interfaz interactiva para pacientes que requieren consultas puntuales sobre medicamentos.
- Comunicación bidireccional soportada por sockets, reduciendo la latencia y la necesidad de actualizaciones (polling) constantes por parte del cliente HTTP.

### 4. Asistente Virtual (OpenAI)
Integración que potencia la experiencia de usuario mediante un bot automatizado que asiste en la lectura de información médica, sugerencias estandarizadas y resolución de dudas iniciales previas al contacto con un farmacista real.

## Requisitos Previos e Instalación

Para ejecutar este entorno de manera local, asegúrese de contar con los siguientes elementos instalados en su entorno:
1. **Java Development Kit (JDK) 17** o superior.
2. **Apache Maven** (o utilizar el wrapper incluido `mvnw`).
3. **Servidor MySQL 8.0** activo y en ejecución.

### Configuración del Entorno
1. Clone este repositorio en su entorno local.
2. Localice el archivo de propiedades en la ruta `src/main/resources/application.properties`.
3. Configure los parámetros de conexión de su entorno local, principalmente las directivas:
   - `spring.datasource.url`
   - `spring.datasource.username`
   - `spring.datasource.password`
4. Ejecute el volcado de datos inicial de ser necesario (se asume que un archivo SQL base está disponible).

### Ejecución
Desde la raíz del proyecto ejecute el siguiente comando para iniciar el servidor de desarrollo:
```bash
./mvnw spring-boot:run
```
La aplicación inicializará y quedará a la escucha en el puerto designado (por defecto `8080`).

## Contribuciones
Para realizar contribuciones significativas al código fuente o corregir eventualidades detectadas en el sistema, por favor genere un *Pull Request* hacia la rama de desarrollo describiendo los cambios efectuados con claridad. Asegúrese de que el código compila y cumple con las normativas internas del proyecto.