package org.junit.runners.model;

import static java.lang.reflect.Modifier.isStatic;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.internal.MethodSorter;

/**
 * 测试类的运行时包装。
 * 
 * @since 4.5
 * @author 注释By JavaSking 2017年2月5日
 */
public class TestClass {
	/**
	 * 潜在的类类型。
	 */
	private final Class<?> fClass;

	/**
	 * 测试类的方法与注解的映射表：&lt;注解类型, 被键定义的注解类型的注解标注的方法列表>。
	 */
	private Map<Class<?>, List<FrameworkMethod>> fMethodsForAnnotations= new HashMap<Class<?>, List<FrameworkMethod>>();

	/**
	 * 测试类的成员变量与注解的映射表：&lt;注解类型, 被键定义的注解类型的注解标注的成员变量列表>。
	 */
	private Map<Class<?>, List<FrameworkField>> fFieldsForAnnotations= new HashMap<Class<?>, List<FrameworkField>>();

	/**
	 * 构造目标测试类（测试类只能有一个构造方法）的运行时包装。<br>
	 * 
	 * @param klass
	 *            目标测试类。
	 */
	public TestClass(Class<?> klass) {

		fClass= klass;
		if (klass != null && klass.getConstructors().length > 1) {
			throw new IllegalArgumentException("Test class can only have one constructor");
		}

		for (Class<?> eachClass : getSuperClasses(fClass)) {
			for (Method eachMethod : MethodSorter.getDeclaredMethods(eachClass)) {
				addToAnnotationLists(new FrameworkMethod(eachMethod), fMethodsForAnnotations);
			}
			for (Field eachField : eachClass.getDeclaredFields()) {
				addToAnnotationLists(new FrameworkField(eachField), fFieldsForAnnotations);
			}
		}
	}

	/**
	 * 登记目标类的成员上所标注的注解。
	 * 
	 * @param member
	 *            类的成员。
	 * @param map
	 *            类的成员与注解的映射表。
	 */
	private <T extends FrameworkMember<T>> void addToAnnotationLists(T member, Map<Class<?>, List<T>> map) {
		
		for (Annotation each : member.getAnnotations()) {
			Class<? extends Annotation> type= each.annotationType();
			List<T> members= getAnnotatedMembers(map, type);
			if (member.isShadowedBy(members)) {// 跳过被重载的成员
				return;
			}
			/* 注意：这里添加并前置是因为超类的非重载Before/BeforeClass方法将早于子类调用 */
			if (runsTopToBottom(type)) {
				members.add(0, member);
			} else {
				members.add(member);
			}
		}
	}

	/**
	 * 获取被目标类型的注解所标注的类的方法列表。
	 * 
	 * @param annotationClass
	 *            注解类型。
	 * @return 被目标类型的注解所标注的类的方法列表。
	 */
	public List<FrameworkMethod> getAnnotatedMethods(Class<? extends Annotation> annotationClass) {

		return getAnnotatedMembers(fMethodsForAnnotations, annotationClass);
	}

	/**
	 * 获取被目标类型的注解所标注的类的成员变量列表。
	 * 
	 * @param annotationClass
	 *            注解类型。
	 * @return 被目标类型的注解所标注的类的成员变量列表。
	 */
	public List<FrameworkField> getAnnotatedFields(Class<? extends Annotation> annotationClass) {
		
		return getAnnotatedMembers(fFieldsForAnnotations, annotationClass);
	}

	/**
	 * 获取具有目标类型注解的类的成员列表。
	 * 
	 * @param map
	 *            登记类成员注解的集合。
	 * @param type
	 *            注解类型。
	 * @return 具有目标类型注解的类的成员列表。
	 */
	private <T> List<T> getAnnotatedMembers(Map<Class<?>, List<T>> map, Class<? extends Annotation> type) {

		if (!map.containsKey(type)) {
			map.put(type, new ArrayList<T>());
		}
		return map.get(type);
	}

	/**
	 * 判断目标注解是否为Before或BeforeClass注解。
	 * 
	 * @param annotation
	 *            目标注解。
	 * @return 如果目标注解为Before或BeforeClass注解则返回true，否则返回false。
	 */
	private boolean runsTopToBottom(Class<? extends Annotation> annotation) {

		return annotation.equals(Before.class) || annotation.equals(BeforeClass.class);
	}

	/**
	 * 获取目标类的所有超类，包括本身。
	 * 
	 * @param testClass
	 *            目标类。
	 * @return 目标类的超类列表。
	 */
	private List<Class<?>> getSuperClasses(Class<?> testClass) {

		ArrayList<Class<?>> results= new ArrayList<Class<?>>();
		Class<?> current= testClass;
		while (current != null) {
			results.add(current);
			current= current.getSuperclass();
		}
		return results;
	}

	/**
	 * 获取潜在的类类型。
	 * 
	 * @return 潜在的类类型。
	 */
	public Class<?> getJavaClass() {

		return fClass;
	}

	/**
	 * 获取类名，如果没有则返回“null”。
	 * 
	 * @return 类名，如果没有则返回“null”。
	 */
	public String getName() {

		if (fClass == null) {
			return "null";
		}
		return fClass.getName();
	}

	/**
	 * 获取当前测试类的唯一构造器，如果当前类的构造器不唯一将出现断言错误。
	 * 
	 * @return 当前测试类的构造器。
	 */
	public Constructor<?> getOnlyConstructor() {

		Constructor<?>[] constructors= fClass.getConstructors();
		Assert.assertEquals(1, constructors.length);
		return constructors[0];
	}

	/**
	 * 获取当前测试类上的注解。
	 * 
	 * @return 当前测试类上的注解，没有则返回空数组。
	 */
	public Annotation[] getAnnotations() {

		if (fClass == null) {
			return new Annotation[0];
		}
		return fClass.getAnnotations();
	}

	/**
	 * 获取目标对象上被指定类型的注解所标注的成员变量的值，并添加兼容指定类型的值到结果集，返回结果集。
	 * 
	 * @param test
	 *            目标对象。
	 * @param annotationClass
	 *            注解类型。
	 * @param valueClass
	 *            兼容的值类型。
	 * @return 结果集。
	 */
	public <T> List<T> getAnnotatedFieldValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass) {
		
		List<T> results= new ArrayList<T>();
		for (FrameworkField each : getAnnotatedFields(annotationClass)) {
			try {
				Object fieldValue= each.get(test);
				/* 如果域值与类型兼容则获取，否则不获取 */
				if (valueClass.isInstance(fieldValue)) {
					results.add(valueClass.cast(fieldValue));
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException("How did getFields return a field we couldn't access?", e);
			}
		}
		return results;
	}

	/**
	 * 由目标对象上调用其被指定类型的注解所标注的方法，并添加指定类型的返回值到结果集，返回结果集。
	 * 
	 * @param test
	 *            目标对象。
	 * @param annotationClass
	 *            注解类型。
	 * @param valueClass
	 *            兼容的方法返回值类型。
	 * @return 结果集。
	 */
	public <T> List<T> getAnnotatedMethodValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass) {
		List<T> results= new ArrayList<T>();
		for (FrameworkMethod each : getAnnotatedMethods(annotationClass)) {
			try {
				Object fieldValue= each.invokeExplosively(test, new Object[] {});
				if (valueClass.isInstance(fieldValue)) {
					results.add(valueClass.cast(fieldValue));
				}
			} catch (Throwable e) {
				throw new RuntimeException("Exception in " + each.getName(), e);
			}
		}
		return results;
	}

	/**
	 * 判断当前测试类是否为非静态的成员类。
	 * 
	 * @return 如果为非静态的成员类则返回true，否则返回false。
	 */
	public boolean isANonStaticInnerClass() {

		return fClass.isMemberClass() && !isStatic(fClass.getModifiers());
	}
}
