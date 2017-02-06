package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * After注解。标注<code>public void</code>方法。<br>
 * 1、After注解标注的方法在每个<code>&#064;Test</code>方法后都被运行一次，且一定被运行。<br>
 * 2、超类中不被重载的<code>&#064;After</code>方法在当前类的<code>&#064;After</code>方法后被运行。<br>
 * 例子：
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
 * @author 注释By JavaSking 2017年2月4日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface After {
	
}
