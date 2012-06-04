package com.cloudtech.ebusi.entity;

public class Role {
	private String roleName;
	private String desc;
	private String rules;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public Role(String name, String desc, String rules) {
		setRoleName(name);
		setDesc(desc);
		setRules(rules);
	}
}
