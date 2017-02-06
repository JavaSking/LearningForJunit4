package org.junit.internal.runners;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

/**
 * ���󱨸���������
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class ErrorReportingRunner extends Runner {
	/**
	 * �쳣�б�
	 */
	private final List<Throwable> fCauses;

	/**
	 * �����ࡣ
	 */
	private final Class<?> fTestClass;

	/**
	 * ����һ���µĴ��󱨸���������
	 * 
	 * @param testClass
	 *            �����ࡣ
	 * @param cause
	 *            �쳣�б�
	 */
	public ErrorReportingRunner(Class<?> testClass, Throwable cause) {

		fTestClass= testClass;
		fCauses= getCauses(cause);
	}

	@Override
	public Description getDescription() {

		Description description= Description.createSuiteDescription(fTestClass);
		for (Throwable each : fCauses) {
			description.addChild(describeCause(each));
		}
		return description;
	}

	@Override
	public void run(RunNotifier notifier) {

		for (Throwable each : fCauses) {
			runCause(each, notifier);
		}
	}

	/**
	 * ��ȡ���Ŀ���쳣��ԭ���б�
	 * 
	 * @param cause
	 *            Ŀ���쳣��
	 * @return ���Ŀ���쳣��ԭ���б�
	 */
	@SuppressWarnings("deprecation")
	private List<Throwable> getCauses(Throwable cause) {
		
		if (cause instanceof InvocationTargetException) {
			return getCauses(cause.getCause());
		}
		if (cause instanceof InitializationError) {
			return ((InitializationError) cause).getCauses();
		}
		if (cause instanceof org.junit.internal.runners.InitializationError) {
			return ((org.junit.internal.runners.InitializationError) cause).getCauses();
		}
		return Arrays.asList(cause);
	}

	/**
	 * ���������쳣�Ĳ�������������Ϣ��<br>
	 * �ýڵ㽫�����Ϊ�������쳣�Ĳ�����Ĳ�������������Ϣ���ӽڵ㡣
	 * 
	 * @param child
	 *            �쳣��
	 * @return �����쳣�Ĳ�������������Ϣ��
	 */
	private Description describeCause(Throwable child) {

		return Description.createTestDescription(fTestClass, "initializationError");
	}

	/**
	 * ֪ͨĿ�����м�����������֪ͨĿ������෢������
	 * 
	 * @param child
	 *            �����Ĵ���
	 * @param notifier
	 *            ���м����������ߡ�
	 */
	private void runCause(Throwable child, RunNotifier notifier) {

		Description description= describeCause(child);
		notifier.fireTestStarted(description);
		notifier.fireTestFailure(new Failure(description, child));
		notifier.fireTestFinished(description);
	}
}