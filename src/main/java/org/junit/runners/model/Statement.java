package org.junit.runners.model;

/**
 * ����Junit�������й����еĶ�����<br>
 * ��������������ʱ��������ע�ڲ�������������к͵��ò��Դ��롣<br>
 * ���ģʽ��ְ����ģʽ��
 * 
 * @since 4.5
 * @author ע��By JavaSking 2017��2��5��
 */
public abstract class Statement {

	/**
	 * ���ж�����
	 * 
	 * @throws Throwable
	 *             �쳣��
	 */
	public abstract void evaluate() throws Throwable;
}