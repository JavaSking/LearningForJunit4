package org.junit.internal.runners.statements;

import org.junit.runners.model.Statement;

/**
 * ����ʱ���Զ����������ʱ��ʧ�ܲ��׳��쳣��
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class FailOnTimeout extends Statement {

	/**
	 * ԭʼ������
	 */
	private final Statement fOriginalStatement;

	/**
	 * ��ʱʱ����
	 */
	private final long fTimeout;

	/**
	 * ����һ����ʱ���Զ�����
	 * 
	 * @param originalStatement
	 *            ԭʼ������
	 * @param timeout
	 *            ��ʱʱ����
	 */
	public FailOnTimeout(Statement originalStatement, long timeout) {

		fOriginalStatement= originalStatement;
		fTimeout= timeout;
	}

	/**
	 * ִ��ԭʼ����������ʱ���׳��쳣��
	 */
	@Override
	public void evaluate() throws Throwable {

		StatementThread thread= evaluateStatement();
		if (!thread.fFinished) {
			throwExceptionForUnfinishedThread(thread);
		}
	}

	/**
	 * ִ��ԭʼ������
	 * 
	 * @return ִ��ԭʼ�����Ķ����̡߳�
	 * @throws InterruptedException
	 *             �߳��ж��쳣��
	 */
	private StatementThread evaluateStatement() throws InterruptedException {

		StatementThread thread= new StatementThread(fOriginalStatement);
		thread.start();
		thread.join(fTimeout);
		if (!thread.fFinished) {
			thread.recordStackTrace();
		}
		thread.interrupt();// ��ɻ�δ��ɶ��ж��߳�
		return thread;
	}

	/**
	 * �׳�����δ����쳣��
	 * 
	 * @param thread
	 *            ����ִ��ԭʼ�����Ķ����̡߳�
	 * @throws Throwable
	 *             ����δ����쳣��
	 */
	private void throwExceptionForUnfinishedThread(StatementThread thread) throws Throwable {

		if (thread.fExceptionThrownByOriginalStatement != null) {
			throw thread.fExceptionThrownByOriginalStatement;
		} else {
			throwTimeoutException(thread);
		}
	}

	/**
	 * �׳���ʱ�쳣��
	 * 
	 * @param thread
	 *            ����ִ��ԭʼ�����Ķ����̡߳�
	 * @throws Exception
	 *             ��ʱ�쳣��
	 */
	private void throwTimeoutException(StatementThread thread) throws Exception {

		Exception exception= new Exception(String.format("test timed out after %d milliseconds", fTimeout));
		exception.setStackTrace(thread.getRecordedStackTrace());
		throw exception;
	}

	/**
	 * �����̣߳�����ִ��ԭʼ��������ִ��ԭʼ���������쳣���¼���쳣����Ƕ���δ��ɡ�
	 * 
	 * @author ע��By JavaSking 2017��2��5��
	 */
	private static class StatementThread extends Thread {
		/**
		 * ԭʼ������
		 */
		private final Statement fStatement;

		/**
		 * ԭʼ������ɱ�־��
		 */
		private boolean fFinished= false;

		/**
		 * ִ��ԭʼ�����׳����쳣��
		 */
		private Throwable fExceptionThrownByOriginalStatement= null;

		/**
		 * ԭʼ������ջ֡���顣
		 */
		private StackTraceElement[] fRecordedStackTrace= null;

		/**
		 * ����һ������ִ��Ŀ��ԭʼ�����Ķ����̡߳�
		 * 
		 * @param statement
		 *            ԭʼ������
		 */
		public StatementThread(Statement statement) {

			fStatement= statement;
		}

		/**
		 * ��¼ԭʼ�����Ķ�ջ֡��
		 */
		public void recordStackTrace() {

			fRecordedStackTrace= getStackTrace();
		}

		/**
		 * ��ȡԭʼ������ջ֡���顣
		 * 
		 * @return ԭʼ������ջ֡���顣
		 */
		public StackTraceElement[] getRecordedStackTrace() {

			return fRecordedStackTrace;
		}

		/**
		 * ����ԭʼ�������������쳣���¼���쳣��
		 */
		@Override
		public void run() {
			try {
				fStatement.evaluate();
				fFinished= true;
			} catch (InterruptedException e) {
				// ����InterruptedException
			} catch (Throwable e) {
				fExceptionThrownByOriginalStatement= e;
			}
		}
	}
}