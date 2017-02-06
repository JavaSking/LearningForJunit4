package org.junit.internal.builders;

import java.util.Arrays;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/**
 * Ĭ����������������������������ְ������ģʽ��������������<br>
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
public class AllDefaultPossibilitiesBuilder extends RunnerBuilder {

	/**
	 * ��ʾ�������о�̬��suite()�����Ƿ���Ϊ���ڻ�ò��������еķ�����
	 */
	private final boolean fCanUseSuiteMethod;

	/**
	 * ����һ��Ĭ������������������
	 * 
	 * @param canUseSuiteMethod
	 *          �������о�̬��suite()�����Ƿ���Ч��
	 */
	public AllDefaultPossibilitiesBuilder(boolean canUseSuiteMethod) {

		fCanUseSuiteMethod = canUseSuiteMethod;
	}

	@Override
	public Runner runnerForClass(Class<?> testClass) throws Throwable {

		List<RunnerBuilder> builders = Arrays.asList(ignoredBuilder(), annotatedBuilder(), suiteMethodBuilder(), junit3Builder(), junit4Builder());

		for (RunnerBuilder each : builders) {
			/* ����������ְ������ģʽ������������ */
			Runner runner = each.safeRunnerForClass(testClass);
			if (runner != null) {
				return runner;
			}
		}
		return null;
	}

	/**
	 * ��ȡ���𴴽�BlockJUnit4ClassRunner��������������������
	 * 
	 * @return ���𴴽�BlockJUnit4ClassRunner��������������������
	 */
	protected JUnit4Builder junit4Builder() {

		return new JUnit4Builder();
	}

	/**
	 * ��ȡ���𴴽�JUnit38ClassRunner��������������������
	 * 
	 * @return ���𴴽�JUnit38ClassRunner��������������������
	 */
	protected JUnit3Builder junit3Builder() {

		return new JUnit3Builder();
	}

	/**
	 * ��ȡ����@RunWithע�������������������
	 * 
	 * @return ����@RunWithע�������������������
	 */
	protected AnnotatedBuilder annotatedBuilder() {

		return new AnnotatedBuilder(this);
	}

	/**
	 * ��ȡ���𴴽�IgnoredClassRunner�������Ĺ�����
	 * 
	 * @return ���𴴽�IgnoredClassRunner�������Ĺ�����
	 */
	protected IgnoredBuilder ignoredBuilder() {

		return new IgnoredBuilder();
	}

	/**
	 * �������fCanUseSuiteMethod��Ч�򷵻ظ��𴴽�SuiteMethod��������������������<br>
	 * ���򷵻ز������κ���������������������
	 * 
	 * @return ���𴴽�SuiteMethod�������������������򲻴����κ���������������������
	 */
	protected RunnerBuilder suiteMethodBuilder() {

		if (fCanUseSuiteMethod) {
			return new SuiteMethodBuilder();
		}
		return new NullBuilder();
	}
}