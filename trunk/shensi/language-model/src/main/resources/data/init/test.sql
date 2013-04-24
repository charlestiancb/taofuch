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
-- Table structure for table `informationscience`
--

DROP TABLE IF EXISTS `informationscience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `informationscience` (
  `id` int(11) NOT NULL DEFAULT '0',
  `Title` varchar(200) CHARACTER SET gbk NOT NULL,
  `author` varchar(50) CHARACTER SET gbk DEFAULT NULL,
  `Abstract` text CHARACTER SET gbk NOT NULL,
  `keywords` varchar(200) CHARACTER SET gbk DEFAULT '',
  `JournalName` varchar(15) CHARACTER SET gbk DEFAULT NULL,
  `year` int(5) DEFAULT NULL,
  `temporaltoken` varchar(150) CHARACTER SET gbk DEFAULT '',
  `vol` int(2) DEFAULT NULL,
  `TitleToken` varchar(400) CHARACTER SET gbk DEFAULT NULL,
  `AbstractToken` text CHARACTER SET gbk,
  `topicToken` varchar(10) CHARACTER SET gbk DEFAULT NULL,
  `citedsum` int(10) DEFAULT NULL,
  `downsum` int(10) DEFAULT NULL,
  `c1980` text CHARACTER SET gbk,
  `c1981` text CHARACTER SET gbk,
  `c1982` text CHARACTER SET gbk,
  `c1983` text CHARACTER SET gbk,
  `c1984` text CHARACTER SET gbk,
  `c1985` text CHARACTER SET gbk,
  `c1986` text CHARACTER SET gbk,
  `c1987` text CHARACTER SET gbk,
  `c1989` text CHARACTER SET gbk,
  `c1990` text CHARACTER SET gbk,
  `c1991` text CHARACTER SET gbk,
  `c1992` text CHARACTER SET gbk,
  `c1993` text CHARACTER SET gbk,
  `c1994` text CHARACTER SET gbk,
  `c1995` text CHARACTER SET gbk,
  `c1996` text CHARACTER SET gbk,
  `c1997` text CHARACTER SET gbk,
  `c1998` text CHARACTER SET gbk,
  `c1999` text CHARACTER SET gbk,
  `c2000` text CHARACTER SET gbk,
  `c2001` text CHARACTER SET gbk,
  `c2002` text CHARACTER SET gbk,
  `c2003` text CHARACTER SET gbk,
  `c2004` text CHARACTER SET gbk,
  `c2005` text CHARACTER SET gbk,
  `c2006` text CHARACTER SET gbk,
  `c2007` text CHARACTER SET gbk,
  `c2008` text CHARACTER SET gbk,
  `c2009` text CHARACTER SET gbk,
  `c2010` text CHARACTER SET gbk,
  `c2011` text CHARACTER SET gbk,
  `c2012` text CHARACTER SET gbk,
  `c1988` text CHARACTER SET gbk,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `informationscience`
--

LOCK TABLES `informationscience` WRITE;
/*!40000 ALTER TABLE `informationscience` DISABLE KEYS */;
INSERT INTO `informationscience` VALUES (1,'建设第三空间 强化社会参与','刘锦山； 吴建中；','Shipment of gold damaged in a fire','图书馆事业； 上海世博会； 第三空间； 社会参与；','高校图书馆工作',2012,'未来 ---> ',1,NULL,NULL,NULL,3,104,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'引证文献数量:3|详细信息:2012	3	 袁红军. ?第三空间的图书馆服务能力研究[J] 现代情报 2012(11)； 袁红军. ?档案馆第三文化空间探析[J] 浙江档案 2012(10)； 李秀娟. ?作为第三空间图书馆社会价值的实现[J] 公共图书馆 2012(03)',NULL),(2,'图书馆战略研究知行合一的演绎者——吴建中先生学术研究概览','陈有志；','Delivery of silver arrived in a silver truck','吴建中； 管理思想； 战略管理； 世博会； 社会参与；','高校图书馆工作',2012,'未来 ---> ',1,NULL,NULL,NULL,3,41,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'基于学科论的图书馆学结构与学科体系研究','王宏鑫；','Shipment of gold arrived in a truck','图书馆学结构； 图书馆学学科体系； 图书馆学学科论；','高校图书馆工作',2012,'2012',1,NULL,NULL,NULL,3,38,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `informationscience` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-04-24 21:15:15
