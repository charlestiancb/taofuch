package com.tfc.webtest.entity;

import java.io.Serializable;

/**
 * 用例完整的一行信息
 * 
 * @author taofucheng
 * 
 */
public class CaseLine implements Serializable {
	private static final long serialVersionUID = -7970468587550462746L;
	private String operName;
	private String pageElement;
	private String elementLocation;
	private String content;
	private String var;

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getPageElement() {
		return pageElement;
	}

	public void setPageElement(String pageElement) {
		this.pageElement = pageElement;
	}

	public String getElementLocation() {
		return elementLocation;
	}

	public void setElementLocation(String elementLocation) {
		this.elementLocation = elementLocation;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
}
