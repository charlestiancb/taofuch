CREATE DATABASE  IF NOT EXISTS `infordb` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `infordb`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: infordb
-- ------------------------------------------------------
-- Server version	5.1.46-community

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
-- Table structure for table `word_tf_idf`
--

DROP TABLE IF EXISTS `word_tf_idf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `word_tf_idf` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `document_title` varchar(500) COLLATE utf8_bin NOT NULL,
  `word_id` bigint(20) NOT NULL,
  `tf` int(11) NOT NULL,
  `tf_idf` double DEFAULT NULL,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='文档中每个词对应的tf和tf/idf值';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `word_tf_idf`
--

LOCK TABLES `word_tf_idf` WRITE;
/*!40000 ALTER TABLE `word_tf_idf` DISABLE KEYS */;
/*!40000 ALTER TABLE `word_tf_idf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `word_idf`
--

DROP TABLE IF EXISTS `word_idf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `word_idf` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `word` varchar(100) COLLATE utf8_bin NOT NULL,
  `df` INT DEFAULT NULL,
  `idf` double DEFAULT NULL,
  PRIMARY KEY (`rec_id`),
  UNIQUE KEY `word_UNIQUE` (`word`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='单个词表及其idf值';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `word_idf`
--

LOCK TABLES `word_idf` WRITE;
/*!40000 ALTER TABLE `word_idf` DISABLE KEYS */;
/*!40000 ALTER TABLE `word_idf` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-04-24 20:57:57
