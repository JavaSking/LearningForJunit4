package org.junit.rules;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.matchers.JUnitMatchers.isThrowable;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

/**
 * �����쳣ƥ����������
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
class ExpectedExceptionMatcherBuilder {

	/**
	 * ע���ƥ�����б�
	 */
	private final List<Matcher<?>> fMatchers= new ArrayList<Matcher<?>>();

	/**
	 * ����ƥ������
	 * 
	 * @param matcher
	 *            ��������ƥ������
	 */
	void add(Matcher<?> matcher) {

		fMatchers.add(matcher);
	}

	/**
	 * �ж��Ƿ���������쳣������ǰƥ�����б�Ϊ�գ���
	 * 
	 * @return ������������쳣�򷵻�true�����򷵻�false��
	 */
	boolean expectsThrowable() {

		return !fMatchers.isEmpty();
	}

	/**
	 * ��������ʧ����ٶ�ջ�Ĺ��ܵ��쳣ƥ������
	 * 
	 * @return ����ʧ����ٶ�ջ�Ĺ��ܵ��쳣ƥ������
	 */
	Matcher<Throwable> build() {

		return isThrowable(allOfTheMatchers());
	}

	/**
	 * ��ȡ��ǰע�������ƥ�����Ĺ��ܴ���ƥ������<br>
	 * �ù��ܴ���ƥ����ֻƥ����Щƥ������ƥ�����Ķ���
	 * 
	 * @return ��ǰע�������ƥ�����Ĺ��ܴ���ƥ������
	 */
	private Matcher<Throwable> allOfTheMatchers() {

		if (fMatchers.size() == 1) {
			return cast(fMatchers.get(0));
		}
		return allOf(castedMatchers());
	}

	/**
	 * ����ǰע���ƥ����ǿ��ת��Ϊ�쳣ƥ���������ء�
	 * 
	 * @return �쳣ƥ�����б�
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Matcher<? super Throwable>> castedMatchers() {

		return new ArrayList<Matcher<? super Throwable>>((List) fMatchers);
	}

	/**
	 * ��Ŀ��ƥ����ǿ��ת��Ϊ�쳣ƥ���������ء�
	 * 
	 * @param singleMatcher
	 *            Ŀ��ƥ������
	 * @return �쳣ƥ������
	 */
	@SuppressWarnings("unchecked")
	private Matcher<Throwable> cast(Matcher<?> singleMatcher) {

		return (Matcher<Throwable>) singleMatcher;
	}
}
