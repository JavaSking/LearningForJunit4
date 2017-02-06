package org.junit.internal.builders;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.RunnerBuilder;

/**
 * 负责创建BlockJUnit4ClassRunner运行器的工厂。<br>
 * Junit默认采用BlockJUnit4ClassRunner运行器。
 * 
 * @author 注释By JavaSking 2017年2月6日
 */
public class JUnit4Builder extends RunnerBuilder {

	/**
	 * 获取BlockJUnit4ClassRunner运行器。
	 * 
	 * @param testClass
	 *          测试类。
	 * @return BlockJUnit4ClassRunner运行器。
	 */
	@Override
	public Runner runnerForClass(Class<?> testClass) throws Throwable {

		return new BlockJUnit4ClassRunner(testClass);
	}
}