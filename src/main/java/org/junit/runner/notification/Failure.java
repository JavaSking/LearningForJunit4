package org.junit.runner.notification;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import org.junit.runner.Description;

/**
 * ��������Ĳ��Եİ�װ����<br>
 * ��װ�˲�������������Ϣ�Լ������Ĵ���
 * 
 * @since 4.0
 * @author ע��By JavaSking 2017��2��5��
 */
public class Failure implements Serializable {
	/**
	 * ���кš�
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * ��������������Ϣ��
	 */
	private final Description fDescription;

	/**
	 * �����Ĵ���
	 */
	private final Throwable fThrownException;

	/**
	 * ����һ����������Ĳ��Եİ�װ����
	 * 
	 * @param description
	 *            Ŀ���������������Ϣ��
	 * @param thrownException
	 *            �����Ĵ���
	 */
	public Failure(Description description, Throwable thrownException) {

		fThrownException= thrownException;
		fDescription= description;
	}

	/**
	 * ��ȡ���Ա��⡣
	 * 
	 * @return ���Ա��⡣
	 */
	public String getTestHeader() {

		return fDescription.getDisplayName();
	}

	/**
	 * ��ȡ��������������Ϣ��
	 * 
	 * @return ��������������Ϣ��
	 */
	public Description getDescription() {

		return fDescription;
	}

	/**
	 * ��ȡ�����Ĵ���
	 * 
	 * @return �����Ĵ���
	 */
	public Throwable getException() {

		return fThrownException;
	}

	@Override
	public String toString() {

		StringBuffer buffer= new StringBuffer();
		buffer.append(getTestHeader() + ": " + fThrownException.getMessage());
		return buffer.toString();
	}

	/**
	 * ��ȡ�����ջ��Ϣ��
	 * 
	 * @return �����ջ��Ϣ��
	 */
	public String getTrace() {

		StringWriter stringWriter= new StringWriter();
		PrintWriter writer= new PrintWriter(stringWriter);
		getException().printStackTrace(writer);
		StringBuffer buffer= stringWriter.getBuffer();
		return buffer.toString();
	}

	/**
	 * ��ȡ������Ϣ��
	 * 
	 * @return ������Ϣ��
	 */
	public String getMessage() {

		return getException().getMessage();
	}
}
