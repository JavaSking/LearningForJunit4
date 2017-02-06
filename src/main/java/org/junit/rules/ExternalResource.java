package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * 附加外部资源管理规则。<br>
 * 例子：
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
 * @author 注释By JavaSking 2017年2月6日
 */
public abstract class ExternalResource implements TestRule {

	public Statement apply(Statement base, Description description) {

		return statement(base);
	}

	/**
	 * 改写源动作，添加前置/后置动作，并返回结果动作。
	 * 
	 * @param base
	 *            源动作。
	 * @return 结果动作。
	 */
	private Statement statement(final Statement base) {

		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				before();// 前置动作
				try {
					base.evaluate();
				} finally {
					after();// 后置动作，一定执行
				}
			}
		};
	}

	/**
	 * 前置动作，重载以实现外部资源管理。
	 * 
	 * @throws Throwable
	 *             异常。
	 */
	protected void before() throws Throwable {

	}

	/**
	 * 后置动作，重载以实现外部资源管理，一定执行。
	 */
	protected void after() {

	}
}
