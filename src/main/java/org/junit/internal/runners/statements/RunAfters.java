package org.junit.internal.runners.statements;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

/**
 * 由AfterClass/After注解标注的方法代表的动作。<br>
 * <ul>
 * <li>AfterClass标注的方法为静态的，只调用一次，因此<code>fTarget</code>为null。
 * <li>After标注的方法非静态，调用多次，因此<code>fTarget</code>为测试类实例。
 * </ul>
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class RunAfters extends Statement {

	/**
	 * 下一条动作。
	 */
	private final Statement fNext;

	/**
	 * 调用AfterClass/After注解标注的方法的对象。
	 */
	private final Object fTarget;

	/**
	 * AfterClass/After注解标注的方法列表（按子类向超类回溯的顺序排列）。
	 */
	private final List<FrameworkMethod> fAfters;

	/**
	 * 构造一个由AfterClass/After注解标注的方法代表的动作。
	 * 
	 * @param next
	 *            下一条动作。
	 * @param afters
	 *            AfterClass/After注解标注的方法列表。
	 * @param target
	 *            调用AfterClass/After注解标注的方法的对象。
	 */
	public RunAfters(Statement next, List<FrameworkMethod> afters, Object target) {

		fNext= next;
		fAfters= afters;
		fTarget= target;
	}

	/**
	 * 在所有测试方法(目标测试方法）运行后运行AfterClass(/After)注解标注的方法。
	 */
	@Override
	public void evaluate() throws Throwable {

		List<Throwable> errors= new ArrayList<Throwable>();
		try {
			fNext.evaluate();
		} catch (Throwable e) {
			errors.add(e);
		} finally {
			/* 这里验证了AfterClass/After注解标注的方法必被执行 */
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