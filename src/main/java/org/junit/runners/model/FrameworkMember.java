package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * ����������Ա������ʱ��װ��<br>
 * <ul>
 * <li>��Ա�������򣩱���װΪ{@link FrameworkField}��
 * <li>��������װ{@link FrameworkMethod}��
 * </ul>
 * 
 * @since 4.7
 * @author ע��By JavaSking 2017��2��4��
 */
public abstract class FrameworkMember<T extends FrameworkMember<T>> {

	/**
	 * ��ȡ��ע�ڴ˳�Ա�ϵ�ע�⡣
	 * 
	 * @return ��ע�ڴ˳�Ա�ϵ�ע�⡣
	 */
	abstract Annotation[] getAnnotations();

	/**
	 * �жϵ�ǰ��Ա�Ƿ�Ŀ���Ա���ء�
	 * <ul>
	 * <li>����Ƿ��������ʾ�Ƿ����������ء�
	 * <li>����ǳ�Ա�������򣩣����ʾ�Ƿ�����ֵ���ǡ�
	 * </ul>
	 * 
	 * @param otherMember
	 *            Ŀ���Ա��
	 * @return �����ǰ��Ա��Ŀ���Ա�����򷵻�true�����򷵻�false��
	 */
	abstract boolean isShadowedBy(T otherMember);

	/**
	 * �жϵ�ǰ��Ա�Ƿ�Ŀ���Ա֮һ���ء�
	 * <ul>
	 * <li>����Ƿ��������ʾ�Ƿ����������ء�
	 * <li>����ǳ�Ա�������򣩣����ʾ�Ƿ�����ֵ���ǡ�
	 * </ul>
	 * 
	 * @param members
	 *            Ŀ���Ա�б�
	 * @return �����ǰ��Ա��Ŀ���Ա֮һ�����򷵻�true�����򷵻�false��
	 */
	boolean isShadowedBy(List<T> members) {

		for (T each : members) {
			if (isShadowedBy(each)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �ж��Ƿ�Ϊ������Ա��
	 * 
	 * @return ����ǹ�����Ա�򷵻�true�����򷵻�false��
	 */
	public abstract boolean isPublic();

	/**
	 * �ж��Ƿ�Ϊ��̬��Ա��
	 * 
	 * @return ���Ϊ��̬��Ա�򷵻�true�����򷵻�false��
	 */
	public abstract boolean isStatic();

	/**
	 * ��ȡ��Ա����
	 * 
	 * @return ��Ա����
	 */
	public abstract String getName();

	/**
	 * ��ȡ��Ա���͡�
	 * <ul>
	 * <li>����ǳ�Ա�ǳ�Ա��������Ϊ��Ա���������͡�
	 * <li>����ǳ�Ա�Ƿ�������Ϊ�����ķ���ֵ��
	 * </ul>
	 * 
	 * @return ��Ա���͡�
	 */
	public abstract Class<?> getType();
}
