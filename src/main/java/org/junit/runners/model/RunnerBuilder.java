package org.junit.runners.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Runner;

/**
 * 运行器创建工厂。<br>
 * 设计模式：职责链模式。
 * 
 * @see org.junit.runners.Suite
 * @since 4.5
 * @author 注释By JavaSking 2017年2月6日
 */
public abstract class RunnerBuilder {

	/**
	 * 测试类集合。
	 */
	private final Set<Class<?>> parents = new HashSet<Class<?>>();

	/**
	 * 获取目标测试类的运行器，若运行器创建失败则抛出异常。
	 * 
	 * @param testClass
	 *          待测试类。
	 * @return 目标测试类的运行器，可能为null。
	 * @throws Throwable
	 *           运行器创建异常。
	 */
	public abstract Runner runnerForClass(Class<?> testClass) throws Throwable;

	/**
	 * 获取目标测试类的运行器，若运行器创建失败则创建一个错误报告运行器，返回运行器。
	 * 
	 * @param testClass
	 *          待测试类。
	 * @return 运行器。
	 */
	public Runner safeRunnerForClass(Class<?> testClass) {

		try {
			return runnerForClass(testClass);
		} catch (Throwable e) {
			return new ErrorReportingRunner(testClass, e);
		}
	}

	/**
	 * 添加待测试类并返回。
	 * 
	 * @param parent
	 *          待测试类。
	 * @return 待测试类。
	 * @throws InitializationError
	 *           运行器初始化异常。
	 */
	Class<?> addParent(Class<?> parent) throws InitializationError {

		if (!parents.add(parent)) {
			throw new InitializationError(String.format("class '%s' (possibly indirectly) contains itself as a SuiteClass", parent.getName()));
		}
		return parent;
	}

	/**
	 * 移除目标待测试类。
	 * 
	 * @param klass
	 *          待移除的测试类。
	 */
	void removeParent(Class<?> klass) {

		parents.remove(klass);
	}

	public List<Runner> runners(Class<?> parent, Class<?>[] children) throws InitializationError {

		addParent(parent);
		try {
			return runners(children);
		} finally {
			removeParent(parent);
		}
	}

	public List<Runner> runners(Class<?> parent, List<Class<?>> children) throws InitializationError {

		return runners(parent, children.toArray(new Class<?>[0]));
	}

	/**
	 * 获取测试类的运行器。
	 * 
	 * @param children
	 *          测试类数组。
	 * @return 运行器数组。
	 */
	private List<Runner> runners(Class<?>[] children) {

		ArrayList<Runner> runners = new ArrayList<Runner>();
		for (Class<?> each : children) {
			Runner childRunner = safeRunnerForClass(each);
			if (childRunner != null) {
				runners.add(childRunner);
			}
		}
		return runners;
	}
}
