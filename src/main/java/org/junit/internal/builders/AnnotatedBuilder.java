package org.junit.internal.builders;

import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * �������@RunWithע���������������<br>
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
public class AnnotatedBuilder extends RunnerBuilder {

	/**
	 * ������Ϣ��ʽģʽ��
	 */
	private static final String CONSTRUCTOR_ERROR_FORMAT = "Custom runner class %s should have a public constructor with signature %s(Class testClass)";

	/**
	 * ί�ɵ�����������������
	 */
	private RunnerBuilder fSuiteBuilder;

	/**
	 * ����һ������@RunWithע�������������������
	 * 
	 * @param suiteBuilder
	 *          ί�ɵ�����������������
	 */
	public AnnotatedBuilder(RunnerBuilder suiteBuilder) {

		fSuiteBuilder = suiteBuilder;
	}

	/**
	 * �������౻@RunWithע���ע���򷵻�Ŀ�����������������򷵻�null�������¸�����������
	 * 
	 * @param �����ࡣ
	 * @return Ŀ��������������null��
	 */
	@Override
	public Runner runnerForClass(Class<?> testClass) throws Exception {

		/* ����Ƿ����@RunWithע��ָ��ѡ�õ����������� */
		RunWith annotation = testClass.getAnnotation(RunWith.class);
		if (annotation != null) {
			return buildRunner(annotation.value(), testClass);
		}
		return null;
	}

	/**
	 * Ϊ�������ഴ��Ŀ�����͵���������<br>
	 * 
	 * @param runnerClass
	 *          ���������͡�
	 * @param testClass
	 *          �������ࡣ
	 * @return ��������
	 * @throws Exception
	 *           ��������ʼ���쳣��
	 */
	public Runner buildRunner(Class<? extends Runner> runnerClass, Class<?> testClass) throws Exception {

		try {
			return runnerClass.getConstructor(Class.class).newInstance(new Object[] { testClass });
		} catch (NoSuchMethodException e) {
			try {
				return runnerClass.getConstructor(Class.class, RunnerBuilder.class).newInstance(new Object[] { testClass, fSuiteBuilder });
			} catch (NoSuchMethodException e2) {
				String simpleName = runnerClass.getSimpleName();
				throw new InitializationError(String.format(CONSTRUCTOR_ERROR_FORMAT, simpleName, simpleName));
			}
		}
	}
}