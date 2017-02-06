package org.junit.rules;

import org.junit.Rule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * �ṩ�˲�������ִ�й����еĹ���ʹ��{@code TestRule}�������ṩ�༶��Ĺ���<br>
 * ͨ����дԴ������Ӧ�ù����ȡ���������
 * 
 * @since 4.7
 * @author ע��By JavaSking 2017��2��6��
 */
public interface MethodRule {

	/**
	 * ��дԴ���������ؽ��������
	 * 
	 * @param base
	 *            Դ������
	 * @param method
	 *            ���Է�����
	 * @param target
	 *            ���ò��Է����Ķ���
	 * @return ���������
	 */
	Statement apply(Statement base, FrameworkMethod method, Object target);
}