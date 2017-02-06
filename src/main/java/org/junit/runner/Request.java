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
 * 测试请求。为了支持过滤和排序操作引入了测试请求。
 * 
 * @since 4.0
 * @author 注释By JavaSking 2017年2月5日
 */
public abstract class Request {

	/**
	 * 创建测试目标类的目标方法的测试请求。
	 * 
	 * @param clazz
	 *            目标测试类。
	 * @param methodName
	 *            目标测试方法。
	 * @return 测试请求。
	 */
	public static Request method(Class<?> clazz, String methodName) {

		Description method= Description.createTestDescription(clazz,
				methodName);
		return Request.aClass(clazz).filterWith(method);
	}

	/**
	 * 创建测试目标类的测试请求。
	 * 
	 * @param clazz
	 *            目标测试类。
	 * @return 测试请求。
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
	 * 创建测试目标测试类的请求。
	 * 
	 * @param computer 计算机系统。
	 * @param classes 待测试类。
	 * @return 测试请求。
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
	 * 创建测试目标类的测试请求。
	 * 
	 * @param classes 待测试类。
	 * @return 测试目标类的测试请求。
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
	 * 创建使用目标测试运行器运行的测试请求。
	 * 
	 * @param runner 测试运行器。
	 * @return 使用目标测试运行器运行的测试请求。
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
	 * 为当前测试请求创建测试运行器。
	 * 
	 * @return 当前测试请求使用的测试运行器。
	 */
	public abstract Runner getRunner();

	/**
	 * 使用目标过滤器过滤测试请求。
	 * 
	 * @param filter
	 *            目标过滤器。
	 * @return 过滤后的测试请求。
	 */
	public Request filterWith(Filter filter) {

		return new FilterRequest(this, filter);
	}

	/**
	 * 获取只放行目标测试的测试请求。
	 * 
	 * @param desiredDescription
	 *            待放行的目标测试内容描述信息。
	 * @return 只放行目标测试的测试请求。
	 */
	public Request filterWith(final Description desiredDescription) {

		return filterWith(Filter.matchMethodDescription(desiredDescription));
	}

	/**
	 * 使用目标测试排序器排序化测试请求。
	 * 
	 * @param comparator 目标测试排序器。
	 * @return 排序化测试请求。
	 */
	public Request sortWith(Comparator<Description> comparator) {
		
		return new SortingRequest(this, comparator);
	}
}
