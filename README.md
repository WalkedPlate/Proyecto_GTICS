# Proyecto GTICS - Sistema de Gestión de Farmacia y Ventas en Línea

## Descripción del Proyecto
Proyecto desarrollado para la gestión integral de sedes farmacéuticas, control de inventario y ventas en línea. Permite la interacción entre pacientes y farmacistas, administración de roles y manejo de órdenes de compra con funcionalidades avanzadas como soporte de chat en tiempo real e integración con OpenAI.

## Tecnologías Principales
* **Backend:** Java 17, Spring Boot 3.2.4
* **Persistencia:** Spring Data JPA (Hibernate), MySQL 8
* **Seguridad:** Spring Security, Spring Session JDBC
* **Frontend:** Thymeleaf, HTML5, CSS3, JavaScript
* **Integraciones:** WebSockets, Apache HttpClient (OpenAI), iTextPDF, JavaMailSender

## Características Clave
* **Control de Acceso Basado en Roles (RBAC):** Perfiles definidos para Paciente, Farmacista, Administrador de Sede y Superadmin.
* **Comercio Electrónico:** Carrito de compras, procesamiento de órdenes y gestión de inventario por sedes.
* **Comunicación en Tiempo Real:** Chat webSocket integrado para comunicación directa entre pacientes y farmacéuticos.
* **Integración de Servicios:** Envío automático de notificaciones por correo electrónico y soporte asistido por IA (ChatGPT).

## Enlaces de Despliegue
* Producción: http://18.233.247.81:8080/