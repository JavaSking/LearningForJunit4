package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AfterClass注解，标注<code>public static void</code>方法。<br>
 * 1）AfterClass方法在所有Test方法后运行，且一定运行。<br>
 * 2）超类的非重载AfterClass方法在当前类的AfterClass方法后运行。<br>
 * 例子：
 * 
 * <pre>
 * public class Example {
 *    private static DatabaseConnection database;
 *    &#064;BeforeClass 
 *    public static void login() {
 *          database= ...;
 *    }
 *    &#064;Test 
 *    public void something() {
 *          ...
 *    }
 *    &#064;Test 
 *    public void somethingElse() {
 *          ...
 *    }
 *    &#064;AfterClass 
 *    public static void logout() {
 *          database.logout();
 *    }
 * }
 * </pre>
 *
 * @see org.junit.BeforeClass
 * @see org.junit.Test
 * @since 4.0
 * @author 注释By JavaSking 2017年2月4日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterClass {
	
}
