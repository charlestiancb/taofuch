package com.tfc.webtest.entity;

import java.io.Serializable;

/**
 * 由数据定义而组成的可执行的整个用例
 * 
 * @author taofucheng
 * 
 */
public class DataCase extends Case implements Serializable {
	private static final long serialVersionUID = -8088397676530624113L;
	private String dataId;

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
}
