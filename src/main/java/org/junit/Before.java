package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Beforeע�⣬��ע<code>public void</code>������<br>
 * 1��Beforeע���ע�ķ�����ÿ��<code>&#064;Test</code>����ǰ��������һ�Ρ�<br>
 * 2�������в������ص�&#064;Before�����ڵ�ǰ��&#064;Before����ǰ���С�<br>
 * ���ӣ�
 * 
 * <pre>
 * public class Example {
 *    List empty;
 *    &#064;Before 
 *    public void initialize() {
 *       empty= new ArrayList();
 *    }
 *    &#064;Test 
 *    public void size() {
 *       ...
 *    }
 *    &#064;Test 
 *    public void remove() {
 *       ...
 *    }
 * }
 * </pre>
 * 
 * @see org.junit.BeforeClass
 * @see org.junit.After
 * @since 4.0
 * @author ע��By JavaSking 2017��2��4��
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Before {
	
}
