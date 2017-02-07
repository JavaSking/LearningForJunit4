package org.junit.experimental.categories;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Category注解，用于对测试用例分组。<br>
 * 测试方法或测试类可以同属多个分组。类上的Category注解不会把方法上的Category注解覆盖。<br>
 * 
 * 例子：
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
 * public void testGetAge()｛
 * 	int age = person.getAge();
 * 	assertEquals(3, age);
 * ｝
 * 
 * &#064;Category(AttributeFun.class)
 * &#064;Test
 * public void testGetName()｛
 * 	String name = person.getName();
 * 	assertEquals("Willard", name);
 * ｝
 * 
 * &#064;Category(BehaviorFun.class)
 * &#064;Test
 * public void testTalk()｛
 * 	String message = person.talkTo("Jimy");
 * 	assertNotNull(message);
 * ｝
 * 
 * &#064;Category(BehaviorFun.class)
 * &#064;Test(timeout=200)
 * public void testWalk()｛
 * 	person.walk();
 * ｝
 * 
 * 上述将测试用例分为两组：属性方法（GetAge，GetName）和行为方法（Talk，Walk）的测试用例。
 * </pre>
 * 
 * @author 注释By JavaSking 2017年2月6日
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Category {

	/**
	 * 测试用例所属组。
	 * 
	 * @return 测试用例所属组。
	 */
	Class<?>[] value();
}