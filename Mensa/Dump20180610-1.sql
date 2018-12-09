CREATE DATABASE  IF NOT EXISTS `MensaScolastica` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `MensaScolastica`;
-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: MensaScolastica
-- ------------------------------------------------------
-- Server version	8.0.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Allergene`
--

DROP TABLE IF EXISTS `Allergene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Allergene` (
  `nome` varchar(30) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Allergene`
--

LOCK TABLES `Allergene` WRITE;
/*!40000 ALTER TABLE `Allergene` DISABLE KEYS */;
INSERT INTO `Allergene` VALUES ('arachidi'),('crostacei'),('frutta_a_guscio'),('glutine'),('latte'),('lupini'),('molluschi'),('pesce'),('sedano'),('senape'),('sesamo'),('soia'),('solfiti'),('uova');
/*!40000 ALTER TABLE `Allergene` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Allergico`
--

DROP TABLE IF EXISTS `Allergico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Allergico` (
  `idPersona` int(11) NOT NULL,
  `idAllergene` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`idPersona`,`idAllergene`),
  KEY `fk_allergico_a` (`idAllergene`),
  CONSTRAINT `fk_allergico_a` FOREIGN KEY (`idAllergene`) REFERENCES `allergene` (`nome`) ON UPDATE CASCADE,
  CONSTRAINT `fk_persona` FOREIGN KEY (`idPersona`) REFERENCES `persona` (`idPersona`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Allergico`
--

LOCK TABLES `Allergico` WRITE;
/*!40000 ALTER TABLE `Allergico` DISABLE KEYS */;
INSERT INTO `Allergico` VALUES (2,'arachidi'),(680,'arachidi'),(679,'crostacei'),(1,'glutine'),(679,'glutine'),(680,'sedano');
/*!40000 ALTER TABLE `Allergico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Contiene`
--

DROP TABLE IF EXISTS `Contiene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Contiene` (
  `idPiatto` int(11) NOT NULL,
  `allergene` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`idPiatto`,`allergene`),
  KEY `fk_allergene` (`allergene`),
  CONSTRAINT `fk_allergene` FOREIGN KEY (`allergene`) REFERENCES `allergene` (`nome`) ON UPDATE CASCADE,
  CONSTRAINT `fk_idPiatto` FOREIGN KEY (`idPiatto`) REFERENCES `piatto` (`idpiatto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Contiene`
--

LOCK TABLES `Contiene` WRITE;
/*!40000 ALTER TABLE `Contiene` DISABLE KEYS */;
INSERT INTO `Contiene` VALUES (11,'arachidi'),(2,'frutta_a_guscio'),(1,'glutine'),(2,'glutine'),(3,'glutine'),(4,'glutine'),(6,'glutine'),(11,'glutine'),(12,'glutine'),(3,'latte'),(6,'latte'),(11,'latte'),(13,'latte'),(6,'pesce'),(4,'sedano'),(13,'soia'),(4,'uova'),(6,'uova'),(7,'uova'),(11,'uova'),(12,'uova');
/*!40000 ALTER TABLE `Contiene` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Persona`
--

DROP TABLE IF EXISTS `Persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Persona` (
  `idPersona` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `cognome` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `telefono` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `cellulare` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `via` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cap` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `citta` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `note` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `tipo` enum('studente','docente') COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`idPersona`)
) ENGINE=InnoDB AUTO_INCREMENT=681 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Persona`
--

LOCK TABLES `Persona` WRITE;
/*!40000 ALTER TABLE `Persona` DISABLE KEYS */;
INSERT INTO `Persona` VALUES (1,'nome1','cognome1','01010101','01010101','via prova 1','01100','città1','nota1, nota2','studente'),(2,'nome2','cognome2',NULL,'316729','via prova 2','45261','città2','nota','docente'),(679,'giulia','lorini','031610625','3496646557','via 3 ponti, 7','22036','erba','nota, nota2','studente'),(680,'nome','cognome',NULL,'4532','via fwd 4','54332','città','','docente');
/*!40000 ALTER TABLE `Persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Piatto`
--

DROP TABLE IF EXISTS `Piatto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Piatto` (
  `idPiatto` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` enum('primo','secondo','dolce','frutta') COLLATE utf8_bin DEFAULT NULL,
  `nome` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`idPiatto`),
  UNIQUE KEY `nome_UNIQUE` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Piatto`
--

LOCK TABLES `Piatto` WRITE;
/*!40000 ALTER TABLE `Piatto` DISABLE KEYS */;
INSERT INTO `Piatto` VALUES (1,'primo','Pasta al pomodoro'),(2,'primo','Pasta al pesto'),(3,'primo','Riso olio e parmigiano'),(4,'primo','Lasagne alla bolognese'),(5,'secondo','Petto di pollo con zucchine'),(6,'secondo','Bastoncini di pesce con puré di patate'),(7,'secondo','Frittata con carote'),(8,'secondo','Bresaola al limone con spinaci'),(9,'frutta','Macedonia'),(10,'frutta','Frutta di stagione'),(11,'dolce','Budino al cioccolato'),(12,'dolce','Crostata ai frutti di bosco'),(13,'dolce','Gelato');
/*!40000 ALTER TABLE `Piatto` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-10 22:58:21
