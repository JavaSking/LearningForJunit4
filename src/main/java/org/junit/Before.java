package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Before注解，标注<code>public void</code>方法。<br>
 * 1）Before注解标注的方法在每个<code>&#064;Test</code>方法前都被运行一次。<br>
 * 2）超类中不被重载的&#064;Before方法在当前类&#064;Before方法前运行。<br>
 * 例子：
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
 * @author 注释By JavaSking 2017年2月4日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Before {
	
}
