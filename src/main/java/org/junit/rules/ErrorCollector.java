package org.junit.rules;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.hamcrest.Matcher;
import org.junit.runners.model.MultipleFailureException;

/**
 * 该规则收集测试中出现的错误信息，但测试不中断，如果有错误发生测试结束后会标记测试失败。<br>
 * 例子：
 *
 * <pre>
 * public static class UsesErrorCollectorTwice {
 * 	&#064;Rule
 * 	public ErrorCollector collector= new ErrorCollector();
 *
 * 	&#064;Test
 * 	public void example() {
 * 
 * 		collector.addError(new Throwable(&quot;first thing went wrong&quot;));
 * 		collector.addError(new Throwable(&quot;second thing went wrong&quot;));
 * 		collector.checkThat(getResult(), not(containsString(&quot;ERROR!&quot;)));
 * 		// all lines will run, and then a combined failure logged at the end.
 * 	}
 * }
 * </pre>
 *
 * @since 4.7
 * @author 注释By JavaSking 2017年2月6日
 */
public class ErrorCollector extends Verifier {
	/**
	 * 异常列表。
	 */
	private List<Throwable> errors= new ArrayList<Throwable>();

	@Override
	protected void verify() throws Throwable {

		MultipleFailureException.assertEmpty(errors);
	}

	/**
	 * 记录目标异常。
	 * 
	 * @param error
	 *            目标异常。
	 */
	public void addError(Throwable error) {

		errors.add(error);
	}

	/**
	 * 检查目标是否匹配指定匹配器，如果不匹配则记录一条异常。
	 * 
	 * @param value
	 *            目标。
	 * @param matcher
	 *            指定匹配器。
	 */
	public <T> void checkThat(final T value, final Matcher<T> matcher) {

		checkThat("", value, matcher);
	}

	/**
	 * 检查目标是否匹配指定匹配器，如果不匹配则记录一条包含不匹配原因的异常。
	 * 
	 * @param reason
	 *            不匹配原因。
	 * @param value
	 *            目标。
	 * @param matcher
	 *            指定匹配器。
	 */
	public <T> void checkThat(final String reason, final T value, final Matcher<T> matcher) {

		checkSucceeds(new Callable<Object>() {
			
			public Object call() throws Exception {
				assertThat(reason, value, matcher);
				return value;
			}
		});
	}

	/**
	 * 执行任务调用，如果出现异常则记录，不中断程序执行。
	 * 
	 * @param callable
	 *            待调用任务。
	 * @return 任务调用完成则返回调用结果，发生异常则返回null。
	 */
	public Object checkSucceeds(Callable<Object> callable) {

		try {
			return callable.call();
		} catch (Throwable e) {
			addError(e);
			return null;
		}
	}
}