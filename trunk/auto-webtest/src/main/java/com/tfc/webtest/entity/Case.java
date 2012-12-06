package com.tfc.webtest.entity;

import java.io.Serializable;
import java.util.List;

public abstract class Case implements Serializable {
	private static final long serialVersionUID = -9201413428339539058L;
	private String name;
	private String startUrl;
	private List<CaseLine> lines;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}

	public List<CaseLine> getLines() {
		return lines;
	}

	public void setLines(List<CaseLine> lines) {
		this.lines = lines;
	}
}
