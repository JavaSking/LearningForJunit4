package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.runners.MethodSorters;

/**
 * FixMethodOrderע�⣬��ע�����࣬��ʾ���Է�����ִ��˳��
 * 
 * @see org.junit.runners.MethodSorters
 * @since 4.11
 * @author ע��By JavaSking 2017��2��5��
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface FixMethodOrder {

	/**
	 * ʹ�õķ�����������
	 * 
	 * @return ������������
	 */
	MethodSorters value() default MethodSorters.DEFAULT;
}
