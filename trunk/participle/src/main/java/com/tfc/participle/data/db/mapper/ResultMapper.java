package com.tfc.participle.data.db.mapper;

import java.sql.ResultSet;
import java.util.List;

public interface ResultMapper<T> {
	/**
	 * 进行转换处理。
	 * 
	 * @param rs
	 * @return
	 */
	public List<T> doMapper(ResultSet rs);
}
