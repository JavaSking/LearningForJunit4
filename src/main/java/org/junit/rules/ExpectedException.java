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
 * �쳣���Թ���@Test(expected=xxx)�÷����ʾ����������������<br>
 * �����@Test(expected=xxx)���쳣���Թ����ṩ�˸�Ϊ���ƥ��Ĺ���<br>
 * ���Ը���message��cause���쳣����ƥ�䡣
 * 
 * ���ӣ�
 * 
 * <pre>
 * // ���²���ȫ��ͨ����
 * public static class HasExpectedException {
 * 
 * 	&#064;Rule
 * 	public ExpectedException thrown= ExpectedException.none();
 *
 * 	&#064;Test
 * 	public void throwsNothing() {
 * 		// δ���������쳣��δ�׳��쳣��ͨ��
 * 	}
 *
 * 	&#064;Test
 * 	public void throwsNullPointerException() {
 * 
 * 		thrown.expect(NullPointerException.class);// �����׳�NullPointerException�쳣
 * 		throw new NullPointerException();
 * 	}
 *
 * 	&#064;Test
 * 	public void throwsNullPointerExceptionWithMessage() {
 * 
 * 		thrown.expect(NullPointerException.class);// �����׳�NullPointerException�쳣
 * 		thrown.expectMessage(&quot;happened?&quot;);
 * 		thrown.expectMessage(startsWith(&quot;What&quot;));// �����������쳣��Ϣ
 * 		throw new NullPointerException(&quot;What happened?&quot;);
 * 	}
 *
 * 	&#064;Test
 * 	public void throwsIllegalArgumentExceptionWithMessageAndCause() {
 * 
 * 		NullPointerException expectedCause= new NullPointerException();
 * 		thrown.expect(IllegalArgumentException.class);
 * 		thrown.expectMessage(&quot;What&quot;);
 * 		thrown.expectCause(is(expectedCause));// �������������쳣��ԭ��
 * 		throw new IllegalArgumentException(&quot;What happened?&quot;, cause);
 * 	}
 * }
 * </pre>
 *
 * @since 4.7
 * @author ע��By JavaSking 2017��2��6��
 */
public class ExpectedException implements TestRule {

	/**
	 * ��ȡδ�����쳣����
	 * 
	 * @return δ�����쳣����
	 */
	public static ExpectedException none() {

		return new ExpectedException();
	}

	/**
	 * �����쳣ƥ������������
	 */
	private final ExpectedExceptionMatcherBuilder fMatcherBuilder= new ExpectedExceptionMatcherBuilder();

	/**
	 * �Ƿ���{@code AssumptionViolatedException}��Ĭ�ϲ�����
	 */
	private boolean handleAssumptionViolatedExceptions= false;

	/**
	 * �Ƿ���{@code AssertionError}��Ĭ�ϲ�����
	 */
	private boolean handleAssertionErrors= false;

	/**
	 * ����һ��δ�����쳣����
	 */
	private ExpectedException() {

	}

	/**
	 * ���ô���{@code AssertionError}�����ص�ǰ�쳣���Թ���
	 * 
	 * @return ��ǰ�쳣���Թ���
	 */
	public ExpectedException handleAssertionErrors() {

		handleAssertionErrors= true;
		return this;
	}

	/**
	 * ���ô���{@code AssumptionViolatedException}�����ص�ǰ�쳣���Թ���
	 * 
	 * @return ��ǰ�쳣���Թ���
	 */
	public ExpectedException handleAssumptionViolatedExceptions() {

		handleAssumptionViolatedExceptions= true;
		return this;
	}

	public Statement apply(Statement base, org.junit.runner.Description description) {

		return new ExpectedExceptionStatement(base);
	}

	/**
	 * ����Ŀ��ƥ������
	 * 
	 * @param matcher
	 *            ��������ƥ������
	 */
	public void expect(Matcher<?> matcher) {

		fMatcherBuilder.add(matcher);
	}

	/**
	 * �����쳣����ƥ������
	 * 
	 * @param type
	 *            �쳣���͡�
	 */
	public void expect(Class<? extends Throwable> type) {

		expect(instanceOf(type));
	}

	/**
	 * �����쳣��Ϣƥ������ֻҪ����Ŀ����Ϣ�Ӵ���Ϊƥ�䡣<br>
	 * 
	 * 
	 * @param substring
	 *            ƥ����Ϣ��
	 */
	public void expectMessage(String substring) {

		expectMessage(containsString(substring));
	}

	/**
	 * ����Ŀ���쳣��Ϣƥ������
	 * 
	 * @param matcher
	 *            Ŀ���쳣��Ϣƥ������
	 */
	public void expectMessage(Matcher<String> matcher) {

		expect(hasMessage(matcher));
	}

	/**
	 * ����Ŀ���쳣ԭ��ƥ������
	 * 
	 * @param expectedCause
	 *            Ŀ���쳣ԭ��ƥ������
	 */
	public void expectCause(Matcher<? extends Throwable> expectedCause) {

		expect(hasCause(expectedCause));
	}

	/**
	 * �����׳������쳣�Ķ��������δ�׳������쳣���������ʧ�ܡ�<br>
	 * 
	 * @author ע��By JavaSking 2017��2��5��
	 */
	private class ExpectedExceptionStatement extends Statement {

		/**
		 * ��һ��������
		 */
		private final Statement fNext;

		/**
		 * ����һ�������׳������쳣�Ķ�����
		 * 
		 * @param base
		 *            ��һ��������
		 */
		public ExpectedExceptionStatement(Statement base) {

			fNext= base;
		}

		@Override
		public void evaluate() throws Throwable {
			try {
				fNext.evaluate();
				/* ���������쳣����δ�׳��쳣 */
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
	 * δ�׳������쳣����������ʧ�ܲ��׳����Դ���
	 * 
	 * @throws AssertionError
	 *             ���Դ���
	 */
	private void failDueToMissingException() throws AssertionError {

		String expectation= StringDescription.toString(fMatcherBuilder.build());
		fail("Expected test to throw " + expectation);
	}

	/**
	 * ����򲻴���Ŀ���쳣���Ƿ���ȡ����<code>handleException</code>��־��
	 * 
	 * @param e
	 *            ��ѡ������쳣��
	 * @param handleException
	 *            �Ƿ���д���
	 * @throws Throwable
	 *             ������׳����쳣:
	 * 
	 *             <pre>
	 * 					1�������Ҫ�����������������쳣������ƥ�䣬�׳�AssertionError��
	 * 					2�������Ҫ�����������������쳣����ƥ�䣬���׳��쳣��
	 * 					3��������������Ҫ����δ���������쳣����ֱ���׳�Ŀ���쳣��
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
	 * ����Ŀ���쳣��
	 * 
	 * @param e
	 *            �������Ŀ���쳣��
	 * 
	 * @throws Throwable �׳��쳣������ΪĿ���쳣��AssertionError���ޡ�
	 */
	private void handleException(Throwable e) throws Throwable {

		if (fMatcherBuilder.expectsThrowable()) {
			// �����������쳣����Ƚ��Ƿ�ƥ�䣬��ƥ�����׳�AssertionError��
			assertThat(e, fMatcherBuilder.build());
		} else {
			throw e;// δ���������쳣����ֱ���׳���
		}
	}
}
