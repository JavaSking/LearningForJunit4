package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * ������ĳ�Ա�������򣩵�����ʱ��װ��
 * 
 * @since 4.7
 * @author ע��By JavaSking 2017��2��4��
 */
public class FrameworkField extends FrameworkMember<FrameworkField> {
	/**
	 * Ǳ�ڵĳ�Ա������
	 */
	private final Field fField;

	/**
	 * ����һ��Ǳ�ڳ�Ա����������ʱ��װ��
	 * 
	 * @param field
	 *            Ǳ�ڳ�Ա������
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
	 * ��ȡǱ�ڵĳ�Ա������
	 * 
	 * @return Ǳ�ڵĳ�Ա������
	 */
	public Field getField() {

		return fField;
	}

	@Override
	public Class<?> getType() {

		return fField.getType();
	}

	/**
	 * ���Ի�ȡĿ������ϴ˳�Ա�������򣩵�ֵ��
	 * 
	 * @param target
	 *            Ŀ�����
	 * @return Ŀ������ϴ˳�Ա�������򣩵�ֵ��
	 * @throws IllegalArgumentException
	 *             �����쳣��
	 * @throws IllegalAccessException
	 *             �����쳣��
	 */
	public Object get(Object target) throws IllegalArgumentException, IllegalAccessException {

		return fField.get(target);
	}
}
