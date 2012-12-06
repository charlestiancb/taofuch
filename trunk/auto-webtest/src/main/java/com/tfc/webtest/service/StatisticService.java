package com.tfc.webtest.service;

import com.tfc.webtest.entity.DataCase;
import com.tfc.webtest.entity.STATUS;

/**
 * 用于统计的服务
 * 
 * @author taofucheng
 * 
 */
public interface StatisticService {
	/**
	 * 失败的用例
	 * 
	 * @param _case
	 * @param notExists
	 * @param string
	 */
	public void fail(DataCase _case, STATUS notExists, String string);

	/**
	 * 指定的用例对应的所有的数据驱动都是失败的！
	 * 
	 * @param caseId
	 * @param notExists
	 * @param string
	 */
	public void fail(String caseId, STATUS notExists, String string);

	/**
	 * 成功！
	 * 
	 * @param _case
	 */
	public void succ(DataCase _case);

}
