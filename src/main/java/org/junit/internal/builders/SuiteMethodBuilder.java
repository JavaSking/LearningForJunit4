package org.junit.internal.builders;

import org.junit.internal.runners.SuiteMethod;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/**
 * 负责创建SuiteMethod运行器的运行器工厂。
 * 
 * @author 注释By JavaSking 2017年2月6日
 */
public class SuiteMethodBuilder extends RunnerBuilder {

	/**
	 * 若测试类含有suite方法，则返回SuiteMethod运行器，否则返回null，交于下个工厂创建。
	 * 
	 * @param each
	 *          测试类。
	 * @return SuiteMethod运行器或null。
	 */
	@Override
	public Runner runnerForClass(Class<?> each) throws Throwable {

		if (hasSuiteMethod(each)) {
			return new SuiteMethod(each);
		}
		return null;
	}

	/**
	 * 判断目标测试类是否含有suite方法。
	 * 
	 * @param testClass
	 *          目标测试类。
	 * @return 如果目标测试类含有suite方法则返回true，否则返回false。
	 */
	public boolean hasSuiteMethod(Class<?> testClass) {

		try {
			testClass.getMethod("suite");
		} catch (NoSuchMethodException e) {
			return false;
		}
		return true;
	}
}