package org.junit.internal.runners.statements;

import org.junit.runners.model.Statement;

/**
 * 代表超时测试动作，如果超时则失败并抛出异常。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class FailOnTimeout extends Statement {

	/**
	 * 原始动作。
	 */
	private final Statement fOriginalStatement;

	/**
	 * 限时时长。
	 */
	private final long fTimeout;

	/**
	 * 构造一个超时测试动作。
	 * 
	 * @param originalStatement
	 *            原始动作。
	 * @param timeout
	 *            限时时长。
	 */
	public FailOnTimeout(Statement originalStatement, long timeout) {

		fOriginalStatement= originalStatement;
		fTimeout= timeout;
	}

	/**
	 * 执行原始动作，若超时则抛出异常。
	 */
	@Override
	public void evaluate() throws Throwable {

		StatementThread thread= evaluateStatement();
		if (!thread.fFinished) {
			throwExceptionForUnfinishedThread(thread);
		}
	}

	/**
	 * 执行原始动作。
	 * 
	 * @return 执行原始动作的动作线程。
	 * @throws InterruptedException
	 *             线程中断异常。
	 */
	private StatementThread evaluateStatement() throws InterruptedException {

		StatementThread thread= new StatementThread(fOriginalStatement);
		thread.start();
		thread.join(fTimeout);
		if (!thread.fFinished) {
			thread.recordStackTrace();
		}
		thread.interrupt();// 完成或未完成都中断线程
		return thread;
	}

	/**
	 * 抛出动作未完成异常。
	 * 
	 * @param thread
	 *            负责执行原始动作的动作线程。
	 * @throws Throwable
	 *             动作未完成异常。
	 */
	private void throwExceptionForUnfinishedThread(StatementThread thread) throws Throwable {

		if (thread.fExceptionThrownByOriginalStatement != null) {
			throw thread.fExceptionThrownByOriginalStatement;
		} else {
			throwTimeoutException(thread);
		}
	}

	/**
	 * 抛出超时异常。
	 * 
	 * @param thread
	 *            负责执行原始动作的动作线程。
	 * @throws Exception
	 *             超时异常。
	 */
	private void throwTimeoutException(StatementThread thread) throws Exception {

		Exception exception= new Exception(String.format("test timed out after %d milliseconds", fTimeout));
		exception.setStackTrace(thread.getRecordedStackTrace());
		throw exception;
	}

	/**
	 * 动作线程，负责执行原始动作，若执行原始动作发生异常则记录该异常并标记动作未完成。
	 * 
	 * @author 注释By JavaSking 2017年2月5日
	 */
	private static class StatementThread extends Thread {
		/**
		 * 原始动作。
		 */
		private final Statement fStatement;

		/**
		 * 原始动作完成标志。
		 */
		private boolean fFinished= false;

		/**
		 * 执行原始动作抛出的异常。
		 */
		private Throwable fExceptionThrownByOriginalStatement= null;

		/**
		 * 原始动作堆栈帧数组。
		 */
		private StackTraceElement[] fRecordedStackTrace= null;

		/**
		 * 构造一个负责执行目标原始动作的动作线程。
		 * 
		 * @param statement
		 *            原始动作。
		 */
		public StatementThread(Statement statement) {

			fStatement= statement;
		}

		/**
		 * 记录原始动作的堆栈帧。
		 */
		public void recordStackTrace() {

			fRecordedStackTrace= getStackTrace();
		}

		/**
		 * 获取原始动作堆栈帧数组。
		 * 
		 * @return 原始动作堆栈帧数组。
		 */
		public StackTraceElement[] getRecordedStackTrace() {

			return fRecordedStackTrace;
		}

		/**
		 * 运行原始动作，若发生异常则记录该异常。
		 */
		@Override
		public void run() {
			try {
				fStatement.evaluate();
				fFinished= true;
			} catch (InterruptedException e) {
				// 忽略InterruptedException
			} catch (Throwable e) {
				fExceptionThrownByOriginalStatement= e;
			}
		}
	}
}