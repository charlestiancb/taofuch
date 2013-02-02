package com.tfc.data.access.entity;

import com.alibaba.fastjson.JSON;
import com.tfc.data.access.RepositoryFactory;

/**
 * 三维数据格式，对应于：new int[i][j][k]
 * 
 * @author taofucheng
 * 
 */
public class ThreeDimensionalData<T> extends AbstractFormatData {
	private String instanceName;
	private int xLen = 0;
	private int yLen = 0;
	private int zLen = 0;

	public ThreeDimensionalData(String instanceName, int xLen, int yLen, int zLen) {
		this.instanceName = instanceName + System.nanoTime() + random();
		this.xLen = xLen;
		this.yLen = yLen;
		this.zLen = zLen;
	}

	public void save(int x, int y, int z, T value) {
		if (valueClass == null && value != null) {
			valueClass = value.getClass();
		}
		String store = getStoreValue(value);
		RepositoryFactory.save(genarateKey(x, y, z), store);
	}

	@SuppressWarnings("unchecked")
	public T getValue(int x, int y, int z) {
		if (valueClass == null) {
			return null;
		}
		String value = RepositoryFactory.findValueByKey(genarateKey(x, y, z));
		if ("NaN".equals(value)) {
			if (Double.class.isAssignableFrom(valueClass)) {
				return (T) new Double("NaN");
			} else if (Float.class.isAssignableFrom(valueClass)) {
				return (T) new Float("NaN");
			}
		}
		return (T) parseToObject(valueClass, value);
	}

	private String genarateKey(int x, int y, int z) {
		return instanceName + "_" + x + "_" + y + "_" + z;
	}

	/**
	 * 相当于s[i][j].length，其中，s=new int[x][y][z]
	 * 
	 * @return
	 */
	public int getZlen() {
		return zLen;
	}

	/**
	 * 相当于s[i].length，其中，s=new int[x][y][z]
	 * 
	 * @return
	 */
	public int getYlen() {
		return yLen;
	}

	/**
	 * 相当于s.length，其中，s=new int[x][y][z]
	 * 
	 * @return
	 */
	public int length() {
		return xLen;
	}

	/**
	 * 获取二维数据。
	 * 
	 * @param x
	 * @return
	 */
	public FlatFormatData<T> getFlatData(int x) {
		FlatFormatData<T> flat = new FlatFormatData<T>(instanceName, yLen, zLen);
		flat.setInstanceName(instanceName + "_" + x);
		return flat;
	}

	public ArrayFormatData<T> getArrayData(int x, int y) {
		ArrayFormatData<T> arr = new ArrayFormatData<T>(instanceName, zLen);
		arr.setInstanceName(instanceName + "_" + x + "_" + y);
		return arr;
	}

	public static void main(String[] args) {
		String[][][] x = new String[2][3][4];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 4; k++) {
					x[i][j][k] = i + "_" + j + "_" + k;
				}
			}
		}
		System.out.println(x.length);// 结果2
		System.out.println(x[1].length);// 结果3
		System.out.println(x[1][2].length);// 结果4
		System.out.println(JSON.toJSONString(x[1]));
		// 上面结果：[["1_0_0","1_0_1","1_0_2","1_0_3"],["1_1_0","1_1_1","1_1_2","1_1_3"],["1_2_0","1_2_1","1_2_2","1_2_3"]]
		System.out.println(JSON.toJSONString(x[1][2]));
		// 上面结果：["1_2_0","1_2_1","1_2_2","1_2_3"]
	}
}
