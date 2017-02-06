package org.junit.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.FixMethodOrder;

/**
 * ������������
 * 
 * @author ע��By JavaSking 2017��2��4��
 */
public class MethodSorter {
	
	/**
	 * Ĭ�Ϸ�����������
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
	 * ������������ķ�����������
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
	 * ��Ŀ�����������ķ������򲢷��ء�Ӧ�õķ��������������ǣ�<br>
	 * 1��Ŀ����ʹ����<code>FixMethodOrder</code>ע��ָ���˷�����������<br>
	 * 2�����1��δָ����ʹ��Ĭ�Ϸ�����������
	 * 
	 * @param clazz
	 *            Ŀ���ࡣ
	 * @return �������顣
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
	 * ����һ���µķ�����������
	 */
	private MethodSorter() {

	}

	/**
	 * ��ȡ��������
	 * 
	 * @param fixMethodOrder
	 *            FixMethodOrderע�⡣
	 * @return ���ָ����FixMethodOrderע���򷵻�ע������ָ���ķ��������������򷵻�Ĭ�Ϸ�����������
	 */
	private static Comparator<Method> getSorter(FixMethodOrder fixMethodOrder) {

		if (fixMethodOrder == null) {
			return DEFAULT;
		}
		return fixMethodOrder.value().getComparator();
	}
}
