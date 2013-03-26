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
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='系统信息，包括系统名称、系统网址和简介等基本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_info`
--

LOCK TABLES `system_info` WRITE;
/*!40000 ALTER TABLE `system_info` DISABLE KEYS */;
INSERT INTO `system_info` VALUES (20,'ABIZ-SITE','http://www.abiz.com','百卓网站点（本地发布应用）',1),(21,'ABIZ-OSS','http://abiz-oss.vemic.com','百卓后台业务支持系统（本地发布应用）',3),(22,'ABIZ-PTL','http://abiz-ptl.vemic.com','所有后台的门户及权限系统（本地发布应用）',5),(23,'ABIZ-CCS','http://abiz-ccs.vemic.com','后台公共系统，提供列表工具等功能（本地发布应用）',4),(24,'ABIZ-CMS','http://www.abiz.com/info','这是一个独立应用，是内容管理系统，用于发布一些资讯类信息。（本地发布应用）',2),(25,'ABIZ-PAY','http://pay.abiz.com','支付平台。主要是结合第三方的一些支付等操作，它只是简化连接银联等支付平台的功能。（本地发布应用）',6),(26,'ABIZ-WEBASSETS','http://static.abiz.com','项目名叫：webassets。也就是静态资源服务，用于对静态资源作相应处理，以提高web访问的性能。（本地发布应用）',7),(27,'SOA','http://open.abiz.com','它是提供SOA组件服务的应用集合。（本地发布应用）',8),(28,'MICCN-VO','http://vocn.vemic.com:9080','MICCN的VO应用。（本地发布应用）',9),(29,'MICCN-OSS','http://miccn-oss.vemic.com','MICCN的后台业务支持系统。（本地发布应用）',10),(30,'MICCN-ORDER','','此次中英文分离新做的后台订单系统，该系统在后期的维护过程中可能会被消除掉。（新应用）（本地发布应用）',11),(31,'短信平台','','运维负责的对外发送短信的平台',22),(32,'邮件服务器','','运维负责的对外发送邮件的平台',13),(33,'MIC英文版VO','member.made-in-china.com','MIC英文版VO',14),(34,'TM','','TM聊天工具',15),(35,'台塑（台湾）','','百卓大买家资源的来源',16),(36,'搜索服务','','提供全文搜索的应用。',17),(37,'MIC编辑系统','edt.vemic.com','原来支持中英文的编辑系统，现在已经划归MIC英文项目组',18),(38,'MIC客服系统','http://cst.vemic.com','原来支持中英文的客服系统，现在已经划归MIC英文项目组',19),(39,'第三方支付平台','','如财付通、银联等',20),(40,'SALES','sales.vemic.com','MIC销售系统兼订单系统',21),(41,'MIC_VO_CN','','MICCN远程调用，日终服务（本地发布应用）',12),(42,'商聚园','http://www.shangjuyuan.com','商聚园网站',23);
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
) ENGINE=InnoDB AUTO_INCREMENT=522 DEFAULT CHARSET=utf8 COMMENT='系统之间的调用关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_relationship`
--

LOCK TABLES `system_relationship` WRITE;
/*!40000 ALTER TABLE `system_relationship` DISABLE KEYS */;
INSERT INTO `system_relationship` VALUES (474,20,24,'http请求'),(475,20,25,'xmlrpc方式'),(476,20,26,'http请求'),(477,20,27,'http请求'),(478,20,28,'xmlrpc方式'),(479,20,31,'WebService方式'),(480,20,32,NULL),(481,20,33,'xmlrpc'),(482,20,34,'页面js与xmlrpc'),(483,20,35,'xmlrpc'),(484,20,36,'xmlrpc'),(485,21,22,'hessian'),(486,21,23,'http请求的方式'),(487,21,25,'xmlrpc'),(488,21,27,'http请求'),(489,21,28,'xmlrpc'),(490,21,29,'xmlrpc'),(491,21,31,'WebService方式'),(492,21,32,NULL),(493,21,34,'页面js与xmlrpc'),(494,21,35,'xmlrpc'),(495,21,36,'xmlrpc'),(496,21,37,'只是配置'),(497,21,38,'只是配置'),(498,22,21,'菜单链接指向与首页工作区配置'),(499,22,23,'hessian，菜单链接指向'),(500,22,29,'xmlrpc，菜单链接指向'),(501,23,21,'xmlrpc'),(502,23,22,'hessian'),(503,23,29,'xmlrpc（获取列表数据）'),(504,23,36,'xmlrpc'),(505,24,20,'链接引用'),(506,25,31,'WebService方式'),(507,25,32,NULL),(508,25,39,'http方式请求'),(509,27,31,'WebService方式'),(510,27,32,NULL),(511,27,36,'xmlrpc'),(512,29,22,'hessian'),(513,29,23,'hessian，http请求方式'),(514,29,31,'WebService方式'),(515,29,32,NULL),(516,29,36,'xmlrpc'),(517,29,37,'链接指向'),(518,29,38,'xmlrpc'),(519,29,40,'xmlrpc'),(520,30,40,'xmlrpc'),(521,35,20,'xmlrpc方式');
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

-- Dump completed on 2013-03-26  9:50:55
