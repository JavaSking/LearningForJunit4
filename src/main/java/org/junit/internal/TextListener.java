package org.junit.internal;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * 负责记录测试信息的运行监听器。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class TextListener extends RunListener {

	/**
	 * 打印流。
	 */
	private final PrintStream fWriter;

	/**
	 * 构造一个负责记录测试信息的运行监听器。
	 * 
	 * @param system
	 *            系统。
	 */
	public TextListener(JUnitSystem system) {

		this(system.out());
	}

	/**
	 * 构造一个负责记录测试信息的运行监听器。
	 * 
	 * @param writer
	 *            打印流。
	 */
	public TextListener(PrintStream writer) {

		this.fWriter= writer;
	}

	@Override
	public void testRunFinished(Result result) {

		printHeader(result.getRunTime());
		printFailures(result);
		printFooter(result);
	}

	@Override
	public void testStarted(Description description) {

		fWriter.append('.');
	}

	@Override
	public void testFailure(Failure failure) {

		fWriter.append('E');
	}

	@Override
	public void testIgnored(Description description) {

		fWriter.append('I');
	}

	/**
	 * 获取打印流。
	 * 
	 * @return 打印流。
	 */
	private PrintStream getWriter() {

		return fWriter;
	}

	/**
	 * 打印头部信息：测试时长。
	 * 
	 * @param runTime
	 *            测试时长。
	 */
	protected void printHeader(long runTime) {

		getWriter().println();
		getWriter().println("Time: " + elapsedTimeAsString(runTime));
	}

	/**
	 * 打印发生错误的测试的测试信息。
	 * 
	 * @param result
	 *            测试结果。
	 */
	protected void printFailures(Result result) {

		List<Failure> failures= result.getFailures();
		if (failures.size() == 0) {
			return;
		}
		if (failures.size() == 1) {
			getWriter().println("There was " + failures.size() + " failure:");
		} else {
			getWriter().println("There were " + failures.size() + " failures:");
		}
		int i= 1;
		for (Failure each : failures) {
			printFailure(each, "" + i++);
		}
	}

	/**
	 * 打印单个发生错误的测试的测试信息。
	 * 
	 * @param each
	 *            发生错误的测试的包装对象。
	 * @param prefix
	 *            前缀。
	 */
	protected void printFailure(Failure each, String prefix) {

		getWriter().println(prefix + ") " + each.getTestHeader());
		getWriter().print(each.getTrace());
	}

	/**
	 * 打印尾部信息：成功/失败统计信息。
	 * 
	 * @param result
	 *            测试结果。
	 */
	protected void printFooter(Result result) {

		if (result.wasSuccessful()) {
			getWriter().println();
			getWriter().print("OK");
			getWriter().println(" (" + result.getRunCount() + " test" + (result.getRunCount() == 1 ? "" : "s") + ")");

		} else {
			getWriter().println();
			getWriter().println("FAILURES!!!");
			getWriter().println("Tests run: " + result.getRunCount() + ",  Failures: " + result.getFailureCount());
		}
		getWriter().println();
	}

	/**
	 * 格式化运行时长。
	 * 
	 * @param runTime
	 *            运行时长（毫秒）。
	 * @return 格式化后的运行时长。
	 */
	protected String elapsedTimeAsString(long runTime) {

		return NumberFormat.getInstance().format((double) runTime / 1000);
	}
}
