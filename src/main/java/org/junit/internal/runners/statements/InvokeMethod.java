package org.junit.internal.runners.statements;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * �������ö�����<br>
 * ע�⣺��������ڵ�û�ж�����Statement�����ã���Ϊ����Statement���е����һ���ڵ㡣
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class InvokeMethod extends Statement {
	/**
	 * Ŀ�귽����
	 */
	private final FrameworkMethod fTestMethod;

	/**
	 * ���÷����Ķ���
	 */
	private Object fTarget;

	/**
	 * ����һ���������ö�����
	 * 
	 * @param testMethod
	 *            Ŀ�귽����
	 * @param target
	 *            ���÷����Ķ���
	 */
	public InvokeMethod(FrameworkMethod testMethod, Object target) {

		fTestMethod= testMethod;
		fTarget= target;
	}

	/**
	 * ���з������á�
	 */
	@Override
	public void evaluate() throws Throwable {

		fTestMethod.invokeExplosively(fTarget);
	}
}