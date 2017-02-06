package org.junit.internal.builders;

import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/**
 * 不创建任何运行器的运行器工厂，为了统一封装。
 * 
 * @author 注释By JavaSking 2017年2月6日
 */
public class NullBuilder extends RunnerBuilder {

	/**
	 * 不创建任何运行器。
	 * 
	 * @param each
	 *          测试类。
	 * @return null。
	 */
	@Override
	public Runner runnerForClass(Class<?> each) throws Throwable {

		return null;
	}
}