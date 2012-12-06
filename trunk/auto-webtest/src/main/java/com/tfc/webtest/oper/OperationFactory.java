package com.tfc.webtest.oper;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.tfc.webtest.constants.OperType;
import com.tfc.webtest.utils.ClassUtils;

/**
 * 操作的获取工厂
 * 
 * @author taofucheng
 * 
 */
public class OperationFactory {
	private static Map<OperType, Operation> opers;

	/**
	 * 根据操作的类型值查找对应的操作
	 * 
	 * @param operValue
	 * @return
	 */
	public static Operation findOper(String operValue) {
		if (opers == null) {
			opers = new HashMap<OperType, Operation>();
			scanDefault();
		}
		if (StringUtils.isEmpty(operValue)) {
			return null;
		}
		try {
			OperType type = OperType.getByValue(Integer.parseInt(operValue));
			return opers.get(type);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将支持的所有的默认执行器都扫描进来
	 */
	private static void scanDefault() {
		Set<Class<?>> cls = ClassUtils.getClasses(Operation.class.getPackage());
		if (cls != null && cls.size() > 0) {
			for (Class<?> c : cls) {
				try {
					if (Operation.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
						Operation o = (Operation) c.newInstance();
						opers.put(o.getName(), o);
					}
				} catch (Exception e) {
					System.err.println("加载操作[" + c.getName() + "]失败~");
				}
			}
		}
	}
}
