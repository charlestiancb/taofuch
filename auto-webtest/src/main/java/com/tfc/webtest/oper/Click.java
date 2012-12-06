package com.tfc.webtest.oper;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tfc.webtest.constants.OperType;
import com.tfc.webtest.entity.CaseLine;
import com.tfc.webtest.repo.Repository;

public class Click extends Operation {

	@Override
	public OperType getName() {
		return OperType.click;
	}

	@Override
	protected void execute(WebDriver driver, CaseLine line, Map<String, Object> vars, Repository repo) {
		WebElement ele = findElment(driver, line.getPageElement(), line.getElementLocation());
		if (ele != null) {
			ele.click();
		}
	}

	@Override
	public boolean canInput() {
		return true;
	}
}
