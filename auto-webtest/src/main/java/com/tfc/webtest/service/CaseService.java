package com.tfc.webtest.service;

import java.util.List;

import com.tfc.webtest.entity.DataCase;
import com.tfc.webtest.entity.DefinedCase;
import com.tfc.webtest.repo.MysqlRepository;
import com.tfc.webtest.repo.Repository;

/**
 * 用例操作相关的服务类
 * 
 * @author taofucheng
 * 
 */
public class CaseService {
	private Repository repo = new MysqlRepository();

	public List<DataCase> findDataCasesById(String caseId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Repository getRepo() {
		return repo;
	}

	public void setRepo(Repository repo) {
		this.repo = repo;
	}

	/**
	 * 根据指定的数据ID获取对应的用例信息
	 * 
	 * @param dataId
	 * @return
	 */
	public DefinedCase findByDataId(String dataId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 判断指定的数据对应的用例是否是可执行的用例
	 * 
	 * @param dataId
	 * @return
	 */
	public boolean isExecute(String dataId) {
		// TODO Auto-generated method stub
		return false;
	}

}
