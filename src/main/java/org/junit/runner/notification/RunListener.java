package org.junit.runner.notification;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Result;

/**
 * 运行监听器。
 * 
 * @see org.junit.runner.JUnitCore
 * @since 4.0
 * @author 注释By JavaSking 2017年2月5日
 */
public class RunListener {

	/**
	 * 监听测试开始事件，在所有测试开始前调用。
	 * 
	 * @param description
	 *            测试内容描述信息。
	 * @throws Exception
	 *             异常。
	 */
	public void testRunStarted(Description description) throws Exception {

	}

	/**
	 * 监听所有测试完成事件，在所有测试完成时调用。
	 * 
	 * @param result
	 *            测试结果。
	 * @throws Exception
	 *             异常。
	 */
	public void testRunFinished(Result result) throws Exception {

	}

	/**
	 * 监听目标原子测试开始事件。
	 * 
	 * @param description
	 *            目标原子测试内容描述信息。
	 * @throws Exception
	 *             异常。
	 */
	public void testStarted(Description description) throws Exception {

	}

	/**
	 * 监听目标原子测试完成事件。
	 * 
	 * @param description
	 *            目标原子测试内容描述信息。
	 * @throws Exception
	 *             异常。
	 */
	public void testFinished(Description description) throws Exception {

	}

	/**
	 * 监听目标原子测试失败事件。
	 * 
	 * @param failure
	 *            发生错误的测试包装对象。
	 * @throws Exception
	 *             异常。
	 */
	public void testFailure(Failure failure) throws Exception {

	}

	/**
	 * 监听目标原子测试发生入参不符合预期错误事件。<br>
	 * 发生入参不符合预期错误将抛出{@code AssumptionViolatedException}异常。
	 * 
	 * @param failure
	 *            发生错误的测试包装对象。
	 */
	public void testAssumptionFailure(Failure failure) {

	}

	/**
	 * 监听目标原子测试被跳过事件。
	 * 
	 * @param description
	 *            目标原子测试内容描述信息。
	 * @throws Exception
	 *             异常。
	 */
	public void testIgnored(Description description) throws Exception {

	}
}
