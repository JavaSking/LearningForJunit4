package org.junit.runner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RunWith注解标注运行目标测试类选用到的测试运行器。<br>
 * 不添加RunWith注解，则默认使用{@code BlockJUnit4ClassRunner}运行器。<br> 
 * 例子：
 * 
 * <pre>
 * &#064;RunWith(Suite.class)
 * &#064;SuiteClasses({ATest.class, BTest.class, CTest.class})
 * public class ABCSuite {
 * &#64;since 4.0
 * }
 * </pre>
 * 
 * @author JavaSking 2017年2月5日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RunWith {

	/**
	 * 选用的测试运行器。
	 * 
	 * @return 选用的测试运行器。
	 */
	Class<? extends Runner> value();
}
