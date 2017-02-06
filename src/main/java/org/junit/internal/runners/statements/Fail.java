package org.junit.internal.runners.statements;

import org.junit.runners.model.Statement;

/**
 * ������Ŀ���쳣�����ʧ�ܶ�����
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class Fail extends Statement {
	
	/**
	 * �������쳣��
	 */
	private final Throwable fError;

	/**
	 * ����һ����Ŀ���쳣�����ʧ�ܶ�����
	 * 
	 * @param e
	 *            Ŀ���쳣��
	 */
	public Fail(Throwable e) {

		fError= e;
	}

	/**
	 * �׳�Ŀ���쳣��
	 */
	@Override
	public void evaluate() throws Throwable {

		throw fError;
	}
}
