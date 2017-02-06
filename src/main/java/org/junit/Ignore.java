package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ignore注解，标注测试类或方法将被跳过。<br>
 * 可选属性：<code>value</code>表示跳过测试的原因。<br>
 * 例子：
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
 * @author 注释By JavaSking 2017年2月4日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Ignore {
	
	/**
	 * 跳过测试的原因。
	 * 
	 * @return 跳过测试的原因。
	 */
	String value() default "";
}
