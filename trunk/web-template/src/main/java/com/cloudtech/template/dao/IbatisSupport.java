package com.cloudtech.template.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Ibatis操作的支持
 * 
 * @author taofucheng
 * 
 */
public abstract class IbatisSupport {
	protected Log log = LogFactory.getLog("log");
	@Autowired
	protected SqlMapClient sqlMapClient;

}
