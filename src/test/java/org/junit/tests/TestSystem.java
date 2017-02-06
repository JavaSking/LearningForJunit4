package org.junit.tests;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.internal.JUnitSystem;

/**
 * 测试系统。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class TestSystem implements JUnitSystem {
	/**
	 * 打印流。
	 */
	private PrintStream out;

	/**
	 * 字节数组输出流。
	 */
	private ByteArrayOutputStream fOutContents;

	/**
	 * 构造一个测试系统。
	 */
	public TestSystem() {

		fOutContents= new ByteArrayOutputStream();
		out= new PrintStream(fOutContents);
	}

	public PrintStream out() {

		return out;
	}

	/**
	 * 获取字节数组输出流。
	 * 
	 * @return 字节数组输出流。
	 */
	public OutputStream outContents() {

		return fOutContents;
	}
}
