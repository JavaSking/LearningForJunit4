package org.junit.runner.notification;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Result;

/**
 * ���м�������
 * 
 * @see org.junit.runner.JUnitCore
 * @since 4.0
 * @author ע��By JavaSking 2017��2��5��
 */
public class RunListener {

	/**
	 * �������Կ�ʼ�¼��������в��Կ�ʼǰ���á�
	 * 
	 * @param description
	 *            ��������������Ϣ��
	 * @throws Exception
	 *             �쳣��
	 */
	public void testRunStarted(Description description) throws Exception {

	}

	/**
	 * �������в�������¼��������в������ʱ���á�
	 * 
	 * @param result
	 *            ���Խ����
	 * @throws Exception
	 *             �쳣��
	 */
	public void testRunFinished(Result result) throws Exception {

	}

	/**
	 * ����Ŀ��ԭ�Ӳ��Կ�ʼ�¼���
	 * 
	 * @param description
	 *            Ŀ��ԭ�Ӳ�������������Ϣ��
	 * @throws Exception
	 *             �쳣��
	 */
	public void testStarted(Description description) throws Exception {

	}

	/**
	 * ����Ŀ��ԭ�Ӳ�������¼���
	 * 
	 * @param description
	 *            Ŀ��ԭ�Ӳ�������������Ϣ��
	 * @throws Exception
	 *             �쳣��
	 */
	public void testFinished(Description description) throws Exception {

	}

	/**
	 * ����Ŀ��ԭ�Ӳ���ʧ���¼���
	 * 
	 * @param failure
	 *            ��������Ĳ��԰�װ����
	 * @throws Exception
	 *             �쳣��
	 */
	public void testFailure(Failure failure) throws Exception {

	}

	/**
	 * ����Ŀ��ԭ�Ӳ��Է�����β�����Ԥ�ڴ����¼���<br>
	 * ������β�����Ԥ�ڴ����׳�{@code AssumptionViolatedException}�쳣��
	 * 
	 * @param failure
	 *            ��������Ĳ��԰�װ����
	 */
	public void testAssumptionFailure(Failure failure) {

	}

	/**
	 * ����Ŀ��ԭ�Ӳ��Ա������¼���
	 * 
	 * @param description
	 *            Ŀ��ԭ�Ӳ�������������Ϣ��
	 * @throws Exception
	 *             �쳣��
	 */
	public void testIgnored(Description description) throws Exception {

	}
}
