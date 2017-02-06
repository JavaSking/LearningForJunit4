package org.junit.internal.runners.statements;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.Statement;

/**
 * 期望抛出期望异常的动作。下一条Statement是测试方法的调用{@code InvokeMethod}<br>
 * 如果该期望未实现，则表示测试失败。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class ExpectException extends Statement {
	
	/**
	 * 下一条动作。
	 */
	private Statement fNext;

	/**
	 * 期望抛出的异常类型。
	 */
	private final Class<? extends Throwable> fExpected;

	/**
	 * 构造一个期望抛出期望异常的动作。
	 * 
	 * @param next
	 *            下一条动作。
	 * @param expected
	 *            期望抛出的异常类型。
	 */
	public ExpectException(Statement next, Class<? extends Throwable> expected) {

		fNext= next;
		fExpected= expected;
	}

	/**
	 * 期望抛出期望异常，如果该期望未实现，则抛出异常表明测试失败。
	 */
	@Override
	public void evaluate() throws Exception {

		boolean complete= false;
		try {
			fNext.evaluate();
			complete= true;
		} catch (AssumptionViolatedException e) {
			throw e;//这里表明期望异常不能设置为AssumptionViolatedException类型。
		} catch (Throwable e) {
			/* 1、如果不是期望的异常，则抛出异常 */
			if (!fExpected.isAssignableFrom(e.getClass())) {
				String message= "Unexpected exception, expected<" + fExpected.getName() + "> but was<" + e.getClass().getName() + ">";
				throw new Exception(message, e);
			}
			/* 2、如果是期望的异常则不做任何操作 */
		}
		if (complete) {
			throw new AssertionError("Expected exception: " + fExpected.getName());
		}
	}
}