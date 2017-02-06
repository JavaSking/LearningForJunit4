package org.junit.runner;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * �����������������в��Եĳ���
 * 
 * @since 4.6
 * @author ע��By JavaSking 2017��2��5��
 */
public class Computer {

	/**
	 * ��ȡ���л����в��Եļ������
	 * 
	 * @return ���л����в��Եļ������
	 */
	public static Computer serial() {

		return new Computer();
	}

	/**
	 * ʹ��Ŀ����������������������Ŀ����������������
	 * 
	 * @param builder
	 *            ��������������
	 * @param classes
	 *            �����顣
	 * @return ����Ŀ����������������
	 * @throws InitializationError
	 *             ��������ʼ���쳣��
	 */
	public Runner getSuite(final RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
		
		return new Suite(new RunnerBuilder() {
			@Override
			public Runner runnerForClass(Class<?> testClass) throws Throwable {
				return getRunner(builder, testClass);
			}
		}, classes);
	}

	/**
	 * ʹ��Ŀ����������������������Ŀ�������������
	 * 
	 * @param builder
	 *            ��������������
	 * @param testClass
	 *            �������ࡣ
	 * @return ����Ŀ�������������
	 * @throws Throwable
	 *             �쳣��
	 */
	protected Runner getRunner(RunnerBuilder builder, Class<?> testClass) throws Throwable {

		return builder.runnerForClass(testClass);
	}
}
