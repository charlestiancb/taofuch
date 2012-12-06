package com.tfc.webtest.content.function;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.tfc.webtest.content.FunctionContent;
import com.tfc.webtest.entity.CaseLine;
import com.tfc.webtest.repo.Repository;

public class SqlFunction extends FunctionContent {

	@Override
	public String getFuncName() {
		return "sql";
	}

	public Object eval(WebDriver driver, CaseLine line, Map<String, Object> vars, Repository repo) {
		// TODO Auto-generated method stub
		return null;
	}

}
