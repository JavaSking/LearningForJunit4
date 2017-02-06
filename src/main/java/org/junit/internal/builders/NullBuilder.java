package org.junit.internal.builders;

import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/**
 * �������κ���������������������Ϊ��ͳһ��װ��
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
public class NullBuilder extends RunnerBuilder {

	/**
	 * �������κ���������
	 * 
	 * @param each
	 *          �����ࡣ
	 * @return null��
	 */
	@Override
	public Runner runnerForClass(Class<?> each) throws Throwable {

		return null;
	}
}