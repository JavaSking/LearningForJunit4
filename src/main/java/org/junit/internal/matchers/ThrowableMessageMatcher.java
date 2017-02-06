package org.junit.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * �쳣��Ϣƥ������
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
public class ThrowableMessageMatcher<T extends Throwable> extends TypeSafeMatcher<T> {

	/**
	 * Ǳ�ڵ���Ϣƥ������
	 */
	private final Matcher<String> fMatcher;

	/**
	 * ����һ��ʹ��Ŀ����Ϣƥ�������쳣��Ϣƥ������
	 * 
	 * @param matcher
	 *            ��Ϣƥ������
	 */
	public ThrowableMessageMatcher(Matcher<String> matcher) {

		fMatcher= matcher;
	}

	/**
	 * ��������ǰ�쳣��Ϣƥ������
	 * 
	 * @param description
	 *            ��ǰ�쳣��Ϣƥ������������
	 */
	public void describeTo(Description description) {

		description.appendText("exception with message ");
		description.appendDescriptionOf(fMatcher);
	}

	/**
	 * ���Ŀ���쳣�Ƿ�ƥ�䵱ǰ�쳣��Ϣƥ������
	 * 
	 * @param item
	 *            ������Ŀ���쳣����Ϊnull��
	 * 
	 * @return ���Ŀ���쳣ƥ�䵱ǰ�쳣��Ϣƥ�����򷵻�true�����򷵻�false��
	 */
	@Override
	protected boolean matchesSafely(T item) {

		return fMatcher.matches(item.getMessage());
	}

	/**
	 * ������Ŀ���쳣��ƥ�䵱ǰ�쳣��Ϣƥ������
	 * 
	 * @param item
	 *            Ŀ���쳣��
	 * @param description
	 *            ������Ϣ��
	 */
	@Override
	protected void describeMismatchSafely(T item, Description description) {

		description.appendText("message ");
		fMatcher.describeMismatch(item.getMessage(), description);
	}

	/**
	 * ����һ��ʹ��Ŀ����Ϣƥ�������쳣��Ϣƥ������
	 * 
	 * @param matcher
	 *            ��Ϣƥ������
	 * @return ʹ��Ŀ����Ϣƥ�������쳣��Ϣƥ������
	 */
	@Factory
	public static <T extends Throwable> Matcher<T> hasMessage(final Matcher<String> matcher) {

		return new ThrowableMessageMatcher<T>(matcher);
	}
}