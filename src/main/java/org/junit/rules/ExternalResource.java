package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * �����ⲿ��Դ�������<br>
 * ���ӣ�
 * 
 * <pre>
 * public static class UsesExternalResource {
 * 
 * 	Server myServer= new Server();
 *
 * 	&#064;Rule
 * 	public ExternalResource resource= new ExternalResource() {
 * 		&#064;Override
 * 		protected void before() throws Throwable {
 * 			myServer.connect();
 * 		};
 *
 * 		&#064;Override
 * 		protected void after() {
 * 			myServer.disconnect();
 * 		};
 * 	};
 *
 * 	&#064;Test
 * 	public void testFoo() {
 * 		new Client().run(myServer);
 * 	}
 * }
 * </pre>
 *
 * @since 4.7
 * @author ע��By JavaSking 2017��2��6��
 */
public abstract class ExternalResource implements TestRule {

	public Statement apply(Statement base, Description description) {

		return statement(base);
	}

	/**
	 * ��дԴ���������ǰ��/���ö����������ؽ��������
	 * 
	 * @param base
	 *            Դ������
	 * @return ���������
	 */
	private Statement statement(final Statement base) {

		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				before();// ǰ�ö���
				try {
					base.evaluate();
				} finally {
					after();// ���ö�����һ��ִ��
				}
			}
		};
	}

	/**
	 * ǰ�ö�����������ʵ���ⲿ��Դ����
	 * 
	 * @throws Throwable
	 *             �쳣��
	 */
	protected void before() throws Throwable {

	}

	/**
	 * ���ö�����������ʵ���ⲿ��Դ����һ��ִ�С�
	 */
	protected void after() {

	}
}
