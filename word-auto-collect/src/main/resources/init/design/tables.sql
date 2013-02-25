SET FOREIGN_KEY_CHECKS=0;

create database words;
use words;

CREATE TABLE `word_base` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `word` varchar(500) NOT NULL COMMENT '具体的词语内容',
  `special_type` varchar(20) DEFAULT '0' COMMENT '专用类型，如学科分类。0：通用类型。',
  `check_status` varchar(20) DEFAULT '0' COMMENT '审核状态，是否已经被审核通过。0：未审核；1：审核通过',
  `stat_num` bigint(20) DEFAULT 0 COMMENT '自动学习时，统计得到的数字',
  `add_time` DATETIME DEFAULT now() COMMENT '该词加入到表中的时间',
  `checked_time` DATETIME DEFAULT NULL COMMENT '该词审核通过的时间',
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '词语的基本信息';

CREATE TABLE `fetch_orig` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orig` varchar(5000) NOT NULL COMMENT '具体的来源，如URL或文件路径与名称',
  `add_time` DATETIME DEFAULT now() COMMENT '该url加入到表中的时间，即抓取解析的时间',
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '解析词的来源信息，如记录从哪儿获取的信息，防止重复读取！';