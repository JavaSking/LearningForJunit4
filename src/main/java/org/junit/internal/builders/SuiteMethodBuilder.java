package org.junit.internal.builders;

import org.junit.internal.runners.SuiteMethod;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/**
 * ���𴴽�SuiteMethod��������������������
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
public class SuiteMethodBuilder extends RunnerBuilder {

	/**
	 * �������ຬ��suite�������򷵻�SuiteMethod�����������򷵻�null�������¸�����������
	 * 
	 * @param each
	 *          �����ࡣ
	 * @return SuiteMethod��������null��
	 */
	@Override
	public Runner runnerForClass(Class<?> each) throws Throwable {

		if (hasSuiteMethod(each)) {
			return new SuiteMethod(each);
		}
		return null;
	}

	/**
	 * �ж�Ŀ��������Ƿ���suite������
	 * 
	 * @param testClass
	 *          Ŀ������ࡣ
	 * @return ���Ŀ������ຬ��suite�����򷵻�true�����򷵻�false��
	 */
	public boolean hasSuiteMethod(Class<?> testClass) {

		try {
			testClass.getMethod("suite");
		} catch (NoSuchMethodException e) {
			return false;
		}
		return true;
	}
}