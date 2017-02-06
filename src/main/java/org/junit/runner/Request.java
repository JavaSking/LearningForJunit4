package org.junit.runner;

import java.util.Comparator;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.internal.requests.ClassRequest;
import org.junit.internal.requests.FilterRequest;
import org.junit.internal.requests.SortingRequest;
import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.manipulation.Filter;
import org.junit.runners.model.InitializationError;

/**
 * ��������Ϊ��֧�ֹ��˺�������������˲�������
 * 
 * @since 4.0
 * @author ע��By JavaSking 2017��2��5��
 */
public abstract class Request {

	/**
	 * ��������Ŀ�����Ŀ�귽���Ĳ�������
	 * 
	 * @param clazz
	 *            Ŀ������ࡣ
	 * @param methodName
	 *            Ŀ����Է�����
	 * @return ��������
	 */
	public static Request method(Class<?> clazz, String methodName) {

		Description method= Description.createTestDescription(clazz,
				methodName);
		return Request.aClass(clazz).filterWith(method);
	}

	/**
	 * ��������Ŀ����Ĳ�������
	 * 
	 * @param clazz
	 *            Ŀ������ࡣ
	 * @return ��������
	 */
	public static Request aClass(Class<?> clazz) {

		return new ClassRequest(clazz);
	}

	/**
	 * Create a <code>Request</code> that, when processed, will run all the
	 * tests in a class. If the class has a suite() method, it will be ignored.
	 *
	 * @param clazz
	 *            the class containing the tests
	 * @return a <code>Request</code> that will cause all tests in the class to
	 *         be run
	 */
	public static Request classWithoutSuiteMethod(Class<?> clazz) {
		
		return new ClassRequest(clazz, false);
	}

	/**
	 * ��������Ŀ������������
	 * 
	 * @param computer �����ϵͳ��
	 * @param classes �������ࡣ
	 * @return ��������
	 */
	public static Request classes(Computer computer, Class<?>... classes) {
		try {
			AllDefaultPossibilitiesBuilder builder= new AllDefaultPossibilitiesBuilder(
					true);
			Runner suite= computer.getSuite(builder, classes);
			return runner(suite);
		} catch (InitializationError e) {
			throw new RuntimeException(
					"Bug in saff's brain: Suite constructor, called as above, should always complete");
		}
	}

	/**
	 * ��������Ŀ����Ĳ�������
	 * 
	 * @param classes �������ࡣ
	 * @return ����Ŀ����Ĳ�������
	 */
	public static Request classes(Class<?>... classes) {
		
		return classes(JUnitCore.defaultComputer(), classes);
	}

	/**
	 * Not used within JUnit. Clients should simply instantiate
	 * ErrorReportingRunner themselves
	 */
	@Deprecated
	public static Request errorReport(Class<?> klass, Throwable cause) {
		return runner(new ErrorReportingRunner(klass, cause));
	}

	/**
	 * ����ʹ��Ŀ��������������еĲ�������
	 * 
	 * @param runner ������������
	 * @return ʹ��Ŀ��������������еĲ�������
	 */
	public static Request runner(final Runner runner) {
		
		return new Request() {
			@Override
			public Runner getRunner() {
				return runner;
			}
		};
	}

	/**
	 * Ϊ��ǰ�������󴴽�������������
	 * 
	 * @return ��ǰ��������ʹ�õĲ�����������
	 */
	public abstract Runner getRunner();

	/**
	 * ʹ��Ŀ����������˲�������
	 * 
	 * @param filter
	 *            Ŀ���������
	 * @return ���˺�Ĳ�������
	 */
	public Request filterWith(Filter filter) {

		return new FilterRequest(this, filter);
	}

	/**
	 * ��ȡֻ����Ŀ����ԵĲ�������
	 * 
	 * @param desiredDescription
	 *            �����е�Ŀ���������������Ϣ��
	 * @return ֻ����Ŀ����ԵĲ�������
	 */
	public Request filterWith(final Description desiredDescription) {

		return filterWith(Filter.matchMethodDescription(desiredDescription));
	}

	/**
	 * ʹ��Ŀ��������������򻯲�������
	 * 
	 * @param comparator Ŀ�������������
	 * @return ���򻯲�������
	 */
	public Request sortWith(Comparator<Description> comparator) {
		
		return new SortingRequest(this, comparator);
	}
}
