package org.junit.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * �쳣ԭ��ƥ������
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
public class ThrowableCauseMatcher<T extends Throwable>
		extends TypeSafeMatcher<T> {

	/**
	 * Ǳ�ڵ�ԭ��ƥ������
	 */
	private final Matcher<T> fMatcher;

	/**
	 * ����һ��ʹ��Ŀ��ԭ��ƥ�������쳣ԭ��ƥ������
	 * 
	 * @param matcher
	 *            ԭ��ƥ������
	 */
	public ThrowableCauseMatcher(Matcher<T> matcher) {

		fMatcher= matcher;
	}

	/**
	 * ��������ǰ�쳣ԭ��ƥ������
	 * 
	 * @param description
	 *            ��ǰ�쳣ƥ������������
	 */
	public void describeTo(Description description) {

		description.appendText("exception with cause ");
		description.appendDescriptionOf(fMatcher);
	}

	/**
	 * ���Ŀ���쳣�Ƿ�ƥ�䵱ǰ�쳣ԭ��ƥ������
	 * 
	 * @param item
	 *            ������Ŀ���쳣����Ϊnull��
	 * 
	 * @return ���Ŀ���쳣ƥ�䵱ǰ�쳣ԭ��ƥ�����򷵻�true�����򷵻�false��
	 */
	@Override
	protected boolean matchesSafely(T item) {

		return fMatcher.matches(item.getCause());
	}

	/**
	 * ������Ŀ���쳣��ƥ�䵱ǰ�쳣ԭ��ƥ������
	 * 
	 * @param item
	 *            Ŀ���쳣��
	 * @param description
	 *            ������Ϣ��
	 */
	@Override
	protected void describeMismatchSafely(T item, Description description) {

		description.appendText("cause ");
		fMatcher.describeMismatch(item.getCause(), description);
	}

	/**
	 * ����һ��ʹ��Ŀ��ԭ��ƥ�������쳣ԭ��ƥ������
	 * 
	 * @param matcher
	 *            ԭ��ƥ������
	 * @return ʹ��Ŀ��ԭ��ƥ�������쳣ԭ��ƥ������
	 */
	@Factory
	public static <T extends Throwable> Matcher<T> hasCause(final Matcher<T> matcher) {

		return new ThrowableCauseMatcher<T>(matcher);
	}
}