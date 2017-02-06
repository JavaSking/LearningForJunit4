package org.junit.runner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * ���Խ����ͳ�����в��ԵĲ�����Ϣ��������������Ĳ��ԡ�
 * 
 * @since 4.0
 * @author ע��By JavaSking 2017��2��5��
 */
public class Result implements Serializable {
	/**
	 * ���кš�
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * ���Լ�������
	 */
	private AtomicInteger fCount= new AtomicInteger();

	/**
	 * �����Ĳ��Լ�������
	 */
	private AtomicInteger fIgnoreCount= new AtomicInteger();

	/**
	 * ��������Ĳ��Եİ�װ�����б�
	 */
	private final List<Failure> fFailures= Collections.synchronizedList(new ArrayList<Failure>());

	/**
	 * ��������ʱ�������룩��
	 */
	private long fRunTime= 0;

	/**
	 * ���Կ�ʼʱ�䡣
	 */
	private long fStartTime;

	/**
	 * ͳ�Ʋ�������
	 * 
	 * @return ��������
	 */
	public int getRunCount() {

		return fCount.get();
	}

	/**
	 * ͳ�Ʒ�������Ĳ�������
	 * 
	 * @return ��������Ĳ�������
	 */
	public int getFailureCount() {

		return fFailures.size();
	}

	/**
	 * ͳ�Ʋ���ʱ�������룩��
	 * 
	 * @return ����ʱ����
	 */
	public long getRunTime() {

		return fRunTime;
	}

	/**
	 * ��ȡ��������Ĳ��Եİ�װ�����б�
	 * 
	 * @return ��������Ĳ��Եİ�װ�����б�
	 */
	public List<Failure> getFailures() {

		return fFailures;
	}

	/**
	 * ͳ�Ʊ������Ĳ�������
	 * 
	 * @return �������Ĳ�������
	 */
	public int getIgnoreCount() {

		return fIgnoreCount.get();
	}

	/**
	 * �жϲ����Ƿ�ɹ���
	 * 
	 * @return ������Գɹ��򷵻�true�����򷵻�false��
	 */
	public boolean wasSuccessful() {

		return getFailureCount() == 0;
	}

	/**
	 * �������м�������
	 * 
	 * @author ע��By JavaSking 2017��2��5��
	 */
	private class Listener extends RunListener {

		@Override
		public void testRunStarted(Description description) throws Exception {

			fStartTime= System.currentTimeMillis();// ��¼���Կ�ʼʱ�䡣
		}

		@Override
		public void testRunFinished(Result result) throws Exception {

			long endTime= System.currentTimeMillis();
			fRunTime+= endTime - fStartTime;// �������ʱ����
		}

		@Override
		public void testFinished(Description description) throws Exception {

			fCount.getAndIncrement();
		}

		@Override
		public void testFailure(Failure failure) throws Exception {

			fFailures.add(failure);
		}

		@Override
		public void testIgnored(Description description) throws Exception {

			fIgnoreCount.getAndIncrement();
		}

		@Override
		public void testAssumptionFailure(Failure failure) {

		}
	}

	/**
	 * �½�һ���������м����������ء�
	 * 
	 * @return �½��Ĳ�����������
	 */
	public RunListener createListener() {

		return new Listener();
	}
}
