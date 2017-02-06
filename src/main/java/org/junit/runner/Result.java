package org.junit.runner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * 测试结果，统计所有测试的测试信息，包括发生错误的测试。
 * 
 * @since 4.0
 * @author 注释By JavaSking 2017年2月5日
 */
public class Result implements Serializable {
	/**
	 * 序列号。
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * 测试计数器。
	 */
	private AtomicInteger fCount= new AtomicInteger();

	/**
	 * 跳过的测试计数器。
	 */
	private AtomicInteger fIgnoreCount= new AtomicInteger();

	/**
	 * 发生错误的测试的包装对象列表。
	 */
	private final List<Failure> fFailures= Collections.synchronizedList(new ArrayList<Failure>());

	/**
	 * 测试运行时长（毫秒）。
	 */
	private long fRunTime= 0;

	/**
	 * 测试开始时间。
	 */
	private long fStartTime;

	/**
	 * 统计测试数。
	 * 
	 * @return 测试数。
	 */
	public int getRunCount() {

		return fCount.get();
	}

	/**
	 * 统计发生错误的测试数。
	 * 
	 * @return 发生错误的测试数。
	 */
	public int getFailureCount() {

		return fFailures.size();
	}

	/**
	 * 统计测试时长（毫秒）。
	 * 
	 * @return 测试时长。
	 */
	public long getRunTime() {

		return fRunTime;
	}

	/**
	 * 获取发生错误的测试的包装对象列表。
	 * 
	 * @return 发生错误的测试的包装对象列表。
	 */
	public List<Failure> getFailures() {

		return fFailures;
	}

	/**
	 * 统计被跳过的测试数。
	 * 
	 * @return 被跳过的测试数。
	 */
	public int getIgnoreCount() {

		return fIgnoreCount.get();
	}

	/**
	 * 判断测试是否成功。
	 * 
	 * @return 如果测试成功则返回true，否则返回false。
	 */
	public boolean wasSuccessful() {

		return getFailureCount() == 0;
	}

	/**
	 * 测试运行监听器。
	 * 
	 * @author 注释By JavaSking 2017年2月5日
	 */
	private class Listener extends RunListener {

		@Override
		public void testRunStarted(Description description) throws Exception {

			fStartTime= System.currentTimeMillis();// 记录测试开始时间。
		}

		@Override
		public void testRunFinished(Result result) throws Exception {

			long endTime= System.currentTimeMillis();
			fRunTime+= endTime - fStartTime;// 计算测试时长。
		}

		@Override
		public void testFinished(Description description) throws Exception {

			fCount.getAndIncrement();
		}

		@Override
		public void testFailure(Failure failure) throws Exception {

			fFailures.add(failure);
		}

		@Override
		public void testIgnored(Description description) throws Exception {

			fIgnoreCount.getAndIncrement();
		}

		@Override
		public void testAssumptionFailure(Failure failure) {

		}
	}

	/**
	 * 新建一个测试运行监听器并返回。
	 * 
	 * @return 新建的测试运行器。
	 */
	public RunListener createListener() {

		return new Listener();
	}
}
