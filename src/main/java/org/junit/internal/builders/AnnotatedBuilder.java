package org.junit.internal.builders;

import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * 负责解析@RunWith注解的运行器工厂。<br>
 * 
 * @author 注释By JavaSking 2017年2月6日
 */
public class AnnotatedBuilder extends RunnerBuilder {

	/**
	 * 错误信息格式模式。
	 */
	private static final String CONSTRUCTOR_ERROR_FORMAT = "Custom runner class %s should have a public constructor with signature %s(Class testClass)";

	/**
	 * 委派的运行器创建工厂。
	 */
	private RunnerBuilder fSuiteBuilder;

	/**
	 * 构造一个负责@RunWith注解解析的运行器工厂。
	 * 
	 * @param suiteBuilder
	 *          委派的运行器创建工厂。
	 */
	public AnnotatedBuilder(RunnerBuilder suiteBuilder) {

		fSuiteBuilder = suiteBuilder;
	}

	/**
	 * 若测试类被@RunWith注解标注，则返回目标类型运行器，否则返回null，交于下个工厂创建。
	 * 
	 * @param 测试类。
	 * @return 目标类型运行器或null。
	 */
	@Override
	public Runner runnerForClass(Class<?> testClass) throws Exception {

		/* 检查是否存在@RunWith注解指明选用的运行器类型 */
		RunWith annotation = testClass.getAnnotation(RunWith.class);
		if (annotation != null) {
			return buildRunner(annotation.value(), testClass);
		}
		return null;
	}

	/**
	 * 为待测试类创建目标类型的运行器。<br>
	 * 
	 * @param runnerClass
	 *          运行器类型。
	 * @param testClass
	 *          待测试类。
	 * @return 运行器。
	 * @throws Exception
	 *           运行器初始化异常。
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