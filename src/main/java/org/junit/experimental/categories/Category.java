package org.junit.experimental.categories;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Categoryע�⣬���ڶԲ����������顣<br>
 * ���Է�������������ͬ��������顣���ϵ�Categoryע�ⲻ��ѷ����ϵ�Categoryע�⸲�ǡ�<br>
 * 
 * ���ӣ�
 * 
 * <pre>
 * public interface AttributeFun{ 
 * 
 * }
 * public interface BehaviorFun{ 
 * 
 * }
 * &#064;Category(AttributeFun.class)
 * &#064;Test
 * public void testGetAge()��
 * 	int age = person.getAge();
 * 	assertEquals(3, age);
 * ��
 * 
 * &#064;Category(AttributeFun.class)
 * &#064;Test
 * public void testGetName()��
 * 	String name = person.getName();
 * 	assertEquals("Willard", name);
 * ��
 * 
 * &#064;Category(BehaviorFun.class)
 * &#064;Test
 * public void testTalk()��
 * 	String message = person.talkTo("Jimy");
 * 	assertNotNull(message);
 * ��
 * 
 * &#064;Category(BehaviorFun.class)
 * &#064;Test(timeout=200)
 * public void testWalk()��
 * 	person.walk();
 * ��
 * 
 * ����������������Ϊ���飺���Է�����GetAge��GetName������Ϊ������Talk��Walk���Ĳ���������
 * </pre>
 * 
 * @author ע��By JavaSking 2017��2��6��
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Category {

	/**
	 * �������������顣
	 * 
	 * @return �������������顣
	 */
	Class<?>[] value();
}