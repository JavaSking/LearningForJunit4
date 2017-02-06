package org.junit.internal.builders;

import org.junit.Ignore;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/**
 * ���𴴽�IgnoredClassRunner�������Ĺ����������౻@Ignoredע���ע��<br>
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
public class IgnoredBuilder extends RunnerBuilder {

	/**
	 * �������౻@Ignoredע���ע���򷵻�IgnoredClassRunner�����������򷵻�null�������¸�����������
	 * 
	 * @param �����ࡣ
	 * @return IgnoredClassRunner��������null��
	 */
	@Override
	public Runner runnerForClass(Class<?> testClass) {

		if (testClass.getAnnotation(Ignore.class) != null) {
			return new IgnoredClassRunner(testClass);
		}
		return null;
	}
}