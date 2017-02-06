package org.junit.rules;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.internal.matchers.ThrowableCauseMatcher.hasCause;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.Statement;

/**
 * 异常测试规则，@Test(expected=xxx)用法本质就是利用了这个规则。<br>
 * 相比于@Test(expected=xxx)，异常测试规则提供了更为灵活匹配的规则。<br>
 * 可以根据message、cause、异常类型匹配。
 * 
 * 例子：
 * 
 * <pre>
 * // 以下测试全部通过。
 * public static class HasExpectedException {
 * 
 * 	&#064;Rule
 * 	public ExpectedException thrown= ExpectedException.none();
 *
 * 	&#064;Test
 * 	public void throwsNothing() {
 * 		// 未设置期望异常，未抛出异常：通过
 * 	}
 *
 * 	&#064;Test
 * 	public void throwsNullPointerException() {
 * 
 * 		thrown.expect(NullPointerException.class);// 期望抛出NullPointerException异常
 * 		throw new NullPointerException();
 * 	}
 *
 * 	&#064;Test
 * 	public void throwsNullPointerExceptionWithMessage() {
 * 
 * 		thrown.expect(NullPointerException.class);// 期望抛出NullPointerException异常
 * 		thrown.expectMessage(&quot;happened?&quot;);
 * 		thrown.expectMessage(startsWith(&quot;What&quot;));// 设置期望的异常信息
 * 		throw new NullPointerException(&quot;What happened?&quot;);
 * 	}
 *
 * 	&#064;Test
 * 	public void throwsIllegalArgumentExceptionWithMessageAndCause() {
 * 
 * 		NullPointerException expectedCause= new NullPointerException();
 * 		thrown.expect(IllegalArgumentException.class);
 * 		thrown.expectMessage(&quot;What&quot;);
 * 		thrown.expectCause(is(expectedCause));// 设置引起期望异常的原因
 * 		throw new IllegalArgumentException(&quot;What happened?&quot;, cause);
 * 	}
 * }
 * </pre>
 *
 * @since 4.7
 * @author 注释By JavaSking 2017年2月6日
 */
public class ExpectedException implements TestRule {

	/**
	 * 获取未期望异常规则。
	 * 
	 * @return 未期望异常规则。
	 */
	public static ExpectedException none() {

		return new ExpectedException();
	}

	/**
	 * 期望异常匹配器工厂对象。
	 */
	private final ExpectedExceptionMatcherBuilder fMatcherBuilder= new ExpectedExceptionMatcherBuilder();

	/**
	 * 是否处理{@code AssumptionViolatedException}，默认不处理。
	 */
	private boolean handleAssumptionViolatedExceptions= false;

	/**
	 * 是否处理{@code AssertionError}，默认不处理。
	 */
	private boolean handleAssertionErrors= false;

	/**
	 * 构造一个未期望异常规则。
	 */
	private ExpectedException() {

	}

	/**
	 * 设置处理{@code AssertionError}，返回当前异常测试规则。
	 * 
	 * @return 当前异常测试规则。
	 */
	public ExpectedException handleAssertionErrors() {

		handleAssertionErrors= true;
		return this;
	}

	/**
	 * 设置处理{@code AssumptionViolatedException}，返回当前异常测试规则。
	 * 
	 * @return 当前异常测试规则。
	 */
	public ExpectedException handleAssumptionViolatedExceptions() {

		handleAssumptionViolatedExceptions= true;
		return this;
	}

	public Statement apply(Statement base, org.junit.runner.Description description) {

		return new ExpectedExceptionStatement(base);
	}

	/**
	 * 新增目标匹配器。
	 * 
	 * @param matcher
	 *            待新增的匹配器。
	 */
	public void expect(Matcher<?> matcher) {

		fMatcherBuilder.add(matcher);
	}

	/**
	 * 新增异常类型匹配器。
	 * 
	 * @param type
	 *            异常类型。
	 */
	public void expect(Class<? extends Throwable> type) {

		expect(instanceOf(type));
	}

	/**
	 * 新增异常信息匹配器，只要包含目标信息子串则为匹配。<br>
	 * 
	 * 
	 * @param substring
	 *            匹配信息。
	 */
	public void expectMessage(String substring) {

		expectMessage(containsString(substring));
	}

	/**
	 * 新增目标异常信息匹配器。
	 * 
	 * @param matcher
	 *            目标异常信息匹配器。
	 */
	public void expectMessage(Matcher<String> matcher) {

		expect(hasMessage(matcher));
	}

	/**
	 * 新增目标异常原因匹配器。
	 * 
	 * @param expectedCause
	 *            目标异常原因匹配器。
	 */
	public void expectCause(Matcher<? extends Throwable> expectedCause) {

		expect(hasCause(expectedCause));
	}

	/**
	 * 期望抛出期望异常的动作，如果未抛出期望异常则表明测试失败。<br>
	 * 
	 * @author 注释By JavaSking 2017年2月5日
	 */
	private class ExpectedExceptionStatement extends Statement {

		/**
		 * 下一条动作。
		 */
		private final Statement fNext;

		/**
		 * 构造一个期望抛出期望异常的动作。
		 * 
		 * @param base
		 *            下一条动作。
		 */
		public ExpectedExceptionStatement(Statement base) {

			fNext= base;
		}

		@Override
		public void evaluate() throws Throwable {
			try {
				fNext.evaluate();
				/* 存在期望异常，但未抛出异常 */
				if (fMatcherBuilder.expectsThrowable()) {
					failDueToMissingException();
				}
			} catch (AssumptionViolatedException e) {
				optionallyHandleException(e, handleAssumptionViolatedExceptions);
			} catch (AssertionError e) {
				optionallyHandleException(e, handleAssertionErrors);
			} catch (Throwable e) {
				handleException(e);
			}
		}
	}

	/**
	 * 未抛出期望异常，表明测试失败并抛出断言错误。
	 * 
	 * @throws AssertionError
	 *             断言错误。
	 */
	private void failDueToMissingException() throws AssertionError {

		String expectation= StringDescription.toString(fMatcherBuilder.build());
		fail("Expected test to throw " + expectation);
	}

	/**
	 * 处理或不处理目标异常，是否处理取决于<code>handleException</code>标志。
	 * 
	 * @param e
	 *            可选处理的异常。
	 * @param handleException
	 *            是否进行处理。
	 * @throws Throwable
	 *             处理后抛出的异常:
	 * 
	 *             <pre>
	 * 					1、如果需要处理，并设置了期望异常，但不匹配，抛出AssertionError。
	 * 					2、如果需要处理，并设置了期望异常，且匹配，不抛出异常。
	 * 					3、如果不处理或需要处理但未设置期望异常，则直接抛出目标异常。
	 *             </pre>
	 */
	private void optionallyHandleException(Throwable e, boolean handleException) throws Throwable {

		if (handleException) {
			handleException(e);
		} else {
			throw e;
		}
	}

	/**
	 * 处理目标异常。
	 * 
	 * @param e
	 *            待处理的目标异常。
	 * 
	 * @throws Throwable 抛出异常，可能为目标异常、AssertionError或无。
	 */
	private void handleException(Throwable e) throws Throwable {

		if (fMatcherBuilder.expectsThrowable()) {
			// 设置了期望异常，则比较是否匹配，不匹配则抛出AssertionError。
			assertThat(e, fMatcherBuilder.build());
		} else {
			throw e;// 未设置期望异常，则直接抛出。
		}
	}
}
