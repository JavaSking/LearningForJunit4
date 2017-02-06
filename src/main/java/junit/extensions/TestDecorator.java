package junit.extensions;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestResult;

/**
 * 测试对象的包装器。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class TestDecorator extends Assert implements Test {
	/**
	 * 潜在的测试对象。
	 */
	protected Test fTest;

	/**
	 * 构造一个目标测试对象的包装器。
	 * 
	 * @param test
	 *            目标测试对象。
	 */
	public TestDecorator(Test test) {

		fTest= test;
	}

	/**
	 * 运行基本的测试动作。
	 * 
	 * @param result
	 *            测试结果。
	 */
	public void basicRun(TestResult result) {

		fTest.run(result);
	}

	public int countTestCases() {

		return fTest.countTestCases();
	}

	public void run(TestResult result) {

		basicRun(result);
	}

	@Override
	public String toString() {

		return fTest.toString();
	}

	/**
	 * 获取潜在的测试对象。
	 * 
	 * @return 潜在的测试对象。
	 */
	public Test getTest() {

		return fTest;
	}
}