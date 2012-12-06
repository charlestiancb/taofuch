package com.tfc.webtest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.tfc.webtest.entity.CaseLine;
import com.tfc.webtest.entity.DataCase;
import com.tfc.webtest.entity.STATUS;
import com.tfc.webtest.oper.Operation;
import com.tfc.webtest.oper.OperationFactory;
import com.tfc.webtest.repo.MysqlRepository;
import com.tfc.webtest.repo.Repository;

/*
 * 执行用例的服务
 */
public class ExecuteService {
	private Repository repo = new MysqlRepository();
	private DataService dataService = new DataService();
	private CaseService caseService = new CaseService();
	private StatisticService stasticService;

	public ExecuteService() {
	}

	public ExecuteService(StatisticService stasticService) {
		setStasticService(stasticService);
	}

	/**
	 * 以某个数据驱动执行用例！
	 * 
	 * @param dataId
	 */
	public void execFromData(String dataId) {
		dataService.setRepo(repo);
		DataCase _case = dataService.findById(dataId);
		execCase(_case);
	}

	/**
	 * 执行指定的用例流程的所有数据的情况
	 * 
	 * @param caseId
	 */
	public void execFromCase(String caseId) {
		caseService.setRepo(repo);
		List<DataCase> cases = caseService.findDataCasesById(caseId);
		if (cases == null || cases.size() == 0) {
			stasticService.fail(caseId, STATUS.NOT_EXISTS, "");
		} else {
			for (DataCase _case : cases) {
				execCase(_case);
			}
		}
	}

	/**
	 * 执行指定的数据用例
	 * 
	 * @param _case
	 */
	public void execCase(DataCase _case) {
		try {
			if (_case == null) {
				stasticService.fail(_case, STATUS.NOT_EXISTS, "");
			} else if (caseService.isExecute(_case.getDataId())) {
				stasticService.fail(_case, STATUS.ERROR, "这个数据对应的是不可执行的用例片段！");
			} else if (StringUtils.isBlank(_case.getStartUrl())) {
				stasticService.fail(_case, STATUS.ERROR, "没有指定启动URL！");
				return;
			} else if (_case.getLines() == null || _case.getLines().size() == 0) {
				stasticService.fail(_case, STATUS.ERROR, "没有可执行的用例步骤！");
				return;
			} else {
				// TODO 下面的改成可扩展的，比如支持多种浏览器
				WebDriver driver = new InternetExplorerDriver();
				driver.get(_case.getStartUrl());
				List<CaseLine> lines = _case.getLines();
				// 存储变量用的
				Map<String, Object> vars = new HashMap<String, Object>();
				for (CaseLine line : lines) {
					Operation o = OperationFactory.findOper(line.getOperName());
					if (o == null) {
						System.err.println("不支持的操作[" + line.getOperName() + "]！");
						return;
					}
					o.exec(driver, line, vars, repo);
				}
			}
		} catch (Exception e) {
			stasticService.fail(_case, STATUS.ERROR, ExceptionUtils.getFullStackTrace(e));
		}
	}

	public Repository getRepo() {
		return repo;
	}

	public void setRepo(Repository repo) {
		this.repo = repo;
	}

	public StatisticService getStasticService() {
		return stasticService;
	}

	public void setStasticService(StatisticService stasticService) {
		this.stasticService = stasticService;
	}
}
