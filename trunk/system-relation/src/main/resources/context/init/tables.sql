CREATE DATABASE  IF NOT EXISTS `relationship` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `relationship`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: relationship
-- ------------------------------------------------------
-- Server version	5.5.20

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
-- Table structure for table `system_info`
--

DROP TABLE IF EXISTS `system_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_info` (
  `sys_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '系统的名称',
  `url` varchar(1000) DEFAULT NULL COMMENT '系统的首页URL',
  `introduce` text COMMENT '系统简介信息',
  `order_num` int(11) NOT NULL DEFAULT '0' COMMENT '指定排序的字段',
  PRIMARY KEY (`sys_id`),
  UNIQUE KEY `sys_id_UNIQUE` (`sys_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统信息，包括系统名称、系统网址和简介等基本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_info`
--

LOCK TABLES `system_info` WRITE;
/*!40000 ALTER TABLE `system_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_relationship`
--

DROP TABLE IF EXISTS `system_relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_relationship` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长的记录id',
  `mid` bigint(20) NOT NULL COMMENT '发起调用的系统id.全称为:master_id',
  `sid` bigint(20) NOT NULL COMMENT '被调用的系统id,全称:slave_id',
  `introduce` text COMMENT '具体的关系说明，如对于调用方式的说明可以是xmlrpc,hessian,http请求等.',
  PRIMARY KEY (`rec_id`),
  UNIQUE KEY `rec_id_UNIQUE` (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统之间的调用关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_relationship`
--

LOCK TABLES `system_relationship` WRITE;
/*!40000 ALTER TABLE `system_relationship` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_relationship` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-03-14 10:50:31
