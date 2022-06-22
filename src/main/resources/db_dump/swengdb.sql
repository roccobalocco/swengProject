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
-- Table structure for table `cand_x_vot`
--

DROP TABLE IF EXISTS `cand_x_vot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cand_x_vot` (
  `votazione_fk2` int NOT NULL,
  `gruppi_fk2` int NOT NULL,
  PRIMARY KEY (`votazione_fk2`,`gruppi_fk2`),
  KEY `votazione_fk2_idx` (`votazione_fk2`),
  KEY `gruppi_fk2_idx` (`gruppi_fk2`),
  CONSTRAINT `gruppi_fk2` FOREIGN KEY (`gruppi_fk2`) REFERENCES `gruppi` (`id`),
  CONSTRAINT `votazione_fk2` FOREIGN KEY (`votazione_fk2`) REFERENCES `votazione` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cand_x_vot`
--

LOCK TABLES `cand_x_vot` WRITE;
/*!40000 ALTER TABLE `cand_x_vot` DISABLE KEYS */;
/*!40000 ALTER TABLE `cand_x_vot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `candidati`
--

DROP TABLE IF EXISTS `candidati`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `candidati` (
  `id` int NOT NULL,
  `nome` varchar(45) NOT NULL,
  `gruppi_fk` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gruppi_fk_idx` (`gruppi_fk`),
  CONSTRAINT `gruppi_fk` FOREIGN KEY (`gruppi_fk`) REFERENCES `candidati` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidati`
--

LOCK TABLES `candidati` WRITE;
/*!40000 ALTER TABLE `candidati` DISABLE KEYS */;
INSERT INTO `candidati` VALUES (0,'SinistraUnita',0),(1,'Pietro Masolini',0),(2,'Paola',0);
/*!40000 ALTER TABLE `candidati` ENABLE KEYS */;
UNLOCK TABLES;

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
/*!40000 ALTER TABLE `gruppi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `referendum`
--

DROP TABLE IF EXISTS `referendum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `referendum` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descrizione` varchar(45) NOT NULL,
  `si` int NOT NULL DEFAULT '0',
  `no` int NOT NULL DEFAULT '0',
  `bianca` int NOT NULL DEFAULT '0',
  `quorum` tinyint NOT NULL DEFAULT '0',
  `scadenza` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `referendum`
--

LOCK TABLES `referendum` WRITE;
/*!40000 ALTER TABLE `referendum` DISABLE KEYS */;
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
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`cf`),
  UNIQUE KEY `cf_UNIQUE` (`cf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES ('MSLPTR00S07C623M','Pietro','Masolini','2010-11-07',0,'Theangel23'),('MSLPTR00S07C623T','Pietro','Masolini','2000-11-07',1,'Theangel23'),('SCRPLA71C71C623T','Paola','Scaramellini','1971-03-31',0,'Theangel23');
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
  PRIMARY KEY (`cf_fk`,`votazione_fk`),
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
  PRIMARY KEY (`cf_fk`,`referendum_fk`),
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
  `cand_x_vot_fk` int NOT NULL,
  `voti` int NOT NULL DEFAULT '0',
  `assoluta` tinyint NOT NULL DEFAULT '0',
  `scadenza` datetime NOT NULL,
  `descrizione` varchar(450) DEFAULT NULL,
  `ordinale` tinyint NOT NULL DEFAULT '0',
  `preferenza` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `votazione`
--

LOCK TABLES `votazione` WRITE;
/*!40000 ALTER TABLE `votazione` DISABLE KEYS */;
/*!40000 ALTER TABLE `votazione` ENABLE KEYS */;
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

-- Dump completed on 2022-06-22 17:41:44
