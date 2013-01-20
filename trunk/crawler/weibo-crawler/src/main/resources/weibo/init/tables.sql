/*
MySQL Data Transfer
Source Host: localhost
Source Database: weibo
Target Host: localhost
Target Database: weibo
Date: 2012-11-26 20:14:45
*/

SET FOREIGN_KEY_CHECKS=0;

create database weibo;
use weibo;
-- ----------------------------
-- Table structure for fetch_info
-- ----------------------------
CREATE TABLE `fetch_info` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `query_str` varchar(500) DEFAULT NULL COMMENT '具体的查询词',
  `relation_id` varchar(200) DEFAULT NULL COMMENT '对应的内容ID，如微博ID。',
  `relation_type` varchar(100) DEFAULT NULL COMMENT '具体的相关类型，如微博或用户等',
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '抓取的信息';
-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE `user` (
  `user_id` varchar(100) NOT NULL COMMENT '用户编号',
  `NAME` varchar(100) DEFAULT NULL COMMENT '用户姓名',
  `url` varchar(1000) DEFAULT NULL COMMENT '用户的微博主页',
  `info` varchar(1000) DEFAULT NULL COMMENT '相关信息，如：公司、地址等',
  `favor` varchar(500) DEFAULT NULL COMMENT '兴趣爱好',
  `introduce` varchar(500) DEFAULT NULL COMMENT '简介',
  `follow_num` int(10) DEFAULT NULL COMMENT '关注数',
  `fans_num` int(10) DEFAULT NULL COMMENT '粉丝数',
  `weibo_num` int(10) DEFAULT NULL COMMENT '发布微博数',
  `tag_info` varchar(300) DEFAULT NULL COMMENT '用户标签信息',
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for user_fans
-- ----------------------------
CREATE TABLE `user_fans` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录编号',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户编号',
  `fans_id` varchar(100) DEFAULT NULL COMMENT '用户的粉丝编号',
  PRIMARY KEY (`rec_id`),
  UNIQUE KEY `fans` (`user_id`,`fans_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户的粉丝表';

-- ----------------------------
-- Table structure for user_follow
-- ----------------------------
CREATE TABLE `user_follow` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录编号',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户编号',
  `follow_id` varchar(100) DEFAULT NULL COMMENT '被关注者',
  PRIMARY KEY (`rec_id`),
  UNIQUE KEY `follow` (`user_id`,`follow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户关注的用户';

-- ----------------------------
-- Table structure for weibo_comment
-- ----------------------------
CREATE TABLE `weibo_comment` (
  `comment_id` varchar(100) NOT NULL COMMENT '编号',
  `weibo_id` varchar(100) DEFAULT NULL COMMENT '对应的微博',
  `content` varchar(2000) DEFAULT NULL COMMENT '评论内容',
  `publish_time` varchar(100) DEFAULT NULL COMMENT '评论发布时间',
  `user_id` varchar(100) DEFAULT NULL COMMENT '评论者',
  PRIMARY KEY (`comment_id`),
  UNIQUE KEY `comment_id` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微博评论表';

-- ----------------------------
-- Table structure for weibo_info
-- ----------------------------
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

CREATE  TABLE `weibo`.`failed_request_url` (
  `rec_id` BIGINT NOT NULL AUTO_INCREMENT ,
  `url` VARCHAR(2000) NOT NULL COMMENT '失败的链接' ,
  `failed_node` VARCHAR(100) NULL COMMENT '说明是在哪个过程节点上出现的错误,用于下次继续从这个点恢复~' ,
  `failed_reason` VARCHAR(4000) NULL COMMENT '记录请求失败的原因！' ,
  PRIMARY KEY (`rec_id`) ,
  UNIQUE INDEX `rec_id_UNIQUE` (`rec_id` ASC)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '请求失败的链接！';

-- ----------------------------
-- Records 
-- ----------------------------