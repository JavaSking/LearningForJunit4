package org.junit.rules;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.matchers.JUnitMatchers.isThrowable;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

/**
 * 期望异常匹配器工厂。
 * 
 * @author 注释By JavaSking 2017年2月6日
 */
class ExpectedExceptionMatcherBuilder {

	/**
	 * 注册的匹配器列表。
	 */
	private final List<Matcher<?>> fMatchers= new ArrayList<Matcher<?>>();

	/**
	 * 新增匹配器。
	 * 
	 * @param matcher
	 *            待新增的匹配器。
	 */
	void add(Matcher<?> matcher) {

		fMatchers.add(matcher);
	}

	/**
	 * 判断是否存在期望异常（即当前匹配器列表不为空）。
	 * 
	 * @return 如果存在期望异常则返回true，否则返回false。
	 */
	boolean expectsThrowable() {

		return !fMatchers.isEmpty();
	}

	/**
	 * 创建附加失配跟踪堆栈的功能的异常匹配器。
	 * 
	 * @return 附加失配跟踪堆栈的功能的异常匹配器。
	 */
	Matcher<Throwable> build() {

		return isThrowable(allOfTheMatchers());
	}

	/**
	 * 获取当前注册的所有匹配器的功能代替匹配器。<br>
	 * 该功能代替匹配器只匹配那些匹配所有匹配器的对象。
	 * 
	 * @return 当前注册的所有匹配器的功能代替匹配器。
	 */
	private Matcher<Throwable> allOfTheMatchers() {

		if (fMatchers.size() == 1) {
			return cast(fMatchers.get(0));
		}
		return allOf(castedMatchers());
	}

	/**
	 * 将当前注册的匹配器强制转换为异常匹配器并返回。
	 * 
	 * @return 异常匹配器列表。
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Matcher<? super Throwable>> castedMatchers() {

		return new ArrayList<Matcher<? super Throwable>>((List) fMatchers);
	}

	/**
	 * 将目标匹配器强制转换为异常匹配器并返回。
	 * 
	 * @param singleMatcher
	 *            目标匹配器。
	 * @return 异常匹配器。
	 */
	@SuppressWarnings("unchecked")
	private Matcher<Throwable> cast(Matcher<?> singleMatcher) {

		return (Matcher<Throwable>) singleMatcher;
	}
}
