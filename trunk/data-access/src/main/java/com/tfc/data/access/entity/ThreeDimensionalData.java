package com.tfc.data.access.entity;

import com.alibaba.fastjson.JSON;
import com.tfc.data.access.RepositoryFactory;

/**
 * 三维数据格式，对应于：new int[i][j][k]
 * 
 * @author taofucheng
 * 
 */
public class ThreeDimensionalData<T> extends AbstractFormatData<T> {
	private int xLen = 0;
	private int yLen = 0;
	private int zLen = 0;

	private int xCurLen = 0;
	private int yCurLen = 0;
	private int zCurLen = 0;

	public ThreeDimensionalData() {
		this("def", 0, 0, 0);
	}

	public ThreeDimensionalData(String instanceName, int xLen, int yLen, int zLen) {
		setInstanceName(instanceName + System.nanoTime() + random());
		this.xLen = xLen;
		this.yLen = yLen;
		this.zLen = zLen;
	}

	public void save(int x, int y, int z, T value) {
		if (getValueClass() == null && value != null) {
			setValueClass(value.getClass());
		}
		String store = processStore(value);
		boolean ret = RepositoryFactory.save(genarateKey(x, y, z), store);
		if (ret) {
			xCurLen = x > xCurLen ? x : xCurLen;
			yCurLen = y > yCurLen ? y : yCurLen;
			zCurLen = z > zCurLen ? z : zCurLen;
		}
	}

	public T get(int x, int y, int z) {
		if (getValueClass() == null) {
			return null;
		}
		String value = RepositoryFactory.findValueByKey(genarateKey(x, y, z));
		return parseValue(value);
	}

	private String genarateKey(int x, int y, int z) {
		return getInstanceName() + "_" + x + "_" + y + "_" + z;
	}

	/**
	 * 相当于s[i][j].length，其中，s=new int[x][y][z]
	 * 
	 * @return
	 */
	public int zlength() {
		return zLen > zCurLen ? zLen : zCurLen;
	}

	/**
	 * 相当于s[i].length，其中，s=new int[x][y][z]
	 * 
	 * @return
	 */
	public int ylength() {
		return yLen > yCurLen ? yLen : yCurLen;
	}

	/**
	 * 相当于s.length，其中，s=new int[x][y][z]
	 * 
	 * @return
	 */
	public int length() {
		return xLen > xCurLen ? xLen : xCurLen;
	}

	/**
	 * 获取二维数据。
	 * 
	 * @param x
	 * @return
	 */
	public FlatFormatData<T> fetchFlatData(int x) {
		FlatFormatData<T> flat = new FlatFormatData<T>(getInstanceName(), ylength(), zlength());
		flat.setInstanceName(getInstanceName() + "_" + x);
		flat.setValueClass(this.getValueClass());
		return flat;
	}

	public ArrayFormatData<T> fetchArrayData(int x, int y) {
		ArrayFormatData<T> arr = new ArrayFormatData<T>(getInstanceName(), zlength());
		arr.setInstanceName(getInstanceName() + "_" + x + "_" + y);
		arr.setValueClass(this.getValueClass());
		return arr;
	}

	public int getxLen() {
		return xLen;
	}

	public void setxLen(int xLen) {
		this.xLen = xLen;
	}

	public int getyLen() {
		return yLen;
	}

	public void setyLen(int yLen) {
		this.yLen = yLen;
	}

	public int getzLen() {
		return zLen;
	}

	public void setzLen(int zLen) {
		this.zLen = zLen;
	}

	public int getxCurLen() {
		return xCurLen;
	}

	public void setxCurLen(int xCurLen) {
		this.xCurLen = xCurLen;
	}

	public int getyCurLen() {
		return yCurLen;
	}

	public void setyCurLen(int yCurLen) {
		this.yCurLen = yCurLen;
	}

	public int getzCurLen() {
		return zCurLen;
	}

	public void setzCurLen(int zCurLen) {
		this.zCurLen = zCurLen;
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
