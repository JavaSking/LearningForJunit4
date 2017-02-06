package org.junit.internal.runners.statements;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * ��BeforeClass/Beforeע���ע�ķ�������Ķ�����<br>
 * <ul>
 * <li>BeforeClass��ע�ķ���Ϊ��̬�ģ�ֻ����һ�Σ����<code>fTarget</code>Ϊnull��
 * <li>Before��ע�ķ����Ǿ�̬�����ö�Σ����<code>fTarget</code>Ϊ������ʵ����
 * </ul>
 * <ul>
 * <li>�����BeforeClassע���ע�ķ���������һ�����������еĲ��Է������ж���ɵĶ�����
 * <li>�����Beforeע���ע�ķ���������һ�������ǵ�һ���Է�������Ķ�����
 * </ul>
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class RunBefores extends Statement {
	/**
	 * ��һ��������
	 */
	private final Statement fNext;

	/**
	 * ����BeforeClass/Beforeע���ע�ķ����Ķ���
	 */
	private final Object fTarget;

	/**
	 * BeforeClass/Beforeע���ע�ķ����б������ൽ�����˳�����У���
	 */
	private final List<FrameworkMethod> fBefores;

	/**
	 * ����һ����BeforeClass/Beforeע���ע�ķ�������Ķ�����
	 * 
	 * @param next
	 *            ��һ��������
	 * @param befores
	 *            BeforeClass/Beforeע���ע�ķ����б�
	 * @param target
	 *            ����BeforeClass/Beforeע���ע�ķ����Ķ���
	 */
	public RunBefores(Statement next, List<FrameworkMethod> befores, Object target) {

		fNext= next;
		fBefores= befores;
		fTarget= target;
	}

	/**
	 * �����в��Է���(Ŀ����Է���������ǰ����BeforeClass(/Before)ע���ע�ķ�����
	 */
	@Override
	public void evaluate() throws Throwable {

		for (FrameworkMethod before : fBefores) {//�׳��쳣���ж�ִ���ˣ���ͬ��After/AfterClass��
			before.invokeExplosively(fTarget);
		}
		fNext.evaluate();
	}
}