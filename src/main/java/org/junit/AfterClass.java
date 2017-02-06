package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AfterClassע�⣬��ע<code>public static void</code>������<br>
 * 1��AfterClass����������Test���������У���һ�����С�<br>
 * 2������ķ�����AfterClass�����ڵ�ǰ���AfterClass���������С�<br>
 * ���ӣ�
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
 * @author ע��By JavaSking 2017��2��4��
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterClass {
	
}
