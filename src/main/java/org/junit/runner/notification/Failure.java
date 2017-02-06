package org.junit.runner.notification;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import org.junit.runner.Description;

/**
 * 发生错误的测试的包装对象。<br>
 * 包装了测试内容描述信息以及发生的错误。
 * 
 * @since 4.0
 * @author 注释By JavaSking 2017年2月5日
 */
public class Failure implements Serializable {
	/**
	 * 序列号。
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * 测试内容描述信息。
	 */
	private final Description fDescription;

	/**
	 * 发生的错误。
	 */
	private final Throwable fThrownException;

	/**
	 * 构造一个发生错误的测试的包装对象。
	 * 
	 * @param description
	 *            目标测试内容描述信息。
	 * @param thrownException
	 *            发生的错误。
	 */
	public Failure(Description description, Throwable thrownException) {

		fThrownException= thrownException;
		fDescription= description;
	}

	/**
	 * 获取测试标题。
	 * 
	 * @return 测试标题。
	 */
	public String getTestHeader() {

		return fDescription.getDisplayName();
	}

	/**
	 * 获取测试内容描述信息。
	 * 
	 * @return 测试内容描述信息。
	 */
	public Description getDescription() {

		return fDescription;
	}

	/**
	 * 获取发生的错误。
	 * 
	 * @return 发生的错误。
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
	 * 获取错误堆栈信息。
	 * 
	 * @return 错误堆栈信息。
	 */
	public String getTrace() {

		StringWriter stringWriter= new StringWriter();
		PrintWriter writer= new PrintWriter(stringWriter);
		getException().printStackTrace(writer);
		StringBuffer buffer= stringWriter.getBuffer();
		return buffer.toString();
	}

	/**
	 * 获取错误信息。
	 * 
	 * @return 错误信息。
	 */
	public String getMessage() {

		return getException().getMessage();
	}
}
