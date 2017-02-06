package org.junit.rules;

import org.junit.runner.Description;

/**
 * 该规则提供获取每个测试名称的能力。<br>
 * 例子：
 * 
 * <pre>
 * public class TestNameTest {
 * 	&#064;Rule
 * 	public TestName name= new TestName();
 *
 * 	&#064;Test
 * 	public void testA() {
 * 		assertEquals(&quot;testA&quot;, name.getMethodName());
 * 	}
 *
 * 	&#064;Test
 * 	public void testB() {
 * 		assertEquals(&quot;testB&quot;, name.getMethodName());
 * 	}
 * }
 * </pre>
 *
 * @since 4.7
 * @author 注释By JavaSking 2017年2月6日
 */
public class TestName extends TestWatcher {
	/**
	 * 测试名称（测试方法名）。
	 */
	private String fName;

	@Override
	protected void starting(Description d) {

		fName= d.getMethodName();
	}

	/**
	 * 获取测试方法名。
	 * 
	 * @return 测试方法名。
	 */
	public String getMethodName() {

		return fName;
	}
}
