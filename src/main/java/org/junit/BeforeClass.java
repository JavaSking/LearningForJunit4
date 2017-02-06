package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BeforeClass注解，标注<code>public static void</code>无参方法。<br>
 * 1）在所有<code>Test</code>方法之前运行，且只运行一次。<br>
 * 2）超类中非重载的BeforeClass方法早于当前类的BeforeClass方法运行。<br>
 * 例子：
 * 
 * <pre>
 * public class Example {
 *    &#064;BeforeClass 
 *    public static void onlyOnce() {
 *       ...
 *    }
 *    &#064;Test 
 *    public void one() {
 *       ...
 *    }
 *    &#064;Test 
 *    public void two() {
 *       ...
 *    }
 * }
 * </pre>
 * 
 * @see org.junit.AfterClass
 * @since 4.0
 * @author 注释By JavaSking 2017年2月4日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeClass {
	
}
