package org.junit.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * 异常原因匹配器。
 * 
 * @author 注释By JavaSking 2017年2月6日
 */
public class ThrowableCauseMatcher<T extends Throwable>
		extends TypeSafeMatcher<T> {

	/**
	 * 潜在的原因匹配器。
	 */
	private final Matcher<T> fMatcher;

	/**
	 * 构造一个使用目标原因匹配器的异常原因匹配器。
	 * 
	 * @param matcher
	 *            原因匹配器。
	 */
	public ThrowableCauseMatcher(Matcher<T> matcher) {

		fMatcher= matcher;
	}

	/**
	 * 描述：当前异常原因匹配器。
	 * 
	 * @param description
	 *            当前异常匹配器的描述。
	 */
	public void describeTo(Description description) {

		description.appendText("exception with cause ");
		description.appendDescriptionOf(fMatcher);
	}

	/**
	 * 检查目标异常是否匹配当前异常原因匹配器。
	 * 
	 * @param item
	 *            待检查的目标异常，不为null。
	 * 
	 * @return 如果目标异常匹配当前异常原因匹配器则返回true，否则返回false。
	 */
	@Override
	protected boolean matchesSafely(T item) {

		return fMatcher.matches(item.getCause());
	}

	/**
	 * 描述：目标异常不匹配当前异常原因匹配器。
	 * 
	 * @param item
	 *            目标异常。
	 * @param description
	 *            描述信息。
	 */
	@Override
	protected void describeMismatchSafely(T item, Description description) {

		description.appendText("cause ");
		fMatcher.describeMismatch(item.getCause(), description);
	}

	/**
	 * 创建一个使用目标原因匹配器的异常原因匹配器。
	 * 
	 * @param matcher
	 *            原因匹配器。
	 * @return 使用目标原因匹配器的异常原因匹配器。
	 */
	@Factory
	public static <T extends Throwable> Matcher<T> hasCause(final Matcher<T> matcher) {

		return new ThrowableCauseMatcher<T>(matcher);
	}
}