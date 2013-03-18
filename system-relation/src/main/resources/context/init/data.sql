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
  PRIMARY KEY (`sys_id`),
  UNIQUE KEY `sys_id_UNIQUE` (`sys_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='系统信息，包括系统名称、系统网址和简介等基本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_info`
--

LOCK TABLES `system_info` WRITE;
/*!40000 ALTER TABLE `system_info` DISABLE KEYS */;
INSERT INTO `system_info` VALUES (20,'ABIZ-SITE','www.abiz.com','百卓网'),(21,'ABIZ-OSS','abiz-oss.vemic.com','百卓后台业务支持系统'),(22,'ABIZ-PTL','abiz-ptl.vemic.com','所有后台的门户及权限系统'),(23,'ABIZ-CCS','abiz-ccs.vemic.com','后台公共系统，提供列表工具等功能'),(24,'ABIZ-CMS','www.abiz.com/info','这是一个独立应用，是内容管理系统，用于发布一些资讯类信息。'),(25,'ABIZ-PAY','pay.abiz.com','支付平台。主要是结合第三方的一些支付等操作，它只是简化连接银联等支付平台的功能。'),(26,'静态资源服务器','static.abiz.com','项目名叫：webassets。用于对静态资源作相应处理，以提高web访问的性能。'),(27,'SOA','http://open.abiz.com','它是提供SOA组件服务的应用集合。'),(28,'MICCN-VO','vocn.vemic.com:9080','MICCN的VO应用。'),(29,'MICCN-OSS','miccn-oss.vemic.com','miccn的后台业务支持系统。'),(30,'miccn-order','','订单系统，该系统在后期的维护过程中可能会被消除掉。'),(31,'短信平台','','运维负责的对外发送短信的平台'),(32,'邮件服务器','','运维负责的对外发送邮件的平台'),(33,'MIC英文版VO','member.made-in-china.com','MIC英文版VO'),(34,'TM','','TM聊天工具'),(35,'台塑（台湾）','','百卓大买家资源的来源'),(36,'搜索服务','','提供全文搜索的应用。'),(37,'MIC编辑系统','edt.vemic.com','原来支持中英文的编辑系统，现在已经划归MIC英文项目组'),(38,'MIC客服系统','http://cst.vemic.com','原来支持中英文的客服系统，现在已经划归MIC英文项目组'),(39,'第三方支付平台','','如财付通、银联等'),(40,'SALES','sales.vemic.com','MIC销售系统兼订单系统');
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
) ENGINE=InnoDB AUTO_INCREMENT=380 DEFAULT CHARSET=utf8 COMMENT='系统之间的调用关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_relationship`
--

LOCK TABLES `system_relationship` WRITE;
/*!40000 ALTER TABLE `system_relationship` DISABLE KEYS */;
INSERT INTO `system_relationship` VALUES (333,20,24,'http请求'),(334,20,25,'xmlrpc方式'),(335,20,26,'http请求'),(336,20,27,'http请求'),(337,20,28,'xmlrpc方式'),(338,20,31,'WebService方式'),(339,20,32,NULL),(340,20,33,'xmlrpc'),(341,20,34,'页面js与xmlrpc'),(342,20,35,'xmlrpc'),(343,20,36,'xmlrpc'),(344,21,22,'hessian'),(345,21,23,'http请求的方式'),(346,21,25,'xmlrpc'),(347,21,27,'http请求'),(348,21,28,'xmlrpc'),(349,21,29,'xmlrpc'),(350,21,31,'WebService方式'),(351,21,32,NULL),(352,21,34,'页面js与xmlrpc'),(353,21,35,'xmlrpc'),(354,21,36,'xmlrpc'),(355,21,37,'只是配置'),(356,21,38,'只是配置'),(357,22,21,'菜单链接指向与首页工作区配置'),(358,22,23,'hessian，菜单链接指向'),(359,22,29,'xmlrpc，菜单链接指向'),(360,23,21,'xmlrpc'),(361,23,22,'hessian'),(362,23,29,'xmlrpc（获取列表数据）'),(363,23,36,'xmlrpc'),(364,24,20,'链接引用'),(365,25,31,'WebService方式'),(366,25,32,NULL),(367,25,39,'http方式请求'),(368,27,31,'WebService方式'),(369,27,32,NULL),(370,27,36,'xmlrpc'),(371,29,22,'hessian'),(372,29,23,'hessian，http请求方式'),(373,29,31,'WebService方式'),(374,29,32,NULL),(375,29,36,'xmlrpc'),(376,29,37,'链接指向'),(377,29,38,'xmlrpc'),(378,29,40,'xmlrpc'),(379,35,20,'xmlrpc方式');
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

-- Dump completed on 2013-03-18 16:13:52
