package org.junit.rules;

import org.junit.runner.Description;

/**
 * �ù����ṩ��ȡÿ���������Ƶ�������<br>
 * ���ӣ�
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
 * @author ע��By JavaSking 2017��2��6��
 */
public class TestName extends TestWatcher {
	/**
	 * �������ƣ����Է���������
	 */
	private String fName;

	@Override
	protected void starting(Description d) {

		fName= d.getMethodName();
	}

	/**
	 * ��ȡ���Է�������
	 * 
	 * @return ���Է�������
	 */
	public String getMethodName() {

		return fName;
	}
}
