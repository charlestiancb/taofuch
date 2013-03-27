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
-- Table structure for table `system_group`
--

DROP TABLE IF EXISTS `system_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_group` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(200) NOT NULL COMMENT '分组的名称',
  `descript` text COMMENT '对该分组信息的描述.',
  `order_num` int(11) NOT NULL DEFAULT '1' COMMENT '显示时的排序.',
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `group_id_UNIQUE` (`group_id`),
  UNIQUE KEY `group_name_UNIQUE` (`group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='分组信息。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_group`
--

LOCK TABLES `system_group` WRITE;
/*!40000 ALTER TABLE `system_group` DISABLE KEYS */;
INSERT INTO `system_group` VALUES (1,'默认','默认就是默认',1),(3,'SOA','SOA的各个bundle的关系',2);
/*!40000 ALTER TABLE `system_group` ENABLE KEYS */;
UNLOCK TABLES;

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
  `group_id` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`sys_id`),
  UNIQUE KEY `sys_id_UNIQUE` (`sys_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8 COMMENT='系统信息，包括系统名称、系统网址和简介等基本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_info`
--

LOCK TABLES `system_info` WRITE;
/*!40000 ALTER TABLE `system_info` DISABLE KEYS */;
INSERT INTO `system_info` VALUES (20,'ABIZ-SITE','http://www.abiz.com','<b>百卓网站点（本地发布应用）</b>',1,1),(21,'ABIZ-OSS','http://abiz-oss.vemic.com','<b>百卓后台业务支持系统（本地发布应用）</b>',3,1),(22,'ABIZ-PTL','http://abiz-ptl.vemic.com','<b>所有后台的门户及权限系统（本地发布应用）</b>',5,1),(23,'ABIZ-CCS','http://abiz-ccs.vemic.com','<b>后台公共系统，提供列表工具等功能（本地发布应用）</b>',4,1),(24,'ABIZ-CMS','http://www.abiz.com/info','<b>这是一个独立应用，是内容管理系统，用于发布一些资讯类信息。（本地发布应用）</b>',2,1),(25,'ABIZ-PAY','http://pay.abiz.com','<b>支付平台。主要是结合第三方的一些支付等操作，它只是简化连接银联等支付平台的功能。（本地发布应用）</b>',6,1),(26,'ABIZ-WEBASSETS','http://static.abiz.com','<b>项目名叫：webassets。也就是静态资源服务，用于对静态资源作相应处理，以提高web访问的性能。（本地发布应用）</b>',7,1),(27,'SOA','http://open.abiz.com','<b>它是提供SOA组件服务的应用集合。（本地发布应用）</b>',8,1),(28,'MICCN-VO','http://vocn.vemic.com:9080','<b>MICCN的VO应用。（本地发布应用）</b>',9,1),(29,'MICCN-OSS','http://miccn-oss.vemic.com','<b>MICCN的后台业务支持系统。（本地发布应用）</b>',10,1),(30,'MICCN-ORDER','','<b>此次中英文分离新做的后台订单系统，该系统在后期的维护过程中可能会被消除掉。（新应用）（本地发布应用）</b>',11,1),(31,'短信平台','','运维负责的对外发送短信的平台',45,1),(32,'邮件服务器','','运维负责的对外发送邮件的平台',46,1),(33,'MIC英文版VO','member.made-in-china.com','MIC英文版VO',21,1),(34,'TradeMessager','','TM聊天工具',44,1),(35,'台塑（台湾）','','百卓大买家资源的来源',48,1),(36,'搜索服务','','提供全文搜索的应用。',47,1),(37,'MIC编辑系统','edt.vemic.com','原来支持中英文的编辑系统，现在已经划归MIC英文项目组',41,1),(38,'MIC客服系统','http://cst.vemic.com','原来支持中英文的客服系统，现在已经划归MIC英文项目组',42,1),(39,'支付平台（银联，财付通）','','如财付通、银联等',49,1),(40,'MIC销售与订单系统','http://sales.vemic.com','MIC销售系统兼订单系统',43,1),(41,'MIC_VO_CN','','<b>MICCN远程调用，日终服务（本地发布应用）</b>',12,1),(42,'商聚园','http://www.shangjuyuan.com','商聚园网站',30,1),(43,'MICCN','','<b>MIC中文站（本地发布应用）</b>',13,1),(44,'MICCN-ZHANSHITING','','<b>MICCN展示厅（本地发布应用）</b>',14,1),(45,'MICCN-REGION-TAIWAN','','<b>MIC中文站台湾专区（本地发布应用）</b>',16,1),(46,'MICCN-CHANNEL','','<b>MICCN地区频道（本地发布应用）</b>',15,1),(47,'MICCN-IMAGE','','<b>MIC中文站图片应用（本地发布应用）</b>',17,1),(48,'MICCN-CUBE','','<b>MIC中文站-生意大盘（数据罗盘）（本地发布应用）</b>',19,1),(49,'百分百物流','http://www.bfb56.com','百分百物流网',31,1),(52,'爱聘才','http://www.ipincai.com','爱聘才网站',34,1),(53,'领动在线','http://www.leadong.com','领动在线saas',35,1),(54,'新一站','http://www.xyz.cn','新一站保险网',36,1);
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
  UNIQUE KEY `rec_id_UNIQUE` (`rec_id`),
  UNIQUE KEY `msid_unique` (`mid`,`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=645 DEFAULT CHARSET=utf8 COMMENT='系统之间的调用关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_relationship`
--

LOCK TABLES `system_relationship` WRITE;
/*!40000 ALTER TABLE `system_relationship` DISABLE KEYS */;
INSERT INTO `system_relationship` VALUES (572,20,24,'http请求'),(573,20,25,'xmlrpc方式'),(574,20,26,'http请求'),(575,20,27,'http请求'),(576,20,28,'xmlrpc方式'),(577,20,33,'xmlrpc'),(578,20,34,'页面js与xmlrpc'),(579,20,31,'WebService方式'),(580,20,32,NULL),(581,20,36,'xmlrpc'),(582,20,35,'xmlrpc'),(584,21,23,'http请求的方式'),(585,21,22,'hessian'),(586,21,25,'xmlrpc'),(587,21,27,'http请求'),(588,21,28,'xmlrpc'),(589,21,29,'xmlrpc'),(590,21,37,'只是配置'),(591,21,38,'只是配置'),(592,21,34,'页面js与xmlrpc'),(593,21,31,'WebService方式'),(594,21,32,NULL),(595,21,36,'xmlrpc'),(596,21,35,'xmlrpc'),(597,23,21,'xmlrpc'),(598,23,22,'hessian'),(599,23,29,'xmlrpc（获取列表数据）'),(600,23,30,'xmlrpc(获取列表数据)'),(601,23,36,'xmlrpc'),(602,22,21,'菜单链接指向与首页工作区配置'),(603,22,23,'hessian，菜单链接指向'),(604,22,29,'xmlrpc，菜单链接指向'),(605,22,30,'xmlrpc，菜单链接指向'),(606,25,31,'WebService方式'),(607,25,32,NULL),(608,25,39,'http方式请求'),(609,27,31,'WebService方式'),(610,27,32,NULL),(611,27,36,'xmlrpc'),(612,29,23,'hessian，http请求方式'),(613,29,22,'hessian'),(614,29,37,'链接指向'),(615,29,38,'xmlrpc'),(616,29,40,'xmlrpc'),(617,29,31,'WebService方式'),(618,29,32,NULL),(619,29,36,'xmlrpc'),(620,30,23,'hessian,http请求方式'),(621,30,22,'hessian'),(622,30,40,'xmlrpc'),(623,35,20,'xmlrpc方式'),(624,56,20,'adsasdfasd'),(628,24,56,'无adfadfadf'),(629,56,28,'这是一个测试数据'),(631,20,41,'xmlrpc'),(633,53,41,'xmlrpc'),(634,49,41,'xmlrpc'),(635,52,41,'xmlrpc'),(636,42,41,'xmlrpc'),(637,54,41,'xmlrpc'),(638,34,41,'xmlrpc'),(641,24,20,'页面数据流引用和链接指向'),(642,43,34,'tmxmlrpc'),(643,20,43,'xmlrpc'),(644,34,28,'xmlrpc');
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

-- Dump completed on 2013-03-27 19:03:58
