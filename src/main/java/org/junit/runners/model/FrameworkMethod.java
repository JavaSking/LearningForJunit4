package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.internal.runners.model.ReflectiveCallable;

/**
 * 测试类的方法的运行时包装。<br>
 * 这里的方法通常指被@Test,@Before,@After,@BeforeClass等注解标注的方法。
 * 
 * @since 4.5
 * @author 注释By JavaSking 2017年2月4日
 */
public class FrameworkMethod extends FrameworkMember<FrameworkMethod> {
	/**
	 * 潜在方法。
	 */
	final Method fMethod;

	/**
	 * 构造一个潜在方法的运行时包装。
	 * 
	 * @param method
	 *            潜在方法。
	 */
	public FrameworkMethod(Method method) {

		fMethod= method;
	}

	/**
	 * 获取潜在方法。
	 * 
	 * @return 潜在方法。
	 */
	public Method getMethod() {

		return fMethod;
	}

	/**
	 * 由目标对象使用目标方法参数调用当前方法，并返回结果。
	 * 
	 * @param target
	 *            目标对象。
	 * @param params
	 *            目标方法参数。
	 * @return 结果。
	 * @throws Throwable
	 *             异常。
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
	 * 判断当前方法是否为公共的无返回值，无参方法，并且符合{@code isStatic}参数。<br>
	 * 如果不符合则添加错误到错误列表中。
	 * 
	 * @param isStatic
	 *            是否为静态方法。
	 * @param errors
	 *            错误列表。
	 */
	public void validatePublicVoidNoArg(boolean isStatic, List<Throwable> errors) {

		validatePublicVoid(isStatic, errors);
		if (fMethod.getParameterTypes().length != 0) {
			errors.add(new Exception("Method " + fMethod.getName() + " should have no parameters"));
		}
	}

	/**
	 * 判断当前方法是否为公共的无返回值的方法，并且符合{@code isStatic}参数。<br>
	 * 如果不符合则添加错误到错误列表中。
	 * 
	 * @param isStatic
	 *            是否为静态方法。
	 * @param errors
	 *            错误列表。
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
	 * 获取方法的返回值。
	 * 
	 * @return 方法的返回值。
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
	 * 判断此方法是否为无参方法，并且返回值兼容目标类型。
	 * 
	 * @param type 目标类型。
	 *
	 * @deprecated
	 */
	@Deprecated
	public boolean producesType(Type type) {

		return getParameterTypes().length == 0 && type instanceof Class<?> && ((Class<?>) type).isAssignableFrom(fMethod.getReturnType());
	}

	/**
	 * 获取方法参数类型数组。
	 * 
	 * @return 方法参数类型数组。
	 */
	private Class<?>[] getParameterTypes() {

		return fMethod.getParameterTypes();
	}

	@Override
	public Annotation[] getAnnotations() {

		return fMethod.getAnnotations();
	}

	/**
	 * 获取此方法上指定类型的注解。
	 * 
	 * @param annotationType
	 *            注解类型。
	 * @return 此方法上指定类型的注解。
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {

		return fMethod.getAnnotation(annotationType);
	}
}
