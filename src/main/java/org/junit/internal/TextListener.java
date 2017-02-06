package org.junit.internal;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * �����¼������Ϣ�����м�������
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class TextListener extends RunListener {

	/**
	 * ��ӡ����
	 */
	private final PrintStream fWriter;

	/**
	 * ����һ�������¼������Ϣ�����м�������
	 * 
	 * @param system
	 *            ϵͳ��
	 */
	public TextListener(JUnitSystem system) {

		this(system.out());
	}

	/**
	 * ����һ�������¼������Ϣ�����м�������
	 * 
	 * @param writer
	 *            ��ӡ����
	 */
	public TextListener(PrintStream writer) {

		this.fWriter= writer;
	}

	@Override
	public void testRunFinished(Result result) {

		printHeader(result.getRunTime());
		printFailures(result);
		printFooter(result);
	}

	@Override
	public void testStarted(Description description) {

		fWriter.append('.');
	}

	@Override
	public void testFailure(Failure failure) {

		fWriter.append('E');
	}

	@Override
	public void testIgnored(Description description) {

		fWriter.append('I');
	}

	/**
	 * ��ȡ��ӡ����
	 * 
	 * @return ��ӡ����
	 */
	private PrintStream getWriter() {

		return fWriter;
	}

	/**
	 * ��ӡͷ����Ϣ������ʱ����
	 * 
	 * @param runTime
	 *            ����ʱ����
	 */
	protected void printHeader(long runTime) {

		getWriter().println();
		getWriter().println("Time: " + elapsedTimeAsString(runTime));
	}

	/**
	 * ��ӡ��������Ĳ��ԵĲ�����Ϣ��
	 * 
	 * @param result
	 *            ���Խ����
	 */
	protected void printFailures(Result result) {

		List<Failure> failures= result.getFailures();
		if (failures.size() == 0) {
			return;
		}
		if (failures.size() == 1) {
			getWriter().println("There was " + failures.size() + " failure:");
		} else {
			getWriter().println("There were " + failures.size() + " failures:");
		}
		int i= 1;
		for (Failure each : failures) {
			printFailure(each, "" + i++);
		}
	}

	/**
	 * ��ӡ������������Ĳ��ԵĲ�����Ϣ��
	 * 
	 * @param each
	 *            ��������Ĳ��Եİ�װ����
	 * @param prefix
	 *            ǰ׺��
	 */
	protected void printFailure(Failure each, String prefix) {

		getWriter().println(prefix + ") " + each.getTestHeader());
		getWriter().print(each.getTrace());
	}

	/**
	 * ��ӡβ����Ϣ���ɹ�/ʧ��ͳ����Ϣ��
	 * 
	 * @param result
	 *            ���Խ����
	 */
	protected void printFooter(Result result) {

		if (result.wasSuccessful()) {
			getWriter().println();
			getWriter().print("OK");
			getWriter().println(" (" + result.getRunCount() + " test" + (result.getRunCount() == 1 ? "" : "s") + ")");

		} else {
			getWriter().println();
			getWriter().println("FAILURES!!!");
			getWriter().println("Tests run: " + result.getRunCount() + ",  Failures: " + result.getFailureCount());
		}
		getWriter().println();
	}

	/**
	 * ��ʽ������ʱ����
	 * 
	 * @param runTime
	 *            ����ʱ�������룩��
	 * @return ��ʽ���������ʱ����
	 */
	protected String elapsedTimeAsString(long runTime) {

		return NumberFormat.getInstance().format((double) runTime / 1000);
	}
}
