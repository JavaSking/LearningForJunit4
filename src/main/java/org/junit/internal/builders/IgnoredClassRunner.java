package org.junit.internal.builders;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

/**
 * �������������������
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class IgnoredClassRunner extends Runner {
	/**
	 * �������Ĳ����ࡣ
	 */
	private final Class<?> fTestClass;

	/**
	 * ����һ������Ŀ����������������
	 * 
	 * @param testClass
	 *            �������Ĳ����ࡣ
	 */
	public IgnoredClassRunner(Class<?> testClass) {

		fTestClass= testClass;
	}

	@Override
	public void run(RunNotifier notifier) {

		notifier.fireTestIgnored(getDescription());
	}

	@Override
	public Description getDescription() {

		return Description.createSuiteDescription(fTestClass);
	}
}