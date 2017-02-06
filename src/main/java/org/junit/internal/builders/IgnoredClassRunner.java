package org.junit.internal.builders;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

/**
 * 跳过测试类的运行器。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class IgnoredClassRunner extends Runner {
	/**
	 * 被跳过的测试类。
	 */
	private final Class<?> fTestClass;

	/**
	 * 构造一个跳过目标测试类的运行器。
	 * 
	 * @param testClass
	 *            待跳过的测试类。
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