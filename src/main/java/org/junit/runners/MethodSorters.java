package org.junit.runners;

import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.internal.MethodSorter;

/**
 * ����������<code>MethodSorter</code>��ö�١�
 * 
 * @author ע��By JavaSking 2017��2��4��
 * 
 * @since 4.11
 */
public enum MethodSorters {

	/**
	 * ����������������
	 */
	NAME_ASCENDING(MethodSorter.NAME_ASCENDING),

	/**
	 * ��JVM��Ϊ��������
	 */
	JVM(null),

	/**
	 * Ĭ�Ϸ�����������
	 */
	DEFAULT(MethodSorter.DEFAULT);

	/**
	 * ������������
	 */
	private final Comparator<Method> comparator;

	/**
	 * Ĭ�Ϲ��췽����
	 * 
	 * @param comparator
	 *            ������������
	 */
	private MethodSorters(Comparator<Method> comparator) {

		this.comparator= comparator;
	}

	/**
	 * ��ȡ������������
	 * 
	 * @return ������������
	 */
	public Comparator<Method> getComparator() {

		return comparator;
	}
}
