package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * �ṩ�˲�������ִ�й�����ͨ�ù��ܵĹ���Ĺ���<br>
 * ͨ����дԴ������Ӧ�ù����ȡ���������
 * <ul>
 * <li>{@link ErrorCollector}: �ռ����Է����г��ֵĴ�����Ϣ�����Բ����жϣ�����д��������Խ��������ʧ�ܡ�</li>
 * <li>{@link ExpectedException}: �ṩ�����쳣��֤���ܡ�</li>
 * <li>{@link ExternalResource}: �ṩ�ⲿ��Դ����������</li>
 * <li>{@link TemporaryFolder}: ��JUnit�Ĳ���ִ��ǰ�󣬴�����ɾ���µ���ʱĿ¼��</li>
 * <li>{@link TestName}: �ڲ��Է���ִ�й������ṩ��ȡ�������ֵ�������</li>
 * <li>{@link TestWatcher}: ���Ӳ��Է����������ڵĸ����׶Ρ�</li>
 * <li>{@link Timeout}: ���ڲ��Գ�ʱ��Rule��</li>
 * <li>{@link Verifier}: ��֤����ִ�н������ȷ�ԡ�</li>
 * </ul>
 *
 * @since 4.9
 * @author ע��By JavaSking 2017��2��6��
 */
public interface TestRule {

	/**
	 * ��дԴ������Ӧ�ù��򣬷��ؽ��������
	 * 
	 * @param base
	 *            Դ������
	 * @param description
	 *            ��������������Ϣ��
	 * @return ���������
	 */
	Statement apply(Statement base, Description description);
}
