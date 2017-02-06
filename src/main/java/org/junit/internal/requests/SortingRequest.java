package org.junit.internal.requests;

import java.util.Comparator;

import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Sorter;

/**
 * 排序化的测试请求。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class SortingRequest extends Request {
	/**
	 * 原始的测试请求。
	 */
	private final Request fRequest;

	/**
	 * 测试排序器。
	 */
	private final Comparator<Description> fComparator;

	/**
	 * 构造一个排序化测试请求。
	 * 
	 * @param request
	 *            原始的测试请求。
	 * 
	 * @param comparator
	 *            测试排序器。
	 */
	public SortingRequest(Request request, Comparator<Description> comparator) {

		fRequest= request;
		fComparator= comparator;
	}

	@Override
	public Runner getRunner() {

		Runner runner= fRequest.getRunner();
		new Sorter(fComparator).apply(runner);
		return runner;
	}
}
