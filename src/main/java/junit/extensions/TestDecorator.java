package junit.extensions;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestResult;

/**
 * ���Զ���İ�װ����
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class TestDecorator extends Assert implements Test {
	/**
	 * Ǳ�ڵĲ��Զ���
	 */
	protected Test fTest;

	/**
	 * ����һ��Ŀ����Զ���İ�װ����
	 * 
	 * @param test
	 *            Ŀ����Զ���
	 */
	public TestDecorator(Test test) {

		fTest= test;
	}

	/**
	 * ���л����Ĳ��Զ�����
	 * 
	 * @param result
	 *            ���Խ����
	 */
	public void basicRun(TestResult result) {

		fTest.run(result);
	}

	public int countTestCases() {

		return fTest.countTestCases();
	}

	public void run(TestResult result) {

		basicRun(result);
	}

	@Override
	public String toString() {

		return fTest.toString();
	}

	/**
	 * ��ȡǱ�ڵĲ��Զ���
	 * 
	 * @return Ǳ�ڵĲ��Զ���
	 */
	public Test getTest() {

		return fTest;
	}
}