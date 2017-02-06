package org.junit.rules;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.hamcrest.Matcher;
import org.junit.runners.model.MultipleFailureException;

/**
 * �ù����ռ������г��ֵĴ�����Ϣ�������Բ��жϣ�����д��������Խ�������ǲ���ʧ�ܡ�<br>
 * ���ӣ�
 *
 * <pre>
 * public static class UsesErrorCollectorTwice {
 * 	&#064;Rule
 * 	public ErrorCollector collector= new ErrorCollector();
 *
 * 	&#064;Test
 * 	public void example() {
 * 
 * 		collector.addError(new Throwable(&quot;first thing went wrong&quot;));
 * 		collector.addError(new Throwable(&quot;second thing went wrong&quot;));
 * 		collector.checkThat(getResult(), not(containsString(&quot;ERROR!&quot;)));
 * 		// all lines will run, and then a combined failure logged at the end.
 * 	}
 * }
 * </pre>
 *
 * @since 4.7
 * @author ע��By JavaSking 2017��2��6��
 */
public class ErrorCollector extends Verifier {
	/**
	 * �쳣�б�
	 */
	private List<Throwable> errors= new ArrayList<Throwable>();

	@Override
	protected void verify() throws Throwable {

		MultipleFailureException.assertEmpty(errors);
	}

	/**
	 * ��¼Ŀ���쳣��
	 * 
	 * @param error
	 *            Ŀ���쳣��
	 */
	public void addError(Throwable error) {

		errors.add(error);
	}

	/**
	 * ���Ŀ���Ƿ�ƥ��ָ��ƥ�����������ƥ�����¼һ���쳣��
	 * 
	 * @param value
	 *            Ŀ�ꡣ
	 * @param matcher
	 *            ָ��ƥ������
	 */
	public <T> void checkThat(final T value, final Matcher<T> matcher) {

		checkThat("", value, matcher);
	}

	/**
	 * ���Ŀ���Ƿ�ƥ��ָ��ƥ�����������ƥ�����¼һ��������ƥ��ԭ����쳣��
	 * 
	 * @param reason
	 *            ��ƥ��ԭ��
	 * @param value
	 *            Ŀ�ꡣ
	 * @param matcher
	 *            ָ��ƥ������
	 */
	public <T> void checkThat(final String reason, final T value, final Matcher<T> matcher) {

		checkSucceeds(new Callable<Object>() {
			
			public Object call() throws Exception {
				assertThat(reason, value, matcher);
				return value;
			}
		});
	}

	/**
	 * ִ��������ã���������쳣���¼�����жϳ���ִ�С�
	 * 
	 * @param callable
	 *            ����������
	 * @return �����������򷵻ص��ý���������쳣�򷵻�null��
	 */
	public Object checkSucceeds(Callable<Object> callable) {

		try {
			return callable.call();
		} catch (Throwable e) {
			addError(e);
			return null;
		}
	}
}