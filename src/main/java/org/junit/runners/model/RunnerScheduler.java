package org.junit.runners.model;

/**
 * 运行器调度器（在实现中）。
 * 
 * @since 4.7
 * @author 注释By JavaSking 2017年2月5日
 */
public interface RunnerScheduler {

	/**
	 * 调度目标线程。
	 * 
	 * @param childStatement
	 *            目标线程。
	 */
	void schedule(Runnable childStatement);

	/**
	 * 调度结束。
	 */
	void finished();
}
