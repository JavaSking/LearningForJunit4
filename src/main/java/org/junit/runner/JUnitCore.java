package org.junit.runner;

import java.util.ArrayList;
import java.util.List;

import junit.runner.Version;
import org.junit.internal.JUnitSystem;
import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

/**
 * Junit����ڡ�
 * 
 * @see org.junit.runner.Result
 * @see org.junit.runner.notification.RunListener
 * @see org.junit.runner.Request
 * @since 4.0
 * @author ע��By JavaSking 2017��2��5��
 */
public class JUnitCore {
	/**
	 * ���м��������ߡ�
	 */
	private final RunNotifier fNotifier= new RunNotifier();

	/**
	 * ��ڷ���������Ŀ�����ָ���Ĳ����ಢ�˳���
	 * 
	 * @param args
	 *            Ŀ������ࡣ
	 */
	public static void main(String... args) {

		runMainAndExit(new RealSystem(), args);
	}

	/**
	 * ����Ŀ���ಢ�˳���
	 * 
	 * @param system
	 *            ϵͳ��
	 * @param args
	 *            Ŀ������ࡣ
	 */
	private static void runMainAndExit(JUnitSystem system, String... args) {

		Result result= new JUnitCore().runMain(system, args);
		System.exit(result.wasSuccessful() ? 0 : 1);
	}

	/**
	 * ����Ŀ���ಢ���ز��Խ����
	 * 
	 * @param computer
	 *            �����������������в��Եĳ��󣩡�
	 * @param classes
	 *            �������ࡣ
	 * @return ���Խ����
	 */
	public static Result runClasses(Computer computer, Class<?>... classes) {

		return new JUnitCore().run(computer, classes);
	}

	/**
	 * ����Ŀ���ಢ���ز��Խ����
	 * 
	 * @param classes
	 *            �������ࡣ
	 * @return ���Խ����
	 */
	public static Result runClasses(Class<?>... classes) {

		return new JUnitCore().run(defaultComputer(), classes);
	}

	/**
	 * ����Ŀ���࣬�ռ����Խ�������ء�
	 * 
	 * @param system
	 *            ϵͳ��
	 * @param args
	 *            �����ࡣ
	 * @return ���Խ����
	 */
	private Result runMain(JUnitSystem system, String... args) {

		system.out().println("JUnit version " + Version.id());
		List<Class<?>> classes= new ArrayList<Class<?>>();
		List<Failure> missingClasses= new ArrayList<Failure>();
		for (String each : args) {
			try {
				classes.add(Class.forName(each));
			} catch (ClassNotFoundException e) {
				system.out().println("Could not find class: " + each);
				Description description= Description.createSuiteDescription(each);
				Failure failure= new Failure(description, e);
				missingClasses.add(failure);
			}
		}
		RunListener listener= new TextListener(system);
		addListener(listener);
		Result result= run(classes.toArray(new Class[0]));
		for (Failure each : missingClasses) {
			result.getFailures().add(each);
		}
		return result;
	}

	/**
	 * ��ȡJunit�汾��
	 * 
	 * @return Junit�汾��
	 */
	public String getVersion() {

		return Version.id();
	}

	/**
	 * ����Ŀ���࣬�����ز��Խ����
	 * 
	 * @param classes
	 *            �����Ե�Ŀ���ࡣ
	 * @return ���Խ����
	 */
	public Result run(Class<?>... classes) {

		return run(Request.classes(defaultComputer(), classes));
	}

	/**
	 * ����Ŀ���ಢ���ز��Խ����
	 * 
	 * @param computer
	 *            �����������������в��Եĳ��󣩡�
	 * @param classes
	 *            �������ࡣ
	 * @return ���Խ����
	 */
	public Result run(Computer computer, Class<?>... classes) {

		return run(Request.classes(computer, classes));
	}

	/**
	 * ִ��Ŀ��������󣬲����ز��Խ����
	 * 
	 * @param request
	 *            ��������
	 * @return ���Խ����
	 */
	public Result run(Request request) {

		return run(request.getRunner());
	}

	/**
	 * ����JUnit 3.8.x���Ĳ��Բ����ز��Խ���������ݡ�
	 * 
	 * @param test
	 *            �ɷ��Ĳ��Զ���
	 * @return ���Խ����
	 */
	public Result run(junit.framework.Test test) {

		return run(new JUnit38ClassRunner(test));
	}

	/**
	 * ���в��ԡ�
	 * 
	 * @param runner
	 *            ��������
	 * @return ���Խ����
	 */
	public Result run(Runner runner) {

		Result result= new Result();
		RunListener listener= result.createListener();
		fNotifier.addFirstListener(listener);
		try {
			fNotifier.fireTestRunStarted(runner.getDescription());
			runner.run(fNotifier);
			fNotifier.fireTestRunFinished(result);
		} finally {
			removeListener(listener);
		}
		return result;
	}

	/**
	 * ע��Ŀ����Լ�������
	 * 
	 * @param listener
	 *            ��ע���Ŀ����Լ�������
	 */
	public void addListener(RunListener listener) {

		fNotifier.addListener(listener);
	}

	/**
	 * �Ƴ�Ŀ����Լ�������
	 * 
	 * @param listener
	 *            ���Ƴ���Ŀ����Լ�������
	 */
	public void removeListener(RunListener listener) {

		fNotifier.removeListener(listener);
	}

	/**
	 * ����һ�����л����в��Եļ������
	 * 
	 * @return ���л����в��Եļ������
	 */
	static Computer defaultComputer() {

		return new Computer();
	}
}
