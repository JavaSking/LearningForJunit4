package org.junit.runner;

import org.junit.runner.notification.RunNotifier;

/**
 * ������������<br>
 * �������������в��Բ�����֪ͨ�������м����������߽��в����¼�֪ͨ��
 * 
 * @see org.junit.runner.Description
 * @see org.junit.runner.RunWith
 * @since 4.0
 * @author ע��By JavaSking 2017��2��5��
 */
public abstract class Runner implements Describable {

	public abstract Description getDescription();

	/**
	 * ���в��ԡ�
	 * 
	 * @param notifier
	 *            �������м����������ߡ�
	 */
	public abstract void run(RunNotifier notifier);

	/**
	 * ͳ��ԭ�Ӳ�������
	 * 
	 * @return ԭ�Ӳ�������
	 */
	public int testCount() {

		return getDescription().testCount();
	}
}