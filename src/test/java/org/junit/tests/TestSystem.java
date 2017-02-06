package org.junit.tests;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.internal.JUnitSystem;

/**
 * ����ϵͳ��
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class TestSystem implements JUnitSystem {
	/**
	 * ��ӡ����
	 */
	private PrintStream out;

	/**
	 * �ֽ������������
	 */
	private ByteArrayOutputStream fOutContents;

	/**
	 * ����һ������ϵͳ��
	 */
	public TestSystem() {

		fOutContents= new ByteArrayOutputStream();
		out= new PrintStream(fOutContents);
	}

	public PrintStream out() {

		return out;
	}

	/**
	 * ��ȡ�ֽ������������
	 * 
	 * @return �ֽ������������
	 */
	public OutputStream outContents() {

		return fOutContents;
	}
}
