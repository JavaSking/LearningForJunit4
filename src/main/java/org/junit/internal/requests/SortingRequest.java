package org.junit.internal.requests;

import java.util.Comparator;

import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Sorter;

/**
 * ���򻯵Ĳ�������
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class SortingRequest extends Request {
	/**
	 * ԭʼ�Ĳ�������
	 */
	private final Request fRequest;

	/**
	 * ������������
	 */
	private final Comparator<Description> fComparator;

	/**
	 * ����һ�����򻯲�������
	 * 
	 * @param request
	 *            ԭʼ�Ĳ�������
	 * 
	 * @param comparator
	 *            ������������
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
