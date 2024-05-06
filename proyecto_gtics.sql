CREATE DATABASE  IF NOT EXISTS `proyecto_gtics` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `proyecto_gtics`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: proyecto_gtics
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `idcategorias` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idcategorias`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'Farmacos'),(2,'cuidado personal'),(3,'infantes'),(4,'juguetes');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat` (
  `idchat` int NOT NULL AUTO_INCREMENT,
  `usuarios_id_usuario` int NOT NULL,
  `idusuario_2` int DEFAULT NULL,
  `tipo_chat_idtipo_chat` int NOT NULL,
  PRIMARY KEY (`idchat`),
  KEY `fk_chat_usuarios1_idx` (`usuarios_id_usuario`),
  KEY `fk_chat_usuarios2_idx` (`idusuario_2`),
  KEY `fk_chat_tipo_chat1_idx` (`tipo_chat_idtipo_chat`),
  CONSTRAINT `fk_chat_tipo_chat1` FOREIGN KEY (`tipo_chat_idtipo_chat`) REFERENCES `tipo_chat` (`idtipo_chat`),
  CONSTRAINT `fk_chat_usuarios1` FOREIGN KEY (`usuarios_id_usuario`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `fk_chat_usuarios2` FOREIGN KEY (`idusuario_2`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalles_orden`
--

DROP TABLE IF EXISTS `detalles_orden`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalles_orden` (
  `iddetalles_orden` int NOT NULL AUTO_INCREMENT,
  `ordenes_idordenes` int NOT NULL,
  `productos_idproductos` int NOT NULL,
  `cantidad` int NOT NULL,
  `monto_parcial` float DEFAULT NULL,
  PRIMARY KEY (`iddetalles_orden`),
  KEY `fk_detalles_orden_ordenes1_idx` (`ordenes_idordenes`),
  KEY `fk_detalles_orden_productos1_idx` (`productos_idproductos`),
  CONSTRAINT `fk_detalles_orden_ordenes1` FOREIGN KEY (`ordenes_idordenes`) REFERENCES `ordenes` (`idordenes`),
  CONSTRAINT `fk_detalles_orden_productos1` FOREIGN KEY (`productos_idproductos`) REFERENCES `productos` (`idproductos`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalles_orden`
--

LOCK TABLES `detalles_orden` WRITE;
/*!40000 ALTER TABLE `detalles_orden` DISABLE KEYS */;
INSERT INTO `detalles_orden` VALUES (1,1,1,3,10),(2,2,2,4,40),(3,3,7,10,143),(4,4,8,23,143),(5,5,9,34,143),(6,6,1,60,143),(7,7,7,40,143),(8,8,6,10,143),(9,9,3,25,143),(10,3,6,34,143),(11,3,4,10,143),(12,3,6,120,143),(13,3,8,10,143),(14,3,10,20,143),(15,3,22,1,143),(16,3,21,100,143),(17,6,5,13,0);
/*!40000 ALTER TABLE `detalles_orden` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_orden`
--

DROP TABLE IF EXISTS `estado_orden`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado_orden` (
  `idestado_orden` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) NOT NULL,
  PRIMARY KEY (`idestado_orden`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_orden`
--

LOCK TABLES `estado_orden` WRITE;
/*!40000 ALTER TABLE `estado_orden` DISABLE KEYS */;
INSERT INTO `estado_orden` VALUES (1,'Pendiente'),(2,'Aceptado'),(3,'Rechazado'),(4,'Eliminado'),(5,'En ruta'),(6,'Empaquetado'),(7,'En proceso'),(8,'Recibido'),(9,'Solicitado'),(10,'Entregado');
/*!40000 ALTER TABLE `estado_orden` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_usuario`
--

DROP TABLE IF EXISTS `estado_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado_usuario` (
  `idestado_usuario` varchar(80) NOT NULL,
  PRIMARY KEY (`idestado_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_usuario`
--

LOCK TABLES `estado_usuario` WRITE;
/*!40000 ALTER TABLE `estado_usuario` DISABLE KEYS */;
INSERT INTO `estado_usuario` VALUES ('Activo'),('Baneado'),('Eliminado'),('En revisión');
/*!40000 ALTER TABLE `estado_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mensajes`
--

DROP TABLE IF EXISTS `mensajes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mensajes` (
  `idmensajes` int NOT NULL AUTO_INCREMENT,
  `contenido` varchar(200) NOT NULL,
  `chat_idchat` int NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idmensajes`),
  KEY `fk_mensajes_chat1_idx` (`chat_idchat`),
  CONSTRAINT `fk_mensajes_chat1` FOREIGN KEY (`chat_idchat`) REFERENCES `chat` (`idchat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mensajes`
--

LOCK TABLES `mensajes` WRITE;
/*!40000 ALTER TABLE `mensajes` DISABLE KEYS */;
/*!40000 ALTER TABLE `mensajes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordenes`
--

DROP TABLE IF EXISTS `ordenes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordenes` (
  `idordenes` int NOT NULL AUTO_INCREMENT,
  `usuarios_id_usuario` int NOT NULL,
  `estado_orden_idestado_orden` int DEFAULT NULL,
  `tipo_orden_idtipo_orden` int NOT NULL,
  `codigo` varchar(150) NOT NULL,
  `direccion` varchar(45) DEFAULT NULL,
  `monto` float DEFAULT NULL,
  `recurrente` tinyint DEFAULT NULL,
  `telefono` varchar(45) DEFAULT NULL,
  `tipo_cobro_idtipo_cobro` int DEFAULT NULL,
  `fecha_registro` date DEFAULT NULL,
  `foto_receta` longblob,
  `iddoctor` int DEFAULT NULL,
  `fecha_entrega` date DEFAULT NULL,
  PRIMARY KEY (`idordenes`),
  UNIQUE KEY `codigo_UNIQUE` (`codigo`),
  KEY `fk_ordenes_usuarios1_idx` (`usuarios_id_usuario`),
  KEY `fk_ordenes_estado_orden1_idx` (`estado_orden_idestado_orden`),
  KEY `fk_ordenes_tipo_orden1_idx` (`tipo_orden_idtipo_orden`),
  KEY `fk_ordenes_tipo_cobro1_idx` (`tipo_cobro_idtipo_cobro`),
  KEY `fk_ordenes_usuarios2_idx` (`iddoctor`),
  CONSTRAINT `fk_ordenes_estado_orden1` FOREIGN KEY (`estado_orden_idestado_orden`) REFERENCES `estado_orden` (`idestado_orden`),
  CONSTRAINT `fk_ordenes_tipo_cobro1` FOREIGN KEY (`tipo_cobro_idtipo_cobro`) REFERENCES `tipo_cobro` (`idtipo_cobro`),
  CONSTRAINT `fk_ordenes_tipo_orden1` FOREIGN KEY (`tipo_orden_idtipo_orden`) REFERENCES `tipo_orden` (`idtipo_orden`),
  CONSTRAINT `fk_ordenes_usuarios1` FOREIGN KEY (`usuarios_id_usuario`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `fk_ordenes_usuarios2` FOREIGN KEY (`iddoctor`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordenes`
--

LOCK TABLES `ordenes` WRITE;
/*!40000 ALTER TABLE `ordenes` DISABLE KEYS */;
INSERT INTO `ordenes` VALUES (1,11,1,1,'101',NULL,12,NULL,NULL,1,'2024-04-24',NULL,NULL,'2024-04-24'),(2,12,1,1,'102',NULL,14,NULL,NULL,2,'2024-04-24',NULL,NULL,'2024-04-24'),(3,13,2,2,'103',NULL,14,NULL,NULL,3,'2024-04-24',NULL,NULL,'2024-04-24'),(4,11,1,2,'104',NULL,11,NULL,NULL,2,'2024-04-24',NULL,NULL,'2024-04-24'),(5,11,1,2,'105',NULL,190,NULL,NULL,1,'2024-04-24',NULL,NULL,'2024-04-24'),(6,12,1,2,'106',NULL,11.2,NULL,NULL,2,'2024-04-24',NULL,NULL,'2024-04-24'),(7,12,1,2,'107',NULL,15,NULL,NULL,3,'2024-04-24',NULL,NULL,'2024-04-24'),(8,13,1,2,'108',NULL,15,NULL,NULL,1,'2024-04-24',NULL,NULL,'2024-04-24'),(9,12,1,2,'109',NULL,16,NULL,NULL,2,'2024-04-24',NULL,NULL,'2024-04-24');
/*!40000 ALTER TABLE `ordenes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preferencias_usuario`
--

DROP TABLE IF EXISTS `preferencias_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preferencias_usuario` (
  `idpreferencias_usuario` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idpreferencias_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preferencias_usuario`
--

LOCK TABLES `preferencias_usuario` WRITE;
/*!40000 ALTER TABLE `preferencias_usuario` DISABLE KEYS */;
INSERT INTO `preferencias_usuario` VALUES (1,'asdsajhd');
/*!40000 ALTER TABLE `preferencias_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `idproductos` int NOT NULL AUTO_INCREMENT,
  `categorias_idcategorias` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `codigo` varchar(100) NOT NULL,
  `foto` longblob,
  `descripcion` varchar(200) NOT NULL,
  `fecha_vencimiento` date NOT NULL,
  `precio` float NOT NULL,
  `preferencias_usuario_idpreferencias_usuario` int DEFAULT NULL,
  `estado_producto` varchar(20) NOT NULL,
  PRIMARY KEY (`idproductos`),
  KEY `fk_productos_categorias1_idx` (`categorias_idcategorias`),
  KEY `fk_productos_preferencias_usuario1_idx` (`preferencias_usuario_idpreferencias_usuario`),
  CONSTRAINT `fk_productos_categorias1` FOREIGN KEY (`categorias_idcategorias`) REFERENCES `categorias` (`idcategorias`),
  CONSTRAINT `fk_productos_preferencias_usuario1` FOREIGN KEY (`preferencias_usuario_idpreferencias_usuario`) REFERENCES `preferencias_usuario` (`idpreferencias_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,1,'paracetamol','PAR1',NULL,'para el dolor','2030-04-22',1,1,'Activo'),(2,1,'ibuprofeno','IBU2',NULL,'para la inflamación','2030-04-22',7.2,1,'Activo'),(3,1,'aspirina','ASP3',NULL,'para el dolor','2030-04-22',3.8,1,'Activo'),(4,1,'amoxicilina','AMO4',NULL,'para infecciones','2030-04-22',12.4,1,'Activo'),(5,1,'loratadina','LOR5',NULL,'antihistamínico','2030-04-22',6.75,1,'Activo'),(6,1,'omeprazol','OME6',NULL,'para la acidez estomacal','2030-04-22',8.9,1,'Activo'),(7,1,'simvastatina','SIM7',NULL,'para el colesterol','2030-04-22',15.3,1,'Activo'),(8,1,'metformina','MET8',NULL,'para la diabetes','2030-04-22',9.1,1,'Activo'),(9,1,'atenolol','ATE9',NULL,'para la presión arterial','2030-04-22',5.6,1,'Activo'),(10,1,'diazepam','DIA10',NULL,'para la ansiedad','2030-04-22',11.75,1,'Activo'),(11,1,'sertralina','SER11',NULL,'antidepresivo','2030-04-22',13.2,1,'Activo'),(12,1,'furosemida','FUR12',NULL,'diurético','2030-04-22',7.9,1,'Activo'),(13,1,'digoxina','DIG13',NULL,'para problemas cardíacos','2030-04-22',14.6,1,'Activo'),(14,1,'alprazolam','ALP14',NULL,'ansiolítico','2030-04-22',10.85,1,'Activo'),(15,1,'clopidogrel','CLO15',NULL,'anticoagulante','2030-04-22',17.4,1,'Activo'),(16,1,'trazodona','TRA16',NULL,'antidepresivo','2030-04-22',12.75,1,'Activo'),(17,1,'metronidazol','MET17',NULL,'antibiótico','2030-04-22',6.3,1,'Activo'),(18,1,'citalopram','CIT18',NULL,'antidepresivo','2030-04-22',11.2,1,'Activo'),(19,1,'levotiroxina','LEV19',NULL,'hormona tiroidea','2030-04-22',8.15,1,'Activo'),(20,1,'prednisona','PRE20',NULL,'corticosteroide','2030-04-22',9.8,1,'Activo'),(21,1,'metoclopramida','MET21',NULL,'para las náuseas','2030-04-22',7.3,1,'Activo'),(22,1,'quetiapina','QUE22',NULL,'antipsicótico','2030-04-22',14.7,1,'Activo'),(23,1,'fenitoina','FEN23',NULL,'antiepiléptico','2030-04-22',10.5,1,'Activo'),(24,1,'rosuvastatina','ROS24',NULL,'para el colesterol','2030-04-22',16.9,1,'Activo'),(25,1,'cefalexina','CEF25',NULL,'antibiótico','2030-04-22',6.75,1,'Activo'),(26,1,'risperidona','RIS26',NULL,'antipsicótico','2030-04-22',13.4,1,'Activo'),(27,1,'venlafaxina','VEN27',NULL,'antidepresivo','2030-04-22',11.9,1,'Activo'),(28,1,'metilprednisolona','MET28',NULL,'corticosteroide','2030-04-22',8.6,1,'Activo'),(29,1,'ranitidina','RAN29',NULL,'antihistamínico','2030-04-22',6.5,1,'Activo'),(30,1,'fluoxetina','FLU30',NULL,'antidepresivo','2030-04-22',10.2,1,'Activo'),(31,2,'Cetirizina','CET631',NULL,'Antihistaminico alivia la alergia','2025-11-03',2,NULL,'Eliminado');
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos_has_sedes`
--

DROP TABLE IF EXISTS `productos_has_sedes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos_has_sedes` (
  `productos_idproductos` int NOT NULL,
  `sedes_idsedes` int NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`productos_idproductos`,`sedes_idsedes`),
  KEY `fk_productos_has_sedes_sedes1_idx` (`sedes_idsedes`),
  CONSTRAINT `fk_productos_has_sedes_productos1` FOREIGN KEY (`productos_idproductos`) REFERENCES `productos` (`idproductos`),
  CONSTRAINT `fk_productos_has_sedes_sedes1` FOREIGN KEY (`sedes_idsedes`) REFERENCES `sedes` (`idsedes`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos_has_sedes`
--

LOCK TABLES `productos_has_sedes` WRITE;
/*!40000 ALTER TABLE `productos_has_sedes` DISABLE KEYS */;
INSERT INTO `productos_has_sedes` VALUES (1,1,200),(1,2,120),(2,2,100),(3,2,200),(4,2,300),(5,2,400),(6,2,250),(7,2,170),(8,2,180),(9,2,170),(10,2,160),(11,2,140);
/*!40000 ALTER TABLE `productos_has_sedes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sedes`
--

DROP TABLE IF EXISTS `sedes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sedes` (
  `idsedes` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `direccion` varchar(45) NOT NULL,
  `foto` longblob,
  `distrito` varchar(255) DEFAULT NULL,
  `asignado` int NOT NULL,
  PRIMARY KEY (`idsedes`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sedes`
--

LOCK TABLES `sedes` WRITE;
/*!40000 ALTER TABLE `sedes` DISABLE KEYS */;
INSERT INTO `sedes` VALUES (1,'FarmaSalud','Av. Larco 123',NULL,'Miraflores',0),(2,'Curafácil','Av. Conquistadores 456',NULL,'San Isidro',0),(3,'SaludExpress','Av. Caminos del Inca 789',NULL,'Surco',0),(4,'BienestarPlus','Av. Arequipa 101',NULL,'Lince',0),(5,'RemediMax','Av. Bolognesi 202',NULL,'Barranco',0),(6,'MediRápido','Av. Javier Prado Oeste 303',NULL,'Magdalena del Mar',0),(7,'SaludTotal','Av. Aviación 404',NULL,'San Borja',0),(8,'VitalFarma','Av. Brasil 505',NULL,'Jesus María',0),(9,'CuraBien','Av. La Fontana 606',NULL,'La Molina',0),(10,'Farmaxima','Av. Bolívar 707',NULL,'Pueblo Libre',0);
/*!40000 ALTER TABLE `sedes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_chat`
--

DROP TABLE IF EXISTS `tipo_chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_chat` (
  `idtipo_chat` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idtipo_chat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_chat`
--

LOCK TABLES `tipo_chat` WRITE;
/*!40000 ALTER TABLE `tipo_chat` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipo_chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_cobro`
--

DROP TABLE IF EXISTS `tipo_cobro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_cobro` (
  `idtipo_cobro` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idtipo_cobro`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_cobro`
--

LOCK TABLES `tipo_cobro` WRITE;
/*!40000 ALTER TABLE `tipo_cobro` DISABLE KEYS */;
INSERT INTO `tipo_cobro` VALUES (1,'Efectivo'),(2,'Tarjeta credito'),(3,'Tarjeta debito');
/*!40000 ALTER TABLE `tipo_cobro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_orden`
--

DROP TABLE IF EXISTS `tipo_orden`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_orden` (
  `idtipo_orden` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) NOT NULL,
  PRIMARY KEY (`idtipo_orden`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_orden`
--

LOCK TABLES `tipo_orden` WRITE;
/*!40000 ALTER TABLE `tipo_orden` DISABLE KEYS */;
INSERT INTO `tipo_orden` VALUES (1,'Orden Presencial'),(2,'Orden de Reposicion'),(3,'Orden por Web'),(4,'Orden por chatbot'),(5,'Preorden');
/*!40000 ALTER TABLE `tipo_orden` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_usuario`
--

DROP TABLE IF EXISTS `tipo_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_usuario` (
  `idtipo_usuario` varchar(45) NOT NULL,
  PRIMARY KEY (`idtipo_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_usuario`
--

LOCK TABLES `tipo_usuario` WRITE;
/*!40000 ALTER TABLE `tipo_usuario` DISABLE KEYS */;
INSERT INTO `tipo_usuario` VALUES ('AdministradorDeSede'),('Doctor'),('Farmacista'),('Paciente'),('SuperAdmin');
/*!40000 ALTER TABLE `tipo_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `estado_idestado` varchar(80) NOT NULL,
  `sedes_idsedes` int DEFAULT NULL,
  `nombre` varchar(45) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `contrasena` varchar(64) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `foto` longblob,
  `direccion` varchar(100) DEFAULT NULL,
  `distrito_residencia` varchar(100) DEFAULT NULL,
  `codigo_colegio` varchar(100) DEFAULT NULL,
  `tipo_usuario_idtipo_usuario` varchar(100) NOT NULL,
  `token` varchar(200) DEFAULT NULL,
  `fecha_registro` datetime DEFAULT NULL,
  `seguro` varchar(100) DEFAULT NULL,
  `preferencias_usuario_idpreferencias_usuario` int DEFAULT NULL,
  `dni` varchar(45) DEFAULT NULL,
  `dias_ban` int DEFAULT NULL,
  `fecha_ban` date DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  KEY `fk_usuarios_estado1_idx` (`estado_idestado`),
  KEY `fk_usuarios_sedes1_idx` (`sedes_idsedes`),
  KEY `fk_usuarios_tipo_usuario1_idx` (`tipo_usuario_idtipo_usuario`),
  KEY `fk_usuarios_preferencias_usuario1_idx` (`preferencias_usuario_idpreferencias_usuario`),
  CONSTRAINT `fk_usuarios_estado1` FOREIGN KEY (`estado_idestado`) REFERENCES `estado_usuario` (`idestado_usuario`),
  CONSTRAINT `fk_usuarios_sedes1` FOREIGN KEY (`sedes_idsedes`) REFERENCES `sedes` (`idsedes`),
  CONSTRAINT `fk_usuarios_tipo_usuario1` FOREIGN KEY (`tipo_usuario_idtipo_usuario`) REFERENCES `tipo_usuario` (`idtipo_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=1035 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Activo',NULL,'Mario','mario@renacer.pe','123',NULL,NULL,'Av. Champiñón 123',NULL,NULL,'SuperAdmin',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(2,'Activo',NULL,'Luigi','luigi@renacer.pe','123',NULL,NULL,'Av. Fantasma 456',NULL,NULL,'SuperAdmin',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(11,'Activo',1,'Sonic','sonic@renacer.pe','123',NULL,NULL,'Av. Esmeralda 123','Miraflores',NULL,'AdministradorDeSede',NULL,'2024-04-22 00:00:00',NULL,NULL,'71273167',NULL,NULL),(12,'Activo',2,'Tails','tails@renacer.pe','123',NULL,NULL,'Av. Robotnik 456','San Isidro',NULL,'AdministradorDeSede',NULL,'2024-04-22 00:00:00',NULL,NULL,'71273165',NULL,NULL),(13,'Activo',3,'Pikachu','pikachu@renacer.pe','123',NULL,NULL,'Av. Pokémon 789','Surco',NULL,'AdministradorDeSede',NULL,'2024-04-22 00:00:00',NULL,NULL,'71273160',NULL,NULL),(14,'Activo',10,'Link','link@renacer.pe','123',NULL,NULL,NULL,'Lince',NULL,'AdministradorDeSede',NULL,NULL,NULL,NULL,'71283588',NULL,NULL),(101,'Activo',1,'Cortana','cortana@renacer.pe','123',NULL,NULL,'Av. Inteligencia Artificial 456',NULL,NULL,'Farmacista',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(102,'Activo',2,'Kratos','kratos@renacer.pe','Temporal_password',NULL,NULL,NULL,'Lince','123213','Farmacista',NULL,NULL,NULL,NULL,'1321412',NULL,NULL),(103,'Activo',3,'NathanDrake','nathandrake@renacer.pe','123',NULL,NULL,'Av. Tesoro 101',NULL,NULL,'Farmacista',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(104,'Activo',4,'SolidSnake','solidsnake@renacer.pe','123',NULL,NULL,'Av. Shadow Moses 123',NULL,NULL,'Farmacista',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(105,'Activo',5,'LaraCroft','laracroft@renacer.pe','123',NULL,NULL,'Av. Tomb Raider 456',NULL,NULL,'Farmacista',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(106,'Activo',6,'SamusAran','samusaran@renacer.pe','123',NULL,NULL,'Av. Metroid 789',NULL,NULL,'Farmacista',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(107,'Activo',7,'GordonFreeman','gordonfreeman@renacer.pe','123',NULL,NULL,NULL,'Lince',NULL,'AdministradorDeSede',NULL,NULL,NULL,NULL,'71988987',NULL,NULL),(108,'Activo',8,'Ellie','ellie@renacer.pe','123',NULL,NULL,'Av. Last of Us 123',NULL,NULL,'Farmacista',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(109,'Activo',9,'EzioAuditore','ezioauditore@renacer.pe','123',NULL,NULL,NULL,'Miraflores','712731898','Farmacista',NULL,NULL,NULL,NULL,'',NULL,NULL),(110,'Activo',10,'MasterChief','masterchief@renacer.pe','123',NULL,NULL,'Av. Halo 123',NULL,NULL,'Farmacista',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1001,'Activo',4,'VaultDweller','vaultdweller@renacer.pe','123',NULL,NULL,'Av. Fallout 456',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1002,'Activo',3,'GeraltdeRivia','geralt@renacer.pe','123',NULL,NULL,'Av. Brujo 789',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1003,'Activo',4,'SquallLeonhart','squall@renacer.pe','123',NULL,NULL,'Av. Final Fantasy 101',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1004,'Activo',1,'ChunLi','chunli@renacer.pe','123',NULL,NULL,'Av. Street Fighter 123',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1005,'Activo',3,'DonkeyKong','donkeykong@renacer.pe','123',NULL,NULL,'Av. Gorila 456',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1006,'Activo',9,'Megaman','megaman@renacer.pe','123',NULL,NULL,'Av. Robot 789',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1007,'Activo',8,'CrashBandicoot','crash@renacer.pe','123',NULL,NULL,'Av. Bandicoot 101',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1008,'Activo',10,'Spyro','spyro@renacer.pe','123',NULL,NULL,'Av. Dragón 123',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1009,'Activo',7,'Goku','goku@renacer.pe','123',NULL,NULL,'Av. Saiyan 456',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1010,'Activo',6,'Dovahkiin','dovahkiin@renacer.pe','123',NULL,NULL,'Av. Skyrim 123',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1011,'Activo',5,'MarioBros','mariobros@renacer.pe','123',NULL,NULL,'Av. Champiñón 123',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1012,'Activo',9,'Yoshi','yoshi@renacer.pe','123',NULL,NULL,'Av. Isla de Huevo 456',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1013,'Activo',1,'PrincesaPeach','peach@renacer.pe','123',NULL,NULL,'Av. Reino Champiñón 789',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1014,'Activo',6,'Bowser','bowser@renacer.pe','123',NULL,NULL,'Av. Castillo 101',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1015,'Activo',5,'Zelda','zelda@renacer.pe','123',NULL,NULL,'Av. Reino Hyrule 123',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1016,'Activo',6,'Ganondorf','ganondorf@renacer.pe','123',NULL,NULL,'Av. Reino de las Sombras 456',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1017,'Activo',4,'SamusAran','samusaran@renacer.pe','123',NULL,NULL,'Av. Zebes 789',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1018,'Activo',4,'DonkeyKong','donkeykong@renacer.pe','123',NULL,NULL,'Av. Jungla 101',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1019,'Activo',2,'FoxMcCloud','foxmccloud@renacer.pe','123',NULL,NULL,'Av. Espacio 123',NULL,NULL,'Doctor',NULL,'2024-04-22 00:00:00',NULL,NULL,NULL,NULL,NULL),(1020,'Activo',5,'Pikachu','pikachu@renacer.pe','123',NULL,NULL,'Av. Pueblo Paleta 456',NULL,NULL,'Paciente',NULL,'2024-04-22 00:00:00',NULL,NULL,'',NULL,NULL),(1022,'Baneado',2,'Donald','donald@renacer.pe','Temporal_password',NULL,NULL,NULL,'San Isidro','568781','AdministradorDeSede',NULL,NULL,NULL,NULL,'87654321',3,'2024-05-05'),(1026,'En revisión',2,'Hola Henriquez','diegomarcelo@renacer.pe','Temporal_password',NULL,NULL,NULL,'san miguel','3456789','Farmacista',NULL,NULL,NULL,NULL,'3445677',NULL,NULL),(1027,'En revisión',2,'Diego Henriquez','diegomarcelo@renacer.pe','Temporal_password',NULL,NULL,NULL,'san miguel','71273167','Farmacista',NULL,NULL,NULL,NULL,'71273166',NULL,NULL),(1031,'En revisión',8,'Javier Marroquin','javierM@renacer.pe','Temporal_password',NULL,NULL,NULL,'San Miguel',NULL,'AdministradorDeSede',NULL,NULL,NULL,NULL,'71717171',NULL,NULL),(1032,'Eliminado',3,'Pedro Morales','pedroM@renacer.pe','Temporal_password',NULL,NULL,NULL,'Lince',NULL,'AdministradorDeSede',NULL,NULL,NULL,NULL,'71223366',NULL,NULL),(1033,'Activo',3,'Edith Hurtado','edithHurtado@renacer.pe','Temporal_password',NULL,NULL,NULL,'Miraflores','00981519','Doctor',NULL,NULL,NULL,NULL,'71491810',NULL,NULL),(1034,'Activo',9,'Marcela Suni','mSuni@renacer.pe','Temporal_password',NULL,NULL,NULL,'Lince','00491810','Doctor',NULL,NULL,NULL,NULL,'71293189',NULL,NULL);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-05 21:54:47
