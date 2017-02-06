package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ignoreע�⣬��ע������򷽷�����������<br>
 * ��ѡ���ԣ�<code>value</code>��ʾ�������Ե�ԭ��<br>
 * ���ӣ�
 * 
 * <pre>
 *    &#064;Ignore &#064;Test 
 *    public void something() { ...}
 * </pre>
 * 
 * <pre>
 *    &#064;Ignore 
 *    public class IgnoreMe {
 *        &#064;Test 
 *        public void test1() { ... }
 *        &#064;Test 
 *        public void test2() { ... }
 *    }
 * </pre>
 * 
 * @since 4.0
 * @author ע��By JavaSking 2017��2��4��
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Ignore {
	
	/**
	 * �������Ե�ԭ��
	 * 
	 * @return �������Ե�ԭ��
	 */
	String value() default "";
}
