package org.junit.internal.requests;

import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;

/**
 * ���˻��Ĳ�������
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public final class FilterRequest extends Request {
	/**
	 * ԭʼ��������
	 */
	private final Request fRequest;

	/**
	 * ��������
	 */
	private final Filter fFilter;

	/**
	 * ����һ�����˻���������
	 * 
	 * @param classRequest
	 *            ԭʼ��������
	 * @param filter
	 *            ��������
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