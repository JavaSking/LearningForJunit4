package org.junit.runner;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * 计算机，代表测试运行策略的抽象。
 * 
 * @since 4.6
 * @author 注释By JavaSking 2017年2月5日
 */
public class Computer {

	/**
	 * 获取序列化运行测试的计算机。
	 * 
	 * @return 序列化运行测试的计算机。
	 */
	public static Computer serial() {

		return new Computer();
	}

	/**
	 * 使用目标运行器创建器创建测试目标测试组的运行器。
	 * 
	 * @param builder
	 *            运行器创建器。
	 * @param classes
	 *            测试组。
	 * @return 测试目标测试组的运行器。
	 * @throws InitializationError
	 *             运行器初始化异常。
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
	 * 使用目标运行器创建器创建测试目标类的运行器。
	 * 
	 * @param builder
	 *            运行器创建器。
	 * @param testClass
	 *            待测试类。
	 * @return 测试目标类的运行器。
	 * @throws Throwable
	 *             异常。
	 */
	protected Runner getRunner(RunnerBuilder builder, Class<?> testClass) throws Throwable {

		return builder.runnerForClass(testClass);
	}
}
