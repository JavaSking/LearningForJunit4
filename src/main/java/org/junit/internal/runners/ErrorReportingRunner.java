package org.junit.internal.runners;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

/**
 * 错误报告运行器。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class ErrorReportingRunner extends Runner {
	/**
	 * 异常列表。
	 */
	private final List<Throwable> fCauses;

	/**
	 * 测试类。
	 */
	private final Class<?> fTestClass;

	/**
	 * 构造一个新的错误报告运行器。
	 * 
	 * @param testClass
	 *            测试类。
	 * @param cause
	 *            异常列表。
	 */
	public ErrorReportingRunner(Class<?> testClass, Throwable cause) {

		fTestClass= testClass;
		fCauses= getCauses(cause);
	}

	@Override
	public Description getDescription() {

		Description description= Description.createSuiteDescription(fTestClass);
		for (Throwable each : fCauses) {
			description.addChild(describeCause(each));
		}
		return description;
	}

	@Override
	public void run(RunNotifier notifier) {

		for (Throwable each : fCauses) {
			runCause(each, notifier);
		}
	}

	/**
	 * 获取造成目标异常的原因列表。
	 * 
	 * @param cause
	 *            目标异常。
	 * @return 造成目标异常的原因列表。
	 */
	@SuppressWarnings("deprecation")
	private List<Throwable> getCauses(Throwable cause) {
		
		if (cause instanceof InvocationTargetException) {
			return getCauses(cause.getCause());
		}
		if (cause instanceof InitializationError) {
			return ((InitializationError) cause).getCauses();
		}
		if (cause instanceof org.junit.internal.runners.InitializationError) {
			return ((org.junit.internal.runners.InitializationError) cause).getCauses();
		}
		return Arrays.asList(cause);
	}

	/**
	 * 创建描述异常的测试内容描述信息。<br>
	 * 该节点将被添加为发生此异常的测试类的测试内容描述信息的子节点。
	 * 
	 * @param child
	 *            异常。
	 * @return 描述异常的测试内容描述信息。
	 */
	private Description describeCause(Throwable child) {

		return Description.createTestDescription(fTestClass, "initializationError");
	}

	/**
	 * 通知目标运行监听器管理者通知目标测试类发生错误。
	 * 
	 * @param child
	 *            发生的错误。
	 * @param notifier
	 *            运行监听器管理者。
	 */
	private void runCause(Throwable child, RunNotifier notifier) {

		Description description= describeCause(child);
		notifier.fireTestStarted(description);
		notifier.fireTestFailure(new Failure(description, child));
		notifier.fireTestFinished(description);
	}
}