package com.tfc.webtest.service;

import com.tfc.webtest.entity.DataCase;
import com.tfc.webtest.repo.MysqlRepository;
import com.tfc.webtest.repo.Repository;

/**
 * 用例数据的服务
 * 
 * @author taofucheng
 * 
 */
public class DataService {
	private Repository repo = new MysqlRepository();

	/**
	 * 根据数据编号获取对应的所有执行用例步骤及数据
	 * 
	 * @param dataId
	 * @return
	 */
	public DataCase findById(String dataId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Repository getRepo() {
		return repo;
	}

	public void setRepo(Repository repo) {
		this.repo = repo;
	}

}
