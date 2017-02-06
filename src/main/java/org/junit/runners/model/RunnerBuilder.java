package org.junit.runners.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Runner;

/**
 * ����������������<br>
 * ���ģʽ��ְ����ģʽ��
 * 
 * @see org.junit.runners.Suite
 * @since 4.5
 * @author ע��By JavaSking 2017��2��6��
 */
public abstract class RunnerBuilder {

	/**
	 * �����༯�ϡ�
	 */
	private final Set<Class<?>> parents = new HashSet<Class<?>>();

	/**
	 * ��ȡĿ����������������������������ʧ�����׳��쳣��
	 * 
	 * @param testClass
	 *          �������ࡣ
	 * @return Ŀ��������������������Ϊnull��
	 * @throws Throwable
	 *           �����������쳣��
	 */
	public abstract Runner runnerForClass(Class<?> testClass) throws Throwable;

	/**
	 * ��ȡĿ����������������������������ʧ���򴴽�һ�����󱨸���������������������
	 * 
	 * @param testClass
	 *          �������ࡣ
	 * @return ��������
	 */
	public Runner safeRunnerForClass(Class<?> testClass) {

		try {
			return runnerForClass(testClass);
		} catch (Throwable e) {
			return new ErrorReportingRunner(testClass, e);
		}
	}

	/**
	 * ��Ӵ������ಢ���ء�
	 * 
	 * @param parent
	 *          �������ࡣ
	 * @return �������ࡣ
	 * @throws InitializationError
	 *           ��������ʼ���쳣��
	 */
	Class<?> addParent(Class<?> parent) throws InitializationError {

		if (!parents.add(parent)) {
			throw new InitializationError(String.format("class '%s' (possibly indirectly) contains itself as a SuiteClass", parent.getName()));
		}
		return parent;
	}

	/**
	 * �Ƴ�Ŀ��������ࡣ
	 * 
	 * @param klass
	 *          ���Ƴ��Ĳ����ࡣ
	 */
	void removeParent(Class<?> klass) {

		parents.remove(klass);
	}

	public List<Runner> runners(Class<?> parent, Class<?>[] children) throws InitializationError {

		addParent(parent);
		try {
			return runners(children);
		} finally {
			removeParent(parent);
		}
	}

	public List<Runner> runners(Class<?> parent, List<Class<?>> children) throws InitializationError {

		return runners(parent, children.toArray(new Class<?>[0]));
	}

	/**
	 * ��ȡ���������������
	 * 
	 * @param children
	 *          ���������顣
	 * @return ���������顣
	 */
	private List<Runner> runners(Class<?>[] children) {

		ArrayList<Runner> runners = new ArrayList<Runner>();
		for (Class<?> each : children) {
			Runner childRunner = safeRunnerForClass(each);
			if (childRunner != null) {
				runners.add(childRunner);
			}
		}
		return runners;
	}
}
