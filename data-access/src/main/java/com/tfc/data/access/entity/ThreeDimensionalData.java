package com.tfc.data.access.entity;

import com.alibaba.fastjson.JSON;
import com.tfc.data.access.LuceneDataAccess;

/**
 * 三维数据格式，对应于：new int[i][j][k]
 * 
 * @author taofucheng
 * 
 */
public class ThreeDimensionalData extends AbstractFormatData {
	private String instanceName;
	private int xLen = 0;
	private int yLen = 0;
	private int zLen = 0;

	public ThreeDimensionalData(String instanceName, int xLen, int yLen, int zLen) {
		this.instanceName = instanceName + System.nanoTime();
		this.xLen = xLen;
		this.yLen = yLen;
		this.zLen = zLen;
	}

	public void save(int x, int y, int z, Object value) {
		LuceneDataAccess.save(genarateKey(x, y, z), JSON.toJSONString(value));
	}

	public Object get(int x, int y, int z, Class<?> targetElementClass) {
		String value = LuceneDataAccess.findValueByKey(genarateKey(x, y, z));
		return parseToObject(targetElementClass, value);
	}

	public String getString(int x, int y, int z) {
		return (String) get(x, y, z, String.class);
	}

	public Integer getInt(int x, int y, int z) {
		try {
			return (Integer) get(x, y, z, Integer.class);
		} catch (Exception e) {
			return 0;
		}
	}

	public Double getDouble(int x, int y, int z) {
		try {
			return (Double) get(x, y, z, Double.class);
		} catch (Exception e) {
			return 0.0;
		}
	}

	private String genarateKey(int x, int y, int z) {
		return instanceName + "_" + x + "_" + y + "_" + z;
	}

	/**
	 * 相当于int[i][j][k]数组的int[i].length.
	 * 
	 * @return
	 */
	public int getXlen() {
		return xLen;
	}

	/**
	 * 相当于int[i][j][k]数组的int[i][j].length.
	 * 
	 * @return
	 */
	public int getYlen() {
		return yLen;
	}

	/**
	 * 获取二维数据。
	 * 
	 * @param index
	 * @return
	 */
	public FlatFormatData getFlatData(int index) {
		FlatFormatData flat = new FlatFormatData(instanceName, yLen, zLen);
		flat.setInstanceName(instanceName + "_" + index);
		return flat;
	}

	public ArrayFormatData getArrayData(int x, int y) {
		ArrayFormatData arr = new ArrayFormatData(instanceName, zLen);
		arr.setInstanceName(instanceName + "_" + x + "_" + y);
		return arr;
	}
}
