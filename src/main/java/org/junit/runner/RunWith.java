package org.junit.runner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RunWithע���ע����Ŀ�������ѡ�õ��Ĳ�����������<br>
 * �����RunWithע�⣬��Ĭ��ʹ��{@code BlockJUnit4ClassRunner}��������<br> 
 * ���ӣ�
 * 
 * <pre>
 * &#064;RunWith(Suite.class)
 * &#064;SuiteClasses({ATest.class, BTest.class, CTest.class})
 * public class ABCSuite {
 * &#64;since 4.0
 * }
 * </pre>
 * 
 * @author JavaSking 2017��2��5��
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RunWith {

	/**
	 * ѡ�õĲ�����������
	 * 
	 * @return ѡ�õĲ�����������
	 */
	Class<? extends Runner> value();
}
