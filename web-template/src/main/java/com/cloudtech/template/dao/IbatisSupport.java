package com.cloudtech.template.dao;

import org.springframework.beans.factory.annotation.Autowired;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Ibatis操作的支持
 * 
 * @author taofucheng
 * 
 */
public abstract class IbatisSupport {
	@Autowired
	protected SqlMapClient sqlMapClient;
}
