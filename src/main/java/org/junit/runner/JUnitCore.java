package org.junit.runner;

import java.util.ArrayList;
import java.util.List;

import junit.runner.Version;
import org.junit.internal.JUnitSystem;
import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

/**
 * Junit的入口。
 * 
 * @see org.junit.runner.Result
 * @see org.junit.runner.notification.RunListener
 * @see org.junit.runner.Request
 * @since 4.0
 * @author 注释By JavaSking 2017年2月5日
 */
public class JUnitCore {
	/**
	 * 运行监听管理者。
	 */
	private final RunNotifier fNotifier= new RunNotifier();

	/**
	 * 入口方法。测试目标参数指定的测试类并退出。
	 * 
	 * @param args
	 *            目标测试类。
	 */
	public static void main(String... args) {

		runMainAndExit(new RealSystem(), args);
	}

	/**
	 * 测试目标类并退出。
	 * 
	 * @param system
	 *            系统。
	 * @param args
	 *            目标测试类。
	 */
	private static void runMainAndExit(JUnitSystem system, String... args) {

		Result result= new JUnitCore().runMain(system, args);
		System.exit(result.wasSuccessful() ? 0 : 1);
	}

	/**
	 * 测试目标类并返回测试结果。
	 * 
	 * @param computer
	 *            计算机（代表测试运行策略的抽象）。
	 * @param classes
	 *            待测试类。
	 * @return 测试结果。
	 */
	public static Result runClasses(Computer computer, Class<?>... classes) {

		return new JUnitCore().run(computer, classes);
	}

	/**
	 * 测试目标类并返回测试结果。
	 * 
	 * @param classes
	 *            待测试类。
	 * @return 测试结果。
	 */
	public static Result runClasses(Class<?>... classes) {

		return new JUnitCore().run(defaultComputer(), classes);
	}

	/**
	 * 测试目标类，收集测试结果并返回。
	 * 
	 * @param system
	 *            系统。
	 * @param args
	 *            测试类。
	 * @return 测试结果。
	 */
	private Result runMain(JUnitSystem system, String... args) {

		system.out().println("JUnit version " + Version.id());
		List<Class<?>> classes= new ArrayList<Class<?>>();
		List<Failure> missingClasses= new ArrayList<Failure>();
		for (String each : args) {
			try {
				classes.add(Class.forName(each));
			} catch (ClassNotFoundException e) {
				system.out().println("Could not find class: " + each);
				Description description= Description.createSuiteDescription(each);
				Failure failure= new Failure(description, e);
				missingClasses.add(failure);
			}
		}
		RunListener listener= new TextListener(system);
		addListener(listener);
		Result result= run(classes.toArray(new Class[0]));
		for (Failure each : missingClasses) {
			result.getFailures().add(each);
		}
		return result;
	}

	/**
	 * 获取Junit版本。
	 * 
	 * @return Junit版本。
	 */
	public String getVersion() {

		return Version.id();
	}

	/**
	 * 测试目标类，并返回测试结果。
	 * 
	 * @param classes
	 *            待测试的目标类。
	 * @return 测试结果。
	 */
	public Result run(Class<?>... classes) {

		return run(Request.classes(defaultComputer(), classes));
	}

	/**
	 * 测试目标类并返回测试结果。
	 * 
	 * @param computer
	 *            计算机（代表测试运行策略的抽象）。
	 * @param classes
	 *            待测试类。
	 * @return 测试结果。
	 */
	public Result run(Computer computer, Class<?>... classes) {

		return run(Request.classes(computer, classes));
	}

	/**
	 * 执行目标测试请求，并返回测试结果。
	 * 
	 * @param request
	 *            测试请求。
	 * @return 测试结果。
	 */
	public Result run(Request request) {

		return run(request.getRunner());
	}

	/**
	 * 运行JUnit 3.8.x风格的测试并返回测试结果，向后兼容。
	 * 
	 * @param test
	 *            旧风格的测试对象。
	 * @return 测试结果。
	 */
	public Result run(junit.framework.Test test) {

		return run(new JUnit38ClassRunner(test));
	}

	/**
	 * 运行测试。
	 * 
	 * @param runner
	 *            运行器。
	 * @return 测试结果。
	 */
	public Result run(Runner runner) {

		Result result= new Result();
		RunListener listener= result.createListener();
		fNotifier.addFirstListener(listener);
		try {
			fNotifier.fireTestRunStarted(runner.getDescription());
			runner.run(fNotifier);
			fNotifier.fireTestRunFinished(result);
		} finally {
			removeListener(listener);
		}
		return result;
	}

	/**
	 * 注册目标测试监听器。
	 * 
	 * @param listener
	 *            待注册的目标测试监听器。
	 */
	public void addListener(RunListener listener) {

		fNotifier.addListener(listener);
	}

	/**
	 * 移除目标测试监听器。
	 * 
	 * @param listener
	 *            待移除的目标测试监听器。
	 */
	public void removeListener(RunListener listener) {

		fNotifier.removeListener(listener);
	}

	/**
	 * 创建一个序列化运行测试的计算机。
	 * 
	 * @return 序列化运行测试的计算机。
	 */
	static Computer defaultComputer() {

		return new Computer();
	}
}
