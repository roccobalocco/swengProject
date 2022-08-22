CREATE DATABASE  IF NOT EXISTS `swengdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `swengdb`;
-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: swengdb
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `gruppi`
--

DROP TABLE IF EXISTS `gruppi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gruppi` (
  `id` int NOT NULL,
  `nome` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gruppi`
--

LOCK TABLES `gruppi` WRITE;
/*!40000 ALTER TABLE `gruppi` DISABLE KEYS */;
INSERT INTO `gruppi` VALUES (1,'PDL'),(2,'PD'),(3,'M5S'),(4,'Lega Nord'),(5,'PSI'),(6,'PDL'),(7,'Lista Civica - Mario Rossi'),(8,'PD - Pino Balatti'),(9,'Partito dei Pensionati - Carlo Tavasci'),(10,'PD'),(11,'M5S'),(12,'Forza Italia');
/*!40000 ALTER TABLE `gruppi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persone`
--

DROP TABLE IF EXISTS `persone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persone` (
  `id` int NOT NULL,
  `nome` varchar(450) DEFAULT NULL,
  `voti` int DEFAULT '0',
  `voti_gruppiFK` int NOT NULL,
  `gruppoFK` int NOT NULL,
  PRIMARY KEY (`voti_gruppiFK`,`gruppoFK`,`id`),
  KEY `gruppoFK_idx` (`gruppoFK`),
  KEY `voti_gruppiFK_idx` (`voti_gruppiFK`),
  CONSTRAINT `gruppoFK` FOREIGN KEY (`gruppoFK`) REFERENCES `gruppi` (`id`),
  CONSTRAINT `voti_gruppiFK` FOREIGN KEY (`voti_gruppiFK`) REFERENCES `voti_gruppi` (`votazione_fk2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persone`
--

LOCK TABLES `persone` WRITE;
/*!40000 ALTER TABLE `persone` DISABLE KEYS */;
INSERT INTO `persone` VALUES (4,'Lettini',2,3,10),(5,'Bettini',1,3,10),(6,'Bellettini',3,3,10),(1,'Renzetti',0,3,11),(2,'Monzetti',0,3,11),(3,'Perletti',0,3,11),(7,'Silvio',0,3,12),(8,'Marco',0,3,12),(9,'Cardinali',0,3,12);
/*!40000 ALTER TABLE `persone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `referendum`
--

DROP TABLE IF EXISTS `referendum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `referendum` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descrizione` varchar(450) NOT NULL,
  `si` int NOT NULL DEFAULT '0',
  `no` int NOT NULL DEFAULT '0',
  `bianca` int NOT NULL DEFAULT '0',
  `quorum` tinyint NOT NULL DEFAULT '0',
  `scadenza` datetime NOT NULL,
  `tot` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `referendum`
--

LOCK TABLES `referendum` WRITE;
/*!40000 ALTER TABLE `referendum` DISABLE KEYS */;
INSERT INTO `referendum` VALUES (1,'Cannabis Legale',0,0,0,0,'2022-07-15 00:00:00',0),(6,'Eutanasia Legale',0,0,0,1,'2022-07-16 00:00:00',0),(7,'Abrogazione Patti Lateranensi',1,0,0,1,'2022-07-19 00:00:00',0);
/*!40000 ALTER TABLE `referendum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utenti`
--

DROP TABLE IF EXISTS `utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utenti` (
  `cf` varchar(16) NOT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `cognome` varchar(45) DEFAULT NULL,
  `data_nascita` date NOT NULL,
  `isAdmin` tinyint NOT NULL DEFAULT '0',
  `password` varchar(450) NOT NULL,
  PRIMARY KEY (`cf`),
  UNIQUE KEY `cf_UNIQUE` (`cf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES ('MSLPTR00S07C623M','Pietro','Masolini','2010-11-07',0,'toInsert'),('MSLPTR00S07C623T','Pietro','Masolini','2000-11-07',1,'toInsert'),('SCRPLA71C71C623T','Paola','Scaramellini','1971-03-31',0,'toInsert');
/*!40000 ALTER TABLE `utenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `v_c`
--

DROP TABLE IF EXISTS `v_c`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `v_c` (
  `cf_fk` varchar(16) NOT NULL,
  `votazione_fk` int NOT NULL,
  KEY `cf_fk_idx` (`cf_fk`),
  KEY `cf_fk2_idx` (`cf_fk`),
  KEY `votazione_fk_idx` (`votazione_fk`),
  CONSTRAINT `cf_fk2` FOREIGN KEY (`cf_fk`) REFERENCES `utenti` (`cf`),
  CONSTRAINT `votazione_fk` FOREIGN KEY (`votazione_fk`) REFERENCES `votazione` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `v_c`
--

LOCK TABLES `v_c` WRITE;
/*!40000 ALTER TABLE `v_c` DISABLE KEYS */;
INSERT INTO `v_c` VALUES ('MSLPTR00S07C623T',2),('MSLPTR00S07C623T',3),('MSLPTR00S07C623T',1);
/*!40000 ALTER TABLE `v_c` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `v_r`
--

DROP TABLE IF EXISTS `v_r`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `v_r` (
  `cf_fk` varchar(16) NOT NULL,
  `referendum_fk` int NOT NULL,
  KEY `cf_fk_idx` (`cf_fk`),
  KEY `referendum_fk_idx` (`referendum_fk`),
  CONSTRAINT `cf_fk` FOREIGN KEY (`cf_fk`) REFERENCES `utenti` (`cf`),
  CONSTRAINT `referendum_fk` FOREIGN KEY (`referendum_fk`) REFERENCES `referendum` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `v_r`
--

LOCK TABLES `v_r` WRITE;
/*!40000 ALTER TABLE `v_r` DISABLE KEYS */;
INSERT INTO `v_r` VALUES ('MSLPTR00S07C623T',7);
/*!40000 ALTER TABLE `v_r` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `votazione`
--

DROP TABLE IF EXISTS `votazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `votazione` (
  `id` int NOT NULL AUTO_INCREMENT,
  `voti` int NOT NULL DEFAULT '0',
  `assoluta` tinyint NOT NULL DEFAULT '0',
  `scadenza` datetime NOT NULL,
  `descrizione` varchar(450) DEFAULT NULL,
  `ordinale` tinyint NOT NULL DEFAULT '0',
  `preferenza` tinyint NOT NULL DEFAULT '0',
  `tot` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `votazione`
--

LOCK TABLES `votazione` WRITE;
/*!40000 ALTER TABLE `votazione` DISABLE KEYS */;
INSERT INTO `votazione` VALUES (1,3,1,'2022-07-18 00:00:00','Amministrative Chiavenna',1,0,0),(2,3,0,'2022-07-18 00:00:00','Amministrative Gordona',0,0,0),(3,3,1,'2022-07-18 00:00:00','Amminsitrative Milano',0,1,0);
/*!40000 ALTER TABLE `votazione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voti_gruppi`
--

DROP TABLE IF EXISTS `voti_gruppi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voti_gruppi` (
  `votazione_fk2` int NOT NULL,
  `gruppi_fk` int NOT NULL,
  `voti_gruppo` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`votazione_fk2`,`gruppi_fk`),
  KEY `gruppi_fk_idx` (`gruppi_fk`),
  CONSTRAINT `gruppi_fk` FOREIGN KEY (`gruppi_fk`) REFERENCES `gruppi` (`id`),
  CONSTRAINT `votazione_fk2` FOREIGN KEY (`votazione_fk2`) REFERENCES `votazione` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voti_gruppi`
--

LOCK TABLES `voti_gruppi` WRITE;
/*!40000 ALTER TABLE `voti_gruppi` DISABLE KEYS */;
INSERT INTO `voti_gruppi` VALUES (1,1,2),(1,2,4),(1,3,3),(1,4,1),(1,5,5),(2,6,0),(2,7,0),(2,8,0),(2,9,1),(3,10,1),(3,11,0),(3,12,0);
/*!40000 ALTER TABLE `voti_gruppi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'swengdb'
--

--
-- Dumping routines for database 'swengdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-18 10:26:11
