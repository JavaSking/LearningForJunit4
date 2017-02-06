package org.junit.internal.runners.statements;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * 由BeforeClass/Before注解标注的方法代表的动作。<br>
 * <ul>
 * <li>BeforeClass标注的方法为静态的，只调用一次，因此<code>fTarget</code>为null。
 * <li>Before标注的方法非静态，调用多次，因此<code>fTarget</code>为测试类实例。
 * </ul>
 * <ul>
 * <li>如果是BeforeClass注解标注的方法，则下一条动作是所有的测试方法运行而组成的动作。
 * <li>如果是Before注解标注的方法，则下一条动作是单一测试方法代表的动作。
 * </ul>
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class RunBefores extends Statement {
	/**
	 * 下一条动作。
	 */
	private final Statement fNext;

	/**
	 * 调用BeforeClass/Before注解标注的方法的对象。
	 */
	private final Object fTarget;

	/**
	 * BeforeClass/Before注解标注的方法列表（按超类到子类的顺序排列）。
	 */
	private final List<FrameworkMethod> fBefores;

	/**
	 * 构造一个由BeforeClass/Before注解标注的方法代表的动作。
	 * 
	 * @param next
	 *            下一条动作。
	 * @param befores
	 *            BeforeClass/Before注解标注的方法列表。
	 * @param target
	 *            调用BeforeClass/Before注解标注的方法的对象。
	 */
	public RunBefores(Statement next, List<FrameworkMethod> befores, Object target) {

		fNext= next;
		fBefores= befores;
		fTarget= target;
	}

	/**
	 * 在所有测试方法(目标测试方法）运行前运行BeforeClass(/Before)注解标注的方法。
	 */
	@Override
	public void evaluate() throws Throwable {

		for (FrameworkMethod before : fBefores) {//抛出异常则中断执行了，不同于After/AfterClass。
			before.invokeExplosively(fTarget);
		}
		fNext.evaluate();
	}
}