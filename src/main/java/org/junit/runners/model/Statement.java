package org.junit.runners.model;

/**
 * 代表Junit测试运行过程中的动作。<br>
 * 测试用例的运行时描述，关注于测试用例如何运行和调用测试代码。<br>
 * 设计模式：职责链模式。
 * 
 * @since 4.5
 * @author 注释By JavaSking 2017年2月5日
 */
public abstract class Statement {

	/**
	 * 运行动作。
	 * 
	 * @throws Throwable
	 *             异常。
	 */
	public abstract void evaluate() throws Throwable;
}