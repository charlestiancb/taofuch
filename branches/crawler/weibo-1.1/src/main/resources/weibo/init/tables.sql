CREATE DATABASE  IF NOT EXISTS `weibo` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `weibo`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: weibo
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
-- Table structure for table `failed_request_url`
--

DROP TABLE IF EXISTS `failed_request_url`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `failed_request_url` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(2000) NOT NULL COMMENT '失败的链接',
  `failed_node` varchar(100) DEFAULT NULL COMMENT '说明是在哪个过程节点上出现的错误,用于下次继续从这个点恢复~',
  `failed_reason` varchar(4000) DEFAULT NULL COMMENT '记录请求失败的原因！',
  PRIMARY KEY (`rec_id`),
  UNIQUE KEY `rec_id_UNIQUE` (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='请求失败的链接！';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fetch_info`
--

DROP TABLE IF EXISTS `fetch_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fetch_info` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `query_str` varchar(500) DEFAULT NULL COMMENT '具体的查询词',
  `relation_id` varchar(200) DEFAULT NULL COMMENT '对应的内容ID，如微博ID。',
  `relation_type` varchar(100) DEFAULT NULL COMMENT '具体的相关类型，如微博或用户等',
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB AUTO_INCREMENT=927 DEFAULT CHARSET=utf8 COMMENT='抓取的信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` varchar(100) NOT NULL COMMENT '用户编号',
  `NAME` varchar(500) DEFAULT NULL COMMENT '用户姓名',
  `url` varchar(1000) DEFAULT NULL COMMENT '用户的微博主页',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `introduce` text COMMENT '简介',
  `addr` text COMMENT '所在地',
  `blog` varchar(500) DEFAULT NULL COMMENT '博客',
  `email` varchar(1000) DEFAULT NULL COMMENT '邮箱',
  `company` text COMMENT '公司',
  `university` text COMMENT '大学及专业',
  `birthday` varchar(100) DEFAULT NULL COMMENT '生日',
  `tag_info` text DEFAULT NULL COMMENT '用户标签信息',
  `follow_num` int(10) DEFAULT NULL COMMENT '关注数',
  `fans_num` int(10) DEFAULT NULL COMMENT '粉丝数',
  `weibo_num` int(10) DEFAULT NULL COMMENT '发布微博数',
  `has_relation` varchar(2) DEFAULT '0' COMMENT '是否已经抓取过用户关系信息，如粉丝、关注等。',
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_fans`
--

DROP TABLE IF EXISTS `user_fans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_fans` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录编号',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户编号',
  `fans_id` varchar(100) DEFAULT NULL COMMENT '用户的粉丝编号',
  PRIMARY KEY (`rec_id`),
  UNIQUE KEY `fans` (`user_id`,`fans_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户的粉丝表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_follow`
--

DROP TABLE IF EXISTS `user_follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_follow` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录编号',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户编号',
  `follow_id` varchar(100) DEFAULT NULL COMMENT '被关注者',
  PRIMARY KEY (`rec_id`),
  UNIQUE KEY `follow` (`user_id`,`follow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户关注的用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `weibo_comment`
--

DROP TABLE IF EXISTS `weibo_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weibo_comment` (
  `comment_id` varchar(100) NOT NULL COMMENT '编号',
  `weibo_id` varchar(100) DEFAULT NULL COMMENT '对应的微博',
  `content` varchar(2000) DEFAULT NULL COMMENT '评论内容',
  `publish_time` varchar(100) DEFAULT NULL COMMENT '评论发布时间',
  `user_id` varchar(100) DEFAULT NULL COMMENT '评论者',
  PRIMARY KEY (`comment_id`),
  UNIQUE KEY `comment_id` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微博评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `weibo_info`
--

DROP TABLE IF EXISTS `weibo_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weibo_info` (
  `weibo_id` varchar(100) NOT NULL COMMENT '微博编号',
  `content` varchar(2000) DEFAULT NULL COMMENT '内容',
  `url` varchar(255) DEFAULT NULL COMMENT '微博的URL',
  `orign` varchar(100) DEFAULT NULL COMMENT '微博来源',
  `forword_num` int(10) DEFAULT NULL COMMENT '转发数',
  `comment_num` int(10) DEFAULT NULL COMMENT '评论数',
  `publish_time` varchar(100) DEFAULT NULL COMMENT '微博发布时间',
  `user_id` varchar(100) DEFAULT NULL COMMENT '微博发布者',
  `has_comment` varchar(2) DEFAULT '0' COMMENT '是否已经抓取评论信息',
  PRIMARY KEY (`weibo_id`),
  UNIQUE KEY `weibo_id` (`weibo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-03-01  9:07:00
