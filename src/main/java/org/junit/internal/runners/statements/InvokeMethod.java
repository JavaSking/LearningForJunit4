package org.junit.internal.runners.statements;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * 方法调用动作。<br>
 * 注意：这个动作节点没有对其他Statement的引用，因为这是Statement链中的最后一个节点。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class InvokeMethod extends Statement {
	/**
	 * 目标方法。
	 */
	private final FrameworkMethod fTestMethod;

	/**
	 * 调用方法的对象。
	 */
	private Object fTarget;

	/**
	 * 构造一个方法调用动作。
	 * 
	 * @param testMethod
	 *            目标方法。
	 * @param target
	 *            调用方法的对象。
	 */
	public InvokeMethod(FrameworkMethod testMethod, Object target) {

		fTestMethod= testMethod;
		fTarget= target;
	}

	/**
	 * 进行方法调用。
	 */
	@Override
	public void evaluate() throws Throwable {

		fTestMethod.invokeExplosively(fTarget);
	}
}