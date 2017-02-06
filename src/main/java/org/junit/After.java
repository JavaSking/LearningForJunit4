package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Afterע�⡣��ע<code>public void</code>������<br>
 * 1��Afterע���ע�ķ�����ÿ��<code>&#064;Test</code>�����󶼱�����һ�Σ���һ�������С�<br>
 * 2�������в������ص�<code>&#064;After</code>�����ڵ�ǰ���<code>&#064;After</code>���������С�<br>
 * ���ӣ�
 * 
 * <pre>
* public class Example {
*    File output;
*    &#064;Before 
*    public void createOutputFile() {
*          output= new File(...);
*    }
*    &#064;Test 
*    public void something() {
*          ...
*    }
*    &#064;After 
*    public void deleteOutputFile() {
*          output.delete();
*    }
* }
 * </pre>
 * 
 * @see org.junit.Before
 * @see org.junit.Test
 * @since 4.0
 * @author ע��By JavaSking 2017��2��4��
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface After {
	
}
