package org.junit.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.FixMethodOrder;

/**
 * 方法排序器。
 * 
 * @author 注释By JavaSking 2017年2月4日
 */
public class MethodSorter {
	
	/**
	 * 默认方法排序器。
	 */
	public static Comparator<Method> DEFAULT= new Comparator<Method>() {
		
		public int compare(Method m1, Method m2) {
			int i1= m1.getName().hashCode();
			int i2= m2.getName().hashCode();
			if (i1 != i2) {
				return i1 < i2 ? -1 : 1;
			}
			return NAME_ASCENDING.compare(m1, m2);
		}
	};

	/**
	 * 按方法名升序的方法排序器。
	 */
	public static Comparator<Method> NAME_ASCENDING= new Comparator<Method>() {
		
		public int compare(Method m1, Method m2) {
			final int comparison= m1.getName().compareTo(m2.getName());
			if (comparison != 0) {
				return comparison;
			}
			return m1.toString().compareTo(m2.toString());
		}
	};

	/**
	 * 将目标类所声明的方法排序并返回。应用的方法排序器可能是：<br>
	 * 1）目标类使用了<code>FixMethodOrder</code>注解指定了方法排序器。<br>
	 * 2）如果1中未指定则使用默认方法排序器。
	 * 
	 * @param clazz
	 *            目标类。
	 * @return 方法数组。
	 */
	public static Method[] getDeclaredMethods(Class<?> clazz) {

		Comparator<Method> comparator= getSorter(clazz.getAnnotation(FixMethodOrder.class));
		Method[] methods= clazz.getDeclaredMethods();
		if (comparator != null) {
			Arrays.sort(methods, comparator);
		}

		return methods;
	}

	/**
	 * 构造一个新的方法排序器。
	 */
	private MethodSorter() {

	}

	/**
	 * 获取排序器。
	 * 
	 * @param fixMethodOrder
	 *            FixMethodOrder注解。
	 * @return 如果指定了FixMethodOrder注解则返回注解属性指定的方法排序器，否则返回默认方法排序器。
	 */
	private static Comparator<Method> getSorter(FixMethodOrder fixMethodOrder) {

		if (fixMethodOrder == null) {
			return DEFAULT;
		}
		return fixMethodOrder.value().getComparator();
	}
}
