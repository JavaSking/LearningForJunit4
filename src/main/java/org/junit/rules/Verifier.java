package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * 执行验证测试结果正确性规则。<br>
 * 例子：
 * 
 * <pre>
 *     public static class ErrorLogVerifier() {
 *     
 *        private ErrorLog errorLog = new ErrorLog();
 *
 *        &#064;Rule
 *        public Verifier verifier = new Verifier() {
 *           &#064;Override public void verify() {
 *              assertTrue(errorLog.isEmpty());
 *           }
 *        }
 *
 *        &#064;Test 
 *        public void testThatMightWriteErrorLog() {
 *           // ...
 *        }
 *     }
 * </pre>
 *
 * @since 4.7
 * @author 注释By JavaSking 2017年2月6日
 */
public abstract class Verifier implements TestRule {

	public Statement apply(final Statement base, Description description) {

		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				base.evaluate();
				verify();
			}
		};
	}

	/**
	 * 验证测试执行结果是否正确，如果错误则抛出异常表明验证失败，子类实现。
	 * 
	 * @throws Throwable
	 *             异常，表明验证失败。
	 */
	protected void verify() throws Throwable {

	}
}
