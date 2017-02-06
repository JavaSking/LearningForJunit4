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
 * �����������ʱ��װ��
 * 
 * @since 4.5
 * @author ע��By JavaSking 2017��2��5��
 */
public class TestClass {
	/**
	 * Ǳ�ڵ������͡�
	 */
	private final Class<?> fClass;

	/**
	 * ������ķ�����ע���ӳ���&lt;ע������, ���������ע�����͵�ע���ע�ķ����б�>��
	 */
	private Map<Class<?>, List<FrameworkMethod>> fMethodsForAnnotations= new HashMap<Class<?>, List<FrameworkMethod>>();

	/**
	 * ������ĳ�Ա������ע���ӳ���&lt;ע������, ���������ע�����͵�ע���ע�ĳ�Ա�����б�>��
	 */
	private Map<Class<?>, List<FrameworkField>> fFieldsForAnnotations= new HashMap<Class<?>, List<FrameworkField>>();

	/**
	 * ����Ŀ������ࣨ������ֻ����һ�����췽����������ʱ��װ��<br>
	 * 
	 * @param klass
	 *            Ŀ������ࡣ
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
	 * �Ǽ�Ŀ����ĳ�Ա������ע��ע�⡣
	 * 
	 * @param member
	 *            ��ĳ�Ա��
	 * @param map
	 *            ��ĳ�Ա��ע���ӳ���
	 */
	private <T extends FrameworkMember<T>> void addToAnnotationLists(T member, Map<Class<?>, List<T>> map) {
		
		for (Annotation each : member.getAnnotations()) {
			Class<? extends Annotation> type= each.annotationType();
			List<T> members= getAnnotatedMembers(map, type);
			if (member.isShadowedBy(members)) {// ���������صĳ�Ա
				return;
			}
			/* ע�⣺������Ӳ�ǰ������Ϊ����ķ�����Before/BeforeClass����������������� */
			if (runsTopToBottom(type)) {
				members.add(0, member);
			} else {
				members.add(member);
			}
		}
	}

	/**
	 * ��ȡ��Ŀ�����͵�ע������ע����ķ����б�
	 * 
	 * @param annotationClass
	 *            ע�����͡�
	 * @return ��Ŀ�����͵�ע������ע����ķ����б�
	 */
	public List<FrameworkMethod> getAnnotatedMethods(Class<? extends Annotation> annotationClass) {

		return getAnnotatedMembers(fMethodsForAnnotations, annotationClass);
	}

	/**
	 * ��ȡ��Ŀ�����͵�ע������ע����ĳ�Ա�����б�
	 * 
	 * @param annotationClass
	 *            ע�����͡�
	 * @return ��Ŀ�����͵�ע������ע����ĳ�Ա�����б�
	 */
	public List<FrameworkField> getAnnotatedFields(Class<? extends Annotation> annotationClass) {
		
		return getAnnotatedMembers(fFieldsForAnnotations, annotationClass);
	}

	/**
	 * ��ȡ����Ŀ������ע�����ĳ�Ա�б�
	 * 
	 * @param map
	 *            �Ǽ����Աע��ļ��ϡ�
	 * @param type
	 *            ע�����͡�
	 * @return ����Ŀ������ע�����ĳ�Ա�б�
	 */
	private <T> List<T> getAnnotatedMembers(Map<Class<?>, List<T>> map, Class<? extends Annotation> type) {

		if (!map.containsKey(type)) {
			map.put(type, new ArrayList<T>());
		}
		return map.get(type);
	}

	/**
	 * �ж�Ŀ��ע���Ƿ�ΪBefore��BeforeClassע�⡣
	 * 
	 * @param annotation
	 *            Ŀ��ע�⡣
	 * @return ���Ŀ��ע��ΪBefore��BeforeClassע���򷵻�true�����򷵻�false��
	 */
	private boolean runsTopToBottom(Class<? extends Annotation> annotation) {

		return annotation.equals(Before.class) || annotation.equals(BeforeClass.class);
	}

	/**
	 * ��ȡĿ��������г��࣬��������
	 * 
	 * @param testClass
	 *            Ŀ���ࡣ
	 * @return Ŀ����ĳ����б�
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
	 * ��ȡǱ�ڵ������͡�
	 * 
	 * @return Ǳ�ڵ������͡�
	 */
	public Class<?> getJavaClass() {

		return fClass;
	}

	/**
	 * ��ȡ���������û���򷵻ء�null����
	 * 
	 * @return ���������û���򷵻ء�null����
	 */
	public String getName() {

		if (fClass == null) {
			return "null";
		}
		return fClass.getName();
	}

	/**
	 * ��ȡ��ǰ�������Ψһ�������������ǰ��Ĺ�������Ψһ�����ֶ��Դ���
	 * 
	 * @return ��ǰ������Ĺ�������
	 */
	public Constructor<?> getOnlyConstructor() {

		Constructor<?>[] constructors= fClass.getConstructors();
		Assert.assertEquals(1, constructors.length);
		return constructors[0];
	}

	/**
	 * ��ȡ��ǰ�������ϵ�ע�⡣
	 * 
	 * @return ��ǰ�������ϵ�ע�⣬û���򷵻ؿ����顣
	 */
	public Annotation[] getAnnotations() {

		if (fClass == null) {
			return new Annotation[0];
		}
		return fClass.getAnnotations();
	}

	/**
	 * ��ȡĿ������ϱ�ָ�����͵�ע������ע�ĳ�Ա������ֵ������Ӽ���ָ�����͵�ֵ������������ؽ������
	 * 
	 * @param test
	 *            Ŀ�����
	 * @param annotationClass
	 *            ע�����͡�
	 * @param valueClass
	 *            ���ݵ�ֵ���͡�
	 * @return �������
	 */
	public <T> List<T> getAnnotatedFieldValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass) {
		
		List<T> results= new ArrayList<T>();
		for (FrameworkField each : getAnnotatedFields(annotationClass)) {
			try {
				Object fieldValue= each.get(test);
				/* �����ֵ�����ͼ������ȡ�����򲻻�ȡ */
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
	 * ��Ŀ������ϵ����䱻ָ�����͵�ע������ע�ķ����������ָ�����͵ķ���ֵ������������ؽ������
	 * 
	 * @param test
	 *            Ŀ�����
	 * @param annotationClass
	 *            ע�����͡�
	 * @param valueClass
	 *            ���ݵķ�������ֵ���͡�
	 * @return �������
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
	 * �жϵ�ǰ�������Ƿ�Ϊ�Ǿ�̬�ĳ�Ա�ࡣ
	 * 
	 * @return ���Ϊ�Ǿ�̬�ĳ�Ա���򷵻�true�����򷵻�false��
	 */
	public boolean isANonStaticInnerClass() {

		return fClass.isMemberClass() && !isStatic(fClass.getModifiers());
	}
}
