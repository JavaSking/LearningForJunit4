package org.junit.runners.model;

/**
 * ����������������ʵ���У���
 * 
 * @since 4.7
 * @author ע��By JavaSking 2017��2��5��
 */
public interface RunnerScheduler {

	/**
	 * ����Ŀ���̡߳�
	 * 
	 * @param childStatement
	 *            Ŀ���̡߳�
	 */
	void schedule(Runnable childStatement);

	/**
	 * ���Ƚ�����
	 */
	void finished();
}
