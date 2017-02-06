package org.junit.runner.notification;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Result;

/**
 * 运行监听器管理者。
 * 
 * @since 4.0
 * @author 注释By JavaSking 2017年2月5日
 */
public class RunNotifier {
	
	/**
	 * 当前注册的运行监听器列表。
	 */
	private final List<RunListener> fListeners= Collections.synchronizedList(new ArrayList<RunListener>());

	/**
	 * 是否人工终止的标志。
	 */
	private volatile boolean fPleaseStop= false;

	/**
	 * 注册目标运行监听器。
	 * 
	 * @param listener
	 *            待注册的目标运行监听器。
	 */
	public void addListener(RunListener listener) {

		fListeners.add(listener);
	}

	/**
	 * 移除目标运行监听器。
	 * 
	 * @param listener
	 *            待移除的目标运行监听器。
	 */
	public void removeListener(RunListener listener) {

		fListeners.remove(listener);
	}

	/**
	 * 线程安全的运行监听器管理者。
	 * 
	 * @author 注释By JavaSking 2017年2月5日
	 */
	private abstract class SafeNotifier {

		/**
		 * 管理的运行监听器列表。
		 */
		private final List<RunListener> fCurrentListeners;

		/**
		 * 构造一个线程安全的运行监听器管理者。<br>
		 * 默认管理当前注册的所有运行监听器。
		 */
		SafeNotifier() {

			this(fListeners);
		}

		/**
		 * 构造一个管理目标运行监听器的运行监听器管理者。
		 * 
		 * @param currentListeners
		 *            目标运行监听器列表。
		 */
		SafeNotifier(List<RunListener> currentListeners) {

			fCurrentListeners= currentListeners;
		}

		/**
		 * 运行管理。
		 */
		void run() {

			synchronized (fListeners) {

				List<RunListener> safeListeners= new ArrayList<RunListener>();
				List<Failure> failures= new ArrayList<Failure>();
				for (Iterator<RunListener> all= fCurrentListeners.iterator(); all.hasNext();) {
					try {
						RunListener listener= all.next();
						notifyListener(listener);
						safeListeners.add(listener);
					} catch (Exception e) {
						failures.add(new Failure(Description.TEST_MECHANISM, e));
					}
				}
				fireTestFailures(safeListeners, failures);
			}
		}

		/**
		 * 通知运行监听器。
		 * 
		 * @param each
		 *            待通知的运行监听器。
		 * @throws Exception
		 *             异常。
		 */
		abstract protected void notifyListener(RunListener each) throws Exception;
	}

	/**
	 * 通知当前注册的所有运行监听器测试开始。
	 * 
	 * @param description
	 *            测试内容描述信息。
	 */
	public void fireTestRunStarted(final Description description) {

		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {

				each.testRunStarted(description);
			};
		}.run();
	}

	/**
	 * 通知当前注册的所有运行监听器所有测试结束。
	 * 
	 * @param result
	 *            测试结果。
	 */
	public void fireTestRunFinished(final Result result) {

		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {

				each.testRunFinished(result);
			};
		}.run();
	}

	/**
	 * 通知当前注册的所有运行监听器目标原子测试开始。
	 * 
	 * @param description
	 *            目标测试内容描述信息。
	 * @throws StoppedByUserException
	 *             人为终止异常。
	 */
	public void fireTestStarted(final Description description) throws StoppedByUserException {

		if (fPleaseStop) {
			throw new StoppedByUserException();
		}
		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {
				
				each.testStarted(description);
			};
		}.run();
	}

	/**
	 * 通知当前注册的所有运行监听器目标原子测试发生错误。
	 * 
	 * @param failure
	 *            发生错误的测试包装对象。
	 */
	public void fireTestFailure(Failure failure) {

		fireTestFailures(fListeners, asList(failure));
	}

	/**
	 * 通知目标运行监听器目标原子测试发生错误。
	 * 
	 * @param listeners
	 *            待通知的运行监听器列表。
	 * @param failures
	 *            发生错误的测试包装对象列表。
	 */
	private void fireTestFailures(List<RunListener> listeners, final List<Failure> failures) {

		if (!failures.isEmpty()) {
			new SafeNotifier(listeners) {
				@Override
				protected void notifyListener(RunListener listener) throws Exception {
					
					for (Failure each : failures) {
						listener.testFailure(each);
					}
				};
			}.run();
		}
	}

	/**
	 * 通知当前注册的所有运行监听器目标原子发生入参不符合预期的错误。
	 * 
	 * @param failure
	 *            发生错误的测试包装对象。
	 */
	public void fireTestAssumptionFailed(final Failure failure) {

		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {

				each.testAssumptionFailure(failure);
			};
		}.run();
	}

	/**
	 * 通知当前注册的所有运行监听器目标原子测试将跳过。
	 * 
	 * @param description
	 *            目标原子测试内容描述信息。
	 */
	public void fireTestIgnored(final Description description) {

		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {

				each.testIgnored(description);
			}
		}.run();
	}

	/**
	 * 通知当前注册的所有运行监听器目标原子测试结束。
	 * 
	 * @param description
	 *            目标原子测试内容描述信息。
	 */
	public void fireTestFinished(final Description description) {

		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {

				each.testFinished(description);
			};
		}.run();
	}

	/**
	 * 人为终止测试。
	 */
	public void pleaseStop() {

		fPleaseStop= true;
	}

	/**
	 * 注册运行监听器并前置。
	 * 
	 * @param listener
	 *            待注册的运行监听器。
	 */
	public void addFirstListener(RunListener listener) {

		fListeners.add(0, listener);
	}
}