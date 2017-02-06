package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * 测试类的成员变量（域）的运行时包装。
 * 
 * @since 4.7
 * @author 注释By JavaSking 2017年2月4日
 */
public class FrameworkField extends FrameworkMember<FrameworkField> {
	/**
	 * 潜在的成员变量。
	 */
	private final Field fField;

	/**
	 * 构造一个潜在成员变量的运行时包装。
	 * 
	 * @param field
	 *            潜在成员变量。
	 */
	FrameworkField(Field field) {

		fField= field;
	}

	@Override
	public String getName() {

		return getField().getName();
	}

	@Override
	public Annotation[] getAnnotations() {

		return fField.getAnnotations();
	}

	@Override
	public boolean isPublic() {

		int modifiers= fField.getModifiers();
		return Modifier.isPublic(modifiers);
	}

	@Override
	public boolean isShadowedBy(FrameworkField otherMember) {

		return otherMember.getName().equals(getName());
	}

	@Override
	public boolean isStatic() {

		int modifiers= fField.getModifiers();
		return Modifier.isStatic(modifiers);
	}

	/**
	 * 获取潜在的成员变量。
	 * 
	 * @return 潜在的成员变量。
	 */
	public Field getField() {

		return fField;
	}

	@Override
	public Class<?> getType() {

		return fField.getType();
	}

	/**
	 * 尝试获取目标对象上此成员变量（域）的值。
	 * 
	 * @param target
	 *            目标对象。
	 * @return 目标对象上此成员变量（域）的值。
	 * @throws IllegalArgumentException
	 *             参数异常。
	 * @throws IllegalAccessException
	 *             访问异常。
	 */
	public Object get(Object target) throws IllegalArgumentException, IllegalAccessException {

		return fField.get(target);
	}
}
