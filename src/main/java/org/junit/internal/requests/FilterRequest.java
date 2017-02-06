package org.junit.internal.requests;

import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;

/**
 * 过滤化的测试请求。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public final class FilterRequest extends Request {
	/**
	 * 原始测试请求。
	 */
	private final Request fRequest;

	/**
	 * 过滤器。
	 */
	private final Filter fFilter;

	/**
	 * 构造一个过滤化测试请求。
	 * 
	 * @param classRequest
	 *            原始测试请求。
	 * @param filter
	 *            过滤器。
	 */
	public FilterRequest(Request classRequest, Filter filter) {

		fRequest= classRequest;
		fFilter= filter;
	}

	@Override
	public Runner getRunner() {
		try {
			Runner runner= fRequest.getRunner();
			fFilter.apply(runner);
			return runner;
		} catch (NoTestsRemainException e) {
			return new ErrorReportingRunner(Filter.class, new Exception(String.format("No tests found matching %s from %s", fFilter.describe(), fRequest.toString())));
		}
	}
}