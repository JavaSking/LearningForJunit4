package org.junit.internal.runners.statements;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.Statement;

/**
 * �����׳������쳣�Ķ�������һ��Statement�ǲ��Է����ĵ���{@code InvokeMethod}<br>
 * ���������δʵ�֣����ʾ����ʧ�ܡ�
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class ExpectException extends Statement {
	
	/**
	 * ��һ��������
	 */
	private Statement fNext;

	/**
	 * �����׳����쳣���͡�
	 */
	private final Class<? extends Throwable> fExpected;

	/**
	 * ����һ�������׳������쳣�Ķ�����
	 * 
	 * @param next
	 *            ��һ��������
	 * @param expected
	 *            �����׳����쳣���͡�
	 */
	public ExpectException(Statement next, Class<? extends Throwable> expected) {

		fNext= next;
		fExpected= expected;
	}

	/**
	 * �����׳������쳣�����������δʵ�֣����׳��쳣��������ʧ�ܡ�
	 */
	@Override
	public void evaluate() throws Exception {

		boolean complete= false;
		try {
			fNext.evaluate();
			complete= true;
		} catch (AssumptionViolatedException e) {
			throw e;//������������쳣��������ΪAssumptionViolatedException���͡�
		} catch (Throwable e) {
			/* 1����������������쳣�����׳��쳣 */
			if (!fExpected.isAssignableFrom(e.getClass())) {
				String message= "Unexpected exception, expected<" + fExpected.getName() + "> but was<" + e.getClass().getName() + ">";
				throw new Exception(message, e);
			}
			/* 2��������������쳣�����κβ��� */
		}
		if (complete) {
			throw new AssertionError("Expected exception: " + fExpected.getName());
		}
	}
}