package org.junit.internal.builders;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.RunnerBuilder;

/**
 * ���𴴽�BlockJUnit4ClassRunner�������Ĺ�����<br>
 * JunitĬ�ϲ���BlockJUnit4ClassRunner��������
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
public class JUnit4Builder extends RunnerBuilder {

	/**
	 * ��ȡBlockJUnit4ClassRunner��������
	 * 
	 * @param testClass
	 *          �����ࡣ
	 * @return BlockJUnit4ClassRunner��������
	 */
	@Override
	public Runner runnerForClass(Class<?> testClass) throws Throwable {

		return new BlockJUnit4ClassRunner(testClass);
	}
}