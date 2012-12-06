package com.tfc.webtest.entity;

/**
 * 用例执行结果状态
 * 
 * @author taofucheng
 * 
 */
public enum STATUS {
	/** 不存在的数据定义 */
	NOT_EXISTS,
	/** 运行过程中发生异常 */
	ERROR,
	/** 用例执行结果为不通知 */
	FAIL,
	/** 用例执行通过 */
	SUCC
}