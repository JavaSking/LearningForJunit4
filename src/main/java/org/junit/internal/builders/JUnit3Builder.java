package org.junit.internal.builders;

import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/**
 * ���𴴽�JUnit38ClassRunner��������������������<br>
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
public class JUnit3Builder extends RunnerBuilder {

	/**
	 * ��������Ϊ�ɷ������࣬�򷵻�JUnit38ClassRunner�����������򷵻�null�������¸�����������
	 * 
	 * @param testClass
	 *          �����ࡣ
	 * @return JUnit38ClassRunner��������null��
	 */
	@Override
	public Runner runnerForClass(Class<?> testClass) throws Throwable {

		if (isPre4Test(testClass)) {
			return new JUnit38ClassRunner(testClass);
		}
		return null;
	}

	/**
	 * �ж��Ƿ�ΪJunit4֮ǰ���Ĳ����ࣨ�ɷ��ļ̳���TestCase���ࣩ��
	 * 
	 * @param testClass
	 *          �����ࡣ
	 * @return ���ΪJunit4֮ǰ���Ĳ������򷵻�true�����򷵻�false��
	 */
	boolean isPre4Test(Class<?> testClass) {

		return junit.framework.TestCase.class.isAssignableFrom(testClass);
	}
}