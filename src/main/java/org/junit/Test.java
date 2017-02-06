package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test注解，标注 <b><code>public void</code></b> 方法为测试方法。<br>
 * 注意：<b>运行测试方法前都会构造一个新实例对象</b>。<br>
 * 可选属性:
 * <ul>
 * <li><code>expected</code>表示期望抛出的异常类型。
 * <li><code>timeout</code>表示测试限时时长，超出则表示测试失败。
 * </ul>
 * 注意：<b>虽然<code>timeout</code>属性对于获取和终止死循环有效，但这是不确定的。<br>
 * 如下面的测试是否成功取决于操作系统调度线程。
 * 
 * <pre>
 *    &#064;Test(<b>timeout=100</b>) 
 *    public void sleep100() {
 *       Thread.sleep(100);
 *    }
 * </pre>
 * 
 * <b>线程安全提醒:</b> 
 * <pre>
 * 1、使用属性timeout的Test方法是单线程的,详见｛@link FailOnTimeout｝evaluateStatement()方法。
 * 2、使用{@link org.junit.rules.Timeout}规则将保证运行@Before和@After方法在同一个线程中。
 * </pre>
 * 
 * @since 4.0
 * @author 注释By JavaSking 2017年2月4日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Test {

	/**
	 * 空异常。
	 * 
	 * @author 注释By JavaSking 2017年2月4日
	 */
	static class None extends Throwable {
		/**
		 * 序列号。
		 */
		private static final long serialVersionUID= 1L;

		/**
		 * 构造一个空异常。
		 */
		private None() {

		}
	}

	/**
	 * 期望抛出的异常类型，默认为空异常。
	 * 
	 * @return 期望抛出的异常类型。
	 */
	Class<? extends Throwable> expected() default None.class;

	/**
	 * 限时时间（毫秒）。
	 * 
	 * @return 限时时间（毫秒）。
	 */
	long timeout() default 0L;
}
