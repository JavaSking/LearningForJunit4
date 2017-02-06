package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BeforeClassע�⣬��ע<code>public static void</code>�޲η�����<br>
 * 1��������<code>Test</code>����֮ǰ���У���ֻ����һ�Ρ�<br>
 * 2�������з����ص�BeforeClass�������ڵ�ǰ���BeforeClass�������С�<br>
 * ���ӣ�
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
 * @author ע��By JavaSking 2017��2��4��
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeClass {
	
}
