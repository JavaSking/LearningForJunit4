package junit.framework;

import org.junit.runner.Describable;
import org.junit.runner.Description;

/**
 * Junit4���������İ�װ����
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class JUnit4TestCaseFacade implements Test, Describable {
	/**
	 * ��������������Ϣ��
	 */
	private final Description fDescription;

	/**
	 * ����һ��Junit4���������İ�װ����
	 * 
	 * @param description ��������������Ϣ��
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