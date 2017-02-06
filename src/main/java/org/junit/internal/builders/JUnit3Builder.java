package org.junit.internal.builders;

import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/**
 * 负责创建JUnit38ClassRunner运行器的运行器工厂。<br>
 * 
 * @author 注释By JavaSking 2017年2月6日
 */
public class JUnit3Builder extends RunnerBuilder {

	/**
	 * 若测试类为旧风格测试类，则返回JUnit38ClassRunner运行器，否则返回null，交于下个工厂创建。
	 * 
	 * @param testClass
	 *          测试类。
	 * @return JUnit38ClassRunner运行器或null。
	 */
	@Override
	public Runner runnerForClass(Class<?> testClass) throws Throwable {

		if (isPre4Test(testClass)) {
			return new JUnit38ClassRunner(testClass);
		}
		return null;
	}

	/**
	 * 判断是否为Junit4之前风格的测试类（旧风格的继承于TestCase的类）。
	 * 
	 * @param testClass
	 *          测试类。
	 * @return 如果为Junit4之前风格的测试类则返回true，否则返回false。
	 */
	boolean isPre4Test(Class<?> testClass) {

		return junit.framework.TestCase.class.isAssignableFrom(testClass);
	}
}