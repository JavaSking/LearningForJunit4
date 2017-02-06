package org.junit.runners;

import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.internal.MethodSorter;

/**
 * 方法排序器<code>MethodSorter</code>的枚举。
 * 
 * @author 注释By JavaSking 2017年2月4日
 * 
 * @since 4.11
 */
public enum MethodSorters {

	/**
	 * 按方法名升序排序。
	 */
	NAME_ASCENDING(MethodSorter.NAME_ASCENDING),

	/**
	 * 按JVM行为排序，乱序。
	 */
	JVM(null),

	/**
	 * 默认方法排序器。
	 */
	DEFAULT(MethodSorter.DEFAULT);

	/**
	 * 方法排序器。
	 */
	private final Comparator<Method> comparator;

	/**
	 * 默认构造方法。
	 * 
	 * @param comparator
	 *            方法排序器。
	 */
	private MethodSorters(Comparator<Method> comparator) {

		this.comparator= comparator;
	}

	/**
	 * 获取方法排序器。
	 * 
	 * @return 方法排序器。
	 */
	public Comparator<Method> getComparator() {

		return comparator;
	}
}
