package org.junit.internal.builders;

import org.junit.Ignore;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/**
 * 负责创建IgnoredClassRunner运行器的工厂。测试类被@Ignored注解标注。<br>
 * 
 * @author 注释By JavaSking 2017年2月6日
 */
public class IgnoredBuilder extends RunnerBuilder {

	/**
	 * 若测试类被@Ignored注解标注，则返回IgnoredClassRunner运行器，否则返回null，交于下个工厂创建。
	 * 
	 * @param 测试类。
	 * @return IgnoredClassRunner运行器或null。
	 */
	@Override
	public Runner runnerForClass(Class<?> testClass) {

		if (testClass.getAnnotation(Ignore.class) != null) {
			return new IgnoredClassRunner(testClass);
		}
		return null;
	}
}