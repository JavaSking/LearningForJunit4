package org.junit.internal.builders;

import java.util.Arrays;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/**
 * 默认运行器创建工厂。采用类似于职责链的模式来创建运行器。<br>
 * 
 * @author 注释By JavaSking 2017年2月6日
 */
public class AllDefaultPossibilitiesBuilder extends RunnerBuilder {

	/**
	 * 表示测试类中静态的suite()方法是否被视为用于获得测试类运行的方法。
	 */
	private final boolean fCanUseSuiteMethod;

	/**
	 * 创建一个默认运行器创建工厂。
	 * 
	 * @param canUseSuiteMethod
	 *          测试类中静态的suite()方法是否有效。
	 */
	public AllDefaultPossibilitiesBuilder(boolean canUseSuiteMethod) {

		fCanUseSuiteMethod = canUseSuiteMethod;
	}

	@Override
	public Runner runnerForClass(Class<?> testClass) throws Throwable {

		List<RunnerBuilder> builders = Arrays.asList(ignoredBuilder(), annotatedBuilder(), suiteMethodBuilder(), junit3Builder(), junit4Builder());

		for (RunnerBuilder each : builders) {
			/* 采用类似于职责链的模式来创建运行器 */
			Runner runner = each.safeRunnerForClass(testClass);
			if (runner != null) {
				return runner;
			}
		}
		return null;
	}

	/**
	 * 获取负责创建BlockJUnit4ClassRunner运行器的运行器工厂。
	 * 
	 * @return 负责创建BlockJUnit4ClassRunner运行器的运行器工厂。
	 */
	protected JUnit4Builder junit4Builder() {

		return new JUnit4Builder();
	}

	/**
	 * 获取负责创建JUnit38ClassRunner运行器的运行器工厂。
	 * 
	 * @return 负责创建JUnit38ClassRunner运行器的运行器工厂。
	 */
	protected JUnit3Builder junit3Builder() {

		return new JUnit3Builder();
	}

	/**
	 * 获取负责@RunWith注解解析的运行器工厂。
	 * 
	 * @return 负责@RunWith注解解析的运行器工厂。
	 */
	protected AnnotatedBuilder annotatedBuilder() {

		return new AnnotatedBuilder(this);
	}

	/**
	 * 获取负责创建IgnoredClassRunner运行器的工厂。
	 * 
	 * @return 负责创建IgnoredClassRunner运行器的工厂。
	 */
	protected IgnoredBuilder ignoredBuilder() {

		return new IgnoredBuilder();
	}

	/**
	 * 如果参数fCanUseSuiteMethod有效则返回负责创建SuiteMethod运行器的运行器工厂。<br>
	 * 否则返回不创建任何运行器的运行器工厂。
	 * 
	 * @return 负责创建SuiteMethod运行器的运行器工厂或不创建任何运行器的运行器工厂。
	 */
	protected RunnerBuilder suiteMethodBuilder() {

		if (fCanUseSuiteMethod) {
			return new SuiteMethodBuilder();
		}
		return new NullBuilder();
	}
}