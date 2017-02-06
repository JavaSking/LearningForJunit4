package org.junit.rules;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

/**
 * �ù�������Ӳ��Է����������ڵĸ����׶Ρ�<br>
 * TestWatcher�������಻��ı���Ե��κ���Ϊ���ṩ��succeeded��failded��skipped��<br>
 * starting��finished�������һ�����Է����������ڵĸ����׶Ρ�
 * 
 * ���ӣ�
 * 
 * <pre>
 * public static class WatchmanTest {
 * 
 * 	private static String watchedLog;
 *
 * 	&#064;Rule
 * 	public TestWatcher watchman= new TestWatcher() {
 * 
 * 		&#064;Override
 * 		protected void failed(Throwable e, Description description) {
 * 
 * 			watchedLog+= description + &quot;\n&quot;;
 * 		}
 *
 * 		&#064;Override
 * 		protected void succeeded(Description description) {
 * 
 * 			watchedLog+= description + &quot; &quot; + &quot;success!\n&quot;;
 * 		}
 * 	};
 *
 * 	&#064;Test
 * 	public void fails() {
 * 
 * 		fail();
 * 	}
 *
 * 	&#064;Test
 * 	public void succeeds() {
 * 
 * 	}
 * }
 * </pre>
 *
 * @since 4.9
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
public abstract class TestWatcher implements TestRule {

	public Statement apply(final Statement base, final Description description) {
		
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {

				List<Throwable> errors= new ArrayList<Throwable>();

				startingQuietly(description, errors);
				try {
					base.evaluate();
					succeededQuietly(description, errors);
				} catch (AssumptionViolatedException e) {
					errors.add(e);
					skippedQuietly(e, description, errors);
				} catch (Throwable t) {
					errors.add(t);
					failedQuietly(t, description, errors);
				} finally {
					finishedQuietly(description, errors);
				}
				MultipleFailureException.assertEmpty(errors);
			}
		};
	}

	/**
	 * ִ��ԭ�Ӳ��Գɹ��¼������߼���
	 * 
	 * @param description
	 *            ��������������Ϣ��
	 * @param errors
	 *            �쳣�б�
	 */
	private void succeededQuietly(Description description, List<Throwable> errors) {

		try {
			succeeded(description);
		} catch (Throwable t) {
			errors.add(t);
		}
	}

	/**
	 * ִ��ԭ�Ӳ���ʧ���¼������߼���
	 * 
	 * @param t
	 *            �������쳣��
	 * @param description
	 *            ��������������Ϣ��
	 * @param errors
	 *            �쳣�б�
	 */
	private void failedQuietly(Throwable t, Description description, List<Throwable> errors) {

		try {
			failed(t, description);
		} catch (Throwable t1) {
			errors.add(t1);
		}
	}

	/**
	 * ִ��ԭ�Ӳ�����μ���ʧ���¼������߼���
	 * 
	 * @param e
	 *            ��μ���ʧ���쳣��
	 * @param description
	 *            ��������������Ϣ��
	 * @param errors
	 *            �쳣�б�
	 */
	private void skippedQuietly(AssumptionViolatedException e, Description description, List<Throwable> errors) {
		
		try {
			skipped(e, description);
		} catch (Throwable t) {
			errors.add(t);
		}
	}

	/**
	 * ִ��ԭ�Ӳ��Կ�ʼ�¼������߼���
	 * 
	 * @param description
	 *            ��������������Ϣ��
	 * @param errors
	 *            �쳣�б�
	 */
	private void startingQuietly(Description description, List<Throwable> errors) {

		try {
			starting(description);
		} catch (Throwable t) {
			errors.add(t);
		}
	}

	/**
	 * ִ��ԭ�Ӳ�������¼������߼���
	 * 
	 * @param description
	 *            ��������������Ϣ��
	 * @param errors
	 *            �쳣�б�
	 */
	private void finishedQuietly(Description description, List<Throwable> errors) {
		
		try {
			finished(description);
		} catch (Throwable t) {
			errors.add(t);
		}
	}

	/**
	 * ����ԭ�Ӳ��Գɹ��¼���
	 * 
	 * @param description
	 *            ��������������Ϣ��
	 */
	protected void succeeded(Description description) {

	}

	/**
	 * ����ԭ�Ӳ���ʧ���¼���
	 * 
	 * @param e
	 *            �������쳣��
	 * @param description
	 *            ��������������Ϣ��
	 */
	protected void failed(Throwable e, Description description) {

	}

	/**
	 * ����ԭ�Ӳ�����μ���ʧ���¼���
	 * 
	 * @param e
	 *            ��μ���ʧ���쳣��
	 * @param description
	 *            ��������������Ϣ��
	 */
	protected void skipped(AssumptionViolatedException e, Description description) {

	}

	/**
	 * ����ԭ�Ӳ��Կ�ʼ�¼���
	 * 
	 * @param description
	 *            ��������������Ϣ��
	 */
	protected void starting(Description description) {

	}

	/**
	 * ����ԭ�Ӳ�������¼�������ʧ�ܻ��ǳɹ�����
	 * 
	 * @param description
	 *            ��������������Ϣ��
	 */
	protected void finished(Description description) {

	}
}
