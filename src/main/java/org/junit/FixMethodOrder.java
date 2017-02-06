package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.runners.MethodSorters;

/**
 * FixMethodOrder注解，标注测试类，表示测试方法的执行顺序。
 * 
 * @see org.junit.runners.MethodSorters
 * @since 4.11
 * @author 注释By JavaSking 2017年2月5日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface FixMethodOrder {

	/**
	 * 使用的方法排序器。
	 * 
	 * @return 方法排序器。
	 */
	MethodSorters value() default MethodSorters.DEFAULT;
}
