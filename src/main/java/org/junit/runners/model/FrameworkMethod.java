package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.internal.runners.model.ReflectiveCallable;

/**
 * ������ķ���������ʱ��װ��<br>
 * ����ķ���ͨ��ָ��@Test,@Before,@After,@BeforeClass��ע���ע�ķ�����
 * 
 * @since 4.5
 * @author ע��By JavaSking 2017��2��4��
 */
public class FrameworkMethod extends FrameworkMember<FrameworkMethod> {
	/**
	 * Ǳ�ڷ�����
	 */
	final Method fMethod;

	/**
	 * ����һ��Ǳ�ڷ���������ʱ��װ��
	 * 
	 * @param method
	 *            Ǳ�ڷ�����
	 */
	public FrameworkMethod(Method method) {

		fMethod= method;
	}

	/**
	 * ��ȡǱ�ڷ�����
	 * 
	 * @return Ǳ�ڷ�����
	 */
	public Method getMethod() {

		return fMethod;
	}

	/**
	 * ��Ŀ�����ʹ��Ŀ�귽���������õ�ǰ�����������ؽ����
	 * 
	 * @param target
	 *            Ŀ�����
	 * @param params
	 *            Ŀ�귽��������
	 * @return �����
	 * @throws Throwable
	 *             �쳣��
	 */
	public Object invokeExplosively(final Object target, final Object... params) throws Throwable {

		return new ReflectiveCallable() {
			@Override
			protected Object runReflectiveCall() throws Throwable {
				return fMethod.invoke(target, params);
			}
		}.run();
	}

	@Override
	public String getName() {

		return fMethod.getName();
	}

	/**
	 * �жϵ�ǰ�����Ƿ�Ϊ�������޷���ֵ���޲η��������ҷ���{@code isStatic}������<br>
	 * �������������Ӵ��󵽴����б��С�
	 * 
	 * @param isStatic
	 *            �Ƿ�Ϊ��̬������
	 * @param errors
	 *            �����б�
	 */
	public void validatePublicVoidNoArg(boolean isStatic, List<Throwable> errors) {

		validatePublicVoid(isStatic, errors);
		if (fMethod.getParameterTypes().length != 0) {
			errors.add(new Exception("Method " + fMethod.getName() + " should have no parameters"));
		}
	}

	/**
	 * �жϵ�ǰ�����Ƿ�Ϊ�������޷���ֵ�ķ��������ҷ���{@code isStatic}������<br>
	 * �������������Ӵ��󵽴����б��С�
	 * 
	 * @param isStatic
	 *            �Ƿ�Ϊ��̬������
	 * @param errors
	 *            �����б�
	 */
	public void validatePublicVoid(boolean isStatic, List<Throwable> errors) {

		if (Modifier.isStatic(fMethod.getModifiers()) != isStatic) {
			String state= isStatic ? "should" : "should not";
			errors.add(new Exception("Method " + fMethod.getName() + "() " + state + " be static"));
		}
		if (!Modifier.isPublic(fMethod.getDeclaringClass().getModifiers())) {
			errors.add(new Exception("Class " + fMethod.getDeclaringClass().getName() + " should be public"));
		}
		if (!Modifier.isPublic(fMethod.getModifiers())) {
			errors.add(new Exception("Method " + fMethod.getName() + "() should be public"));
		}
		if (fMethod.getReturnType() != Void.TYPE) {
			errors.add(new Exception("Method " + fMethod.getName() + "() should be void"));
		}
	}

	@Override
	public boolean isStatic() {

		return Modifier.isStatic(fMethod.getModifiers());
	}

	@Override
	public boolean isPublic() {

		return Modifier.isPublic(fMethod.getModifiers());
	}

	/**
	 * ��ȡ�����ķ���ֵ��
	 * 
	 * @return �����ķ���ֵ��
	 */
	public Class<?> getReturnType() {

		return fMethod.getReturnType();
	}

	@Override
	public Class<?> getType() {

		return getReturnType();
	}

	public void validateNoTypeParametersOnArgs(List<Throwable> errors) {

		new NoGenericTypeParametersValidator(fMethod).validate(errors);
	}

	@Override
	public boolean isShadowedBy(FrameworkMethod other) {

		if (!other.getName().equals(getName())) {
			return false;
		}
		if (other.getParameterTypes().length != getParameterTypes().length) {
			return false;
		}
		for (int i= 0; i < other.getParameterTypes().length; i++) {
			if (!other.getParameterTypes()[i].equals(getParameterTypes()[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {

		if (!FrameworkMethod.class.isInstance(obj)) {
			return false;
		}
		return ((FrameworkMethod) obj).fMethod.equals(fMethod);
	}

	@Override
	public int hashCode() {

		return fMethod.hashCode();
	}

	/**
	 * �жϴ˷����Ƿ�Ϊ�޲η��������ҷ���ֵ����Ŀ�����͡�
	 * 
	 * @param type Ŀ�����͡�
	 *
	 * @deprecated
	 */
	@Deprecated
	public boolean producesType(Type type) {

		return getParameterTypes().length == 0 && type instanceof Class<?> && ((Class<?>) type).isAssignableFrom(fMethod.getReturnType());
	}

	/**
	 * ��ȡ���������������顣
	 * 
	 * @return ���������������顣
	 */
	private Class<?>[] getParameterTypes() {

		return fMethod.getParameterTypes();
	}

	@Override
	public Annotation[] getAnnotations() {

		return fMethod.getAnnotations();
	}

	/**
	 * ��ȡ�˷�����ָ�����͵�ע�⡣
	 * 
	 * @param annotationType
	 *            ע�����͡�
	 * @return �˷�����ָ�����͵�ע�⡣
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {

		return fMethod.getAnnotation(annotationType);
	}
}
