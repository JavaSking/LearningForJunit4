package org.junit.rules;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

/**
 * 该规则负责监视测试方法生命周期的各个阶段。<br>
 * TestWatcher及其子类不会改变测试的任何行为，提供了succeeded、failded、skipped、<br>
 * starting、finished方法监控一个测试方法生命周期的各个阶段。
 * 
 * 例子：
 * 
 * <pre>
 * public static class WatchmanTest {
 * 
 * 	private static String watchedLog;
 *
 * 	&#064;Rule
 * 	public TestWatcher watchman= new TestWatcher() {
 * 
 * 		&#064;Override
 * 		protected void failed(Throwable e, Description description) {
 * 
 * 			watchedLog+= description + &quot;\n&quot;;
 * 		}
 *
 * 		&#064;Override
 * 		protected void succeeded(Description description) {
 * 
 * 			watchedLog+= description + &quot; &quot; + &quot;success!\n&quot;;
 * 		}
 * 	};
 *
 * 	&#064;Test
 * 	public void fails() {
 * 
 * 		fail();
 * 	}
 *
 * 	&#064;Test
 * 	public void succeeds() {
 * 
 * 	}
 * }
 * </pre>
 *
 * @since 4.9
 * 
 * @author 注释By JavaSking 2017年2月6日
 */
public abstract class TestWatcher implements TestRule {

	public Statement apply(final Statement base, final Description description) {
		
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {

				List<Throwable> errors= new ArrayList<Throwable>();

				startingQuietly(description, errors);
				try {
					base.evaluate();
					succeededQuietly(description, errors);
				} catch (AssumptionViolatedException e) {
					errors.add(e);
					skippedQuietly(e, description, errors);
				} catch (Throwable t) {
					errors.add(t);
					failedQuietly(t, description, errors);
				} finally {
					finishedQuietly(description, errors);
				}
				MultipleFailureException.assertEmpty(errors);
			}
		};
	}

	/**
	 * 执行原子测试成功事件监听逻辑。
	 * 
	 * @param description
	 *            测试内容描述信息。
	 * @param errors
	 *            异常列表。
	 */
	private void succeededQuietly(Description description, List<Throwable> errors) {

		try {
			succeeded(description);
		} catch (Throwable t) {
			errors.add(t);
		}
	}

	/**
	 * 执行原子测试失败事件监听逻辑。
	 * 
	 * @param t
	 *            发生的异常。
	 * @param description
	 *            测试内容描述信息。
	 * @param errors
	 *            异常列表。
	 */
	private void failedQuietly(Throwable t, Description description, List<Throwable> errors) {

		try {
			failed(t, description);
		} catch (Throwable t1) {
			errors.add(t1);
		}
	}

	/**
	 * 执行原子测试入参假设失败事件监听逻辑。
	 * 
	 * @param e
	 *            入参假设失败异常。
	 * @param description
	 *            测试内容描述信息。
	 * @param errors
	 *            异常列表。
	 */
	private void skippedQuietly(AssumptionViolatedException e, Description description, List<Throwable> errors) {
		
		try {
			skipped(e, description);
		} catch (Throwable t) {
			errors.add(t);
		}
	}

	/**
	 * 执行原子测试开始事件监听逻辑。
	 * 
	 * @param description
	 *            测试内容描述信息。
	 * @param errors
	 *            异常列表。
	 */
	private void startingQuietly(Description description, List<Throwable> errors) {

		try {
			starting(description);
		} catch (Throwable t) {
			errors.add(t);
		}
	}

	/**
	 * 执行原子测试完成事件监听逻辑。
	 * 
	 * @param description
	 *            测试内容描述信息。
	 * @param errors
	 *            异常列表。
	 */
	private void finishedQuietly(Description description, List<Throwable> errors) {
		
		try {
			finished(description);
		} catch (Throwable t) {
			errors.add(t);
		}
	}

	/**
	 * 监听原子测试成功事件。
	 * 
	 * @param description
	 *            测试内容描述信息。
	 */
	protected void succeeded(Description description) {

	}

	/**
	 * 监听原子测试失败事件。
	 * 
	 * @param e
	 *            发生的异常。
	 * @param description
	 *            测试内容描述信息。
	 */
	protected void failed(Throwable e, Description description) {

	}

	/**
	 * 监听原子测试入参假设失败事件。
	 * 
	 * @param e
	 *            入参假设失败异常。
	 * @param description
	 *            测试内容描述信息。
	 */
	protected void skipped(AssumptionViolatedException e, Description description) {

	}

	/**
	 * 监听原子测试开始事件。
	 * 
	 * @param description
	 *            测试内容描述信息。
	 */
	protected void starting(Description description) {

	}

	/**
	 * 监听原子测试完成事件（无论失败还是成功）。
	 * 
	 * @param description
	 *            测试内容描述信息。
	 */
	protected void finished(Description description) {

	}
}
