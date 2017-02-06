package org.junit.runner;

import org.junit.runner.notification.RunNotifier;

/**
 * 测试运行器。<br>
 * 测试运行器运行测试并负责通知测试运行监听器管理者进行测试事件通知。
 * 
 * @see org.junit.runner.Description
 * @see org.junit.runner.RunWith
 * @since 4.0
 * @author 注释By JavaSking 2017年2月5日
 */
public abstract class Runner implements Describable {

	public abstract Description getDescription();

	/**
	 * 运行测试。
	 * 
	 * @param notifier
	 *            测试运行监听器管理者。
	 */
	public abstract void run(RunNotifier notifier);

	/**
	 * 统计原子测试数。
	 * 
	 * @return 原子测试数。
	 */
	public int testCount() {

		return getDescription().testCount();
	}
}