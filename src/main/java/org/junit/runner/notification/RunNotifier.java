package org.junit.runner.notification;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Result;

/**
 * ���м����������ߡ�
 * 
 * @since 4.0
 * @author ע��By JavaSking 2017��2��5��
 */
public class RunNotifier {
	
	/**
	 * ��ǰע������м������б�
	 */
	private final List<RunListener> fListeners= Collections.synchronizedList(new ArrayList<RunListener>());

	/**
	 * �Ƿ��˹���ֹ�ı�־��
	 */
	private volatile boolean fPleaseStop= false;

	/**
	 * ע��Ŀ�����м�������
	 * 
	 * @param listener
	 *            ��ע���Ŀ�����м�������
	 */
	public void addListener(RunListener listener) {

		fListeners.add(listener);
	}

	/**
	 * �Ƴ�Ŀ�����м�������
	 * 
	 * @param listener
	 *            ���Ƴ���Ŀ�����м�������
	 */
	public void removeListener(RunListener listener) {

		fListeners.remove(listener);
	}

	/**
	 * �̰߳�ȫ�����м����������ߡ�
	 * 
	 * @author ע��By JavaSking 2017��2��5��
	 */
	private abstract class SafeNotifier {

		/**
		 * ��������м������б�
		 */
		private final List<RunListener> fCurrentListeners;

		/**
		 * ����һ���̰߳�ȫ�����м����������ߡ�<br>
		 * Ĭ�Ϲ���ǰע����������м�������
		 */
		SafeNotifier() {

			this(fListeners);
		}

		/**
		 * ����һ������Ŀ�����м����������м����������ߡ�
		 * 
		 * @param currentListeners
		 *            Ŀ�����м������б�
		 */
		SafeNotifier(List<RunListener> currentListeners) {

			fCurrentListeners= currentListeners;
		}

		/**
		 * ���й���
		 */
		void run() {

			synchronized (fListeners) {

				List<RunListener> safeListeners= new ArrayList<RunListener>();
				List<Failure> failures= new ArrayList<Failure>();
				for (Iterator<RunListener> all= fCurrentListeners.iterator(); all.hasNext();) {
					try {
						RunListener listener= all.next();
						notifyListener(listener);
						safeListeners.add(listener);
					} catch (Exception e) {
						failures.add(new Failure(Description.TEST_MECHANISM, e));
					}
				}
				fireTestFailures(safeListeners, failures);
			}
		}

		/**
		 * ֪ͨ���м�������
		 * 
		 * @param each
		 *            ��֪ͨ�����м�������
		 * @throws Exception
		 *             �쳣��
		 */
		abstract protected void notifyListener(RunListener each) throws Exception;
	}

	/**
	 * ֪ͨ��ǰע����������м��������Կ�ʼ��
	 * 
	 * @param description
	 *            ��������������Ϣ��
	 */
	public void fireTestRunStarted(final Description description) {

		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {

				each.testRunStarted(description);
			};
		}.run();
	}

	/**
	 * ֪ͨ��ǰע����������м��������в��Խ�����
	 * 
	 * @param result
	 *            ���Խ����
	 */
	public void fireTestRunFinished(final Result result) {

		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {

				each.testRunFinished(result);
			};
		}.run();
	}

	/**
	 * ֪ͨ��ǰע����������м�����Ŀ��ԭ�Ӳ��Կ�ʼ��
	 * 
	 * @param description
	 *            Ŀ���������������Ϣ��
	 * @throws StoppedByUserException
	 *             ��Ϊ��ֹ�쳣��
	 */
	public void fireTestStarted(final Description description) throws StoppedByUserException {

		if (fPleaseStop) {
			throw new StoppedByUserException();
		}
		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {
				
				each.testStarted(description);
			};
		}.run();
	}

	/**
	 * ֪ͨ��ǰע����������м�����Ŀ��ԭ�Ӳ��Է�������
	 * 
	 * @param failure
	 *            ��������Ĳ��԰�װ����
	 */
	public void fireTestFailure(Failure failure) {

		fireTestFailures(fListeners, asList(failure));
	}

	/**
	 * ֪ͨĿ�����м�����Ŀ��ԭ�Ӳ��Է�������
	 * 
	 * @param listeners
	 *            ��֪ͨ�����м������б�
	 * @param failures
	 *            ��������Ĳ��԰�װ�����б�
	 */
	private void fireTestFailures(List<RunListener> listeners, final List<Failure> failures) {

		if (!failures.isEmpty()) {
			new SafeNotifier(listeners) {
				@Override
				protected void notifyListener(RunListener listener) throws Exception {
					
					for (Failure each : failures) {
						listener.testFailure(each);
					}
				};
			}.run();
		}
	}

	/**
	 * ֪ͨ��ǰע����������м�����Ŀ��ԭ�ӷ�����β�����Ԥ�ڵĴ���
	 * 
	 * @param failure
	 *            ��������Ĳ��԰�װ����
	 */
	public void fireTestAssumptionFailed(final Failure failure) {

		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {

				each.testAssumptionFailure(failure);
			};
		}.run();
	}

	/**
	 * ֪ͨ��ǰע����������м�����Ŀ��ԭ�Ӳ��Խ�������
	 * 
	 * @param description
	 *            Ŀ��ԭ�Ӳ�������������Ϣ��
	 */
	public void fireTestIgnored(final Description description) {

		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {

				each.testIgnored(description);
			}
		}.run();
	}

	/**
	 * ֪ͨ��ǰע����������м�����Ŀ��ԭ�Ӳ��Խ�����
	 * 
	 * @param description
	 *            Ŀ��ԭ�Ӳ�������������Ϣ��
	 */
	public void fireTestFinished(final Description description) {

		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {

				each.testFinished(description);
			};
		}.run();
	}

	/**
	 * ��Ϊ��ֹ���ԡ�
	 */
	public void pleaseStop() {

		fPleaseStop= true;
	}

	/**
	 * ע�����м�������ǰ�á�
	 * 
	 * @param listener
	 *            ��ע������м�������
	 */
	public void addFirstListener(RunListener listener) {

		fListeners.add(0, listener);
	}
}