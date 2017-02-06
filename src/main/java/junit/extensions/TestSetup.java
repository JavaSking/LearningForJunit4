package junit.extensions;

import junit.framework.Protectable;
import junit.framework.Test;
import junit.framework.TestResult;

/**
 * 测试对象的包装器，可以添加测试前置动作和后置动作。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class TestSetup extends TestDecorator {

	/**
	 * 构造一个可以添加测试前置动作和后置动作的测试对象的包装器。
	 * 
	 * @param test
	 *            目标测试对象。
	 */
	public TestSetup(Test test) {

		super(test);
	}

	@Override
	public void run(final TestResult result) {

		Protectable p= new Protectable() {

			public void protect() throws Exception {
				setUp();
				basicRun(result);
				tearDown();
			}
		};
		result.runProtected(this, p);
	}

	/**
	 * 测试的前置动作。
	 * 
	 * @throws Exception
	 *             异常。
	 */
	protected void setUp() throws Exception {

	}

	/**
	 * 测试的后置动作。
	 * 
	 * @throws Exception
	 *             异常。
	 */
	protected void tearDown() throws Exception {

	}
}