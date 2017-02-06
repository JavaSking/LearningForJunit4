package junit.framework;

import org.junit.runner.Describable;
import org.junit.runner.Description;

/**
 * Junit4测试用例的包装器。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class JUnit4TestCaseFacade implements Test, Describable {
	/**
	 * 测试内容描述信息。
	 */
	private final Description fDescription;

	/**
	 * 构造一个Junit4测试用例的包装器。
	 * 
	 * @param description 测试内容描述信息。
	 */
	JUnit4TestCaseFacade(Description description) {
		
		fDescription= description;
	}

	@Override
	public String toString() {
		
		return getDescription().toString();
	}

	public int countTestCases() {
		
		return 1;
	}

	public void run(TestResult result) {
		
		throw new RuntimeException("This test stub created only for informational purposes.");
	}

	public Description getDescription() {
		
		return fDescription;
	}
}