package org.junit.internal.runners.statements;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

/**
 * ��AfterClass/Afterע���ע�ķ�������Ķ�����<br>
 * <ul>
 * <li>AfterClass��ע�ķ���Ϊ��̬�ģ�ֻ����һ�Σ����<code>fTarget</code>Ϊnull��
 * <li>After��ע�ķ����Ǿ�̬�����ö�Σ����<code>fTarget</code>Ϊ������ʵ����
 * </ul>
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class RunAfters extends Statement {

	/**
	 * ��һ��������
	 */
	private final Statement fNext;

	/**
	 * ����AfterClass/Afterע���ע�ķ����Ķ���
	 */
	private final Object fTarget;

	/**
	 * AfterClass/Afterע���ע�ķ����б�������������ݵ�˳�����У���
	 */
	private final List<FrameworkMethod> fAfters;

	/**
	 * ����һ����AfterClass/Afterע���ע�ķ�������Ķ�����
	 * 
	 * @param next
	 *            ��һ��������
	 * @param afters
	 *            AfterClass/Afterע���ע�ķ����б�
	 * @param target
	 *            ����AfterClass/Afterע���ע�ķ����Ķ���
	 */
	public RunAfters(Statement next, List<FrameworkMethod> afters, Object target) {

		fNext= next;
		fAfters= afters;
		fTarget= target;
	}

	/**
	 * �����в��Է���(Ŀ����Է��������к�����AfterClass(/After)ע���ע�ķ�����
	 */
	@Override
	public void evaluate() throws Throwable {

		List<Throwable> errors= new ArrayList<Throwable>();
		try {
			fNext.evaluate();
		} catch (Throwable e) {
			errors.add(e);
		} finally {
			/* ������֤��AfterClass/Afterע���ע�ķ����ر�ִ�� */
			for (FrameworkMethod each : fAfters) {
				try {
					each.invokeExplosively(fTarget);
				} catch (Throwable e) {
					errors.add(e);
				}
			}
		}
		MultipleFailureException.assertEmpty(errors);
	}
}