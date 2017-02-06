package org.junit.internal.runners.statements;

import org.junit.runners.model.Statement;

/**
 * 代表由目标异常引起的失败动作。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class Fail extends Statement {
	
	/**
	 * 发生的异常。
	 */
	private final Throwable fError;

	/**
	 * 构造一个由目标异常引起的失败动作。
	 * 
	 * @param e
	 *            目标异常。
	 */
	public Fail(Throwable e) {

		fError= e;
	}

	/**
	 * 抛出目标异常。
	 */
	@Override
	public void evaluate() throws Throwable {

		throw fError;
	}
}
