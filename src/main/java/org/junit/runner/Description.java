package org.junit.runner;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ��������������Ϣ�������Ϊ��״�Ľṹ<br>
 * ���ģʽ�����ģʽ��
 * 
 * <pre>
 * ͨ��createTestDescription����������DescriptionΪҶ�ӽڵ㡣
 * </pre>
 * 
 * Ҷ�ӽڵ�ͨ������һ�ķ������ԡ�<br>
 * ��Ҷ�ӽڵ�ͨ������һ������Ի�����顣<br>
 * 
 * @see org.junit.runner.Request
 * @see org.junit.runner.Runner
 * @since 4.0
 * @author ע��By JavaSking 2017��2��5��
 */
public class Description implements Serializable {
	/**
	 * ���кš�
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * ����������ģʽ������������������
	 */
	private static final Pattern METHOD_AND_CLASS_NAME_PATTERN= Pattern.compile("(.*)\\((.*)\\)");

	/**
	 * ����ָ�����ƵĲ�������������Ϣ����Ҷ�ӽڵ�<br>
	 * 
	 * @param name
	 *            ���ơ�
	 * @param annotations
	 *            ע�⡣
	 * @return �½��Ĳ�������������Ϣ����
	 */
	public static Description createSuiteDescription(String name, Annotation... annotations) {

		return new Description(null, name, annotations);
	}

	/**
	 * ����ָ�����ƵĲ�������������Ϣ���󡣷�Ҷ�ӽڵ㡣<br>
	 * 
	 * @param name
	 *            ���ơ�
	 * @param uniqueId
	 *            ���кš�
	 * @param annotations
	 *            ע�⡣
	 * @return �½��Ĳ�������������Ϣ����
	 */
	public static Description createSuiteDescription(String name, Serializable uniqueId, Annotation... annotations) {

		return new Description(null, name, uniqueId, annotations);
	}

	/**
	 * ����һ����������������Ϣ���󣬸�Descriptionһ����ΪҶ�ӽڵ㡣
	 * 
	 * @param className
	 *            ����������
	 * @param name
	 *            ���ƣ�ͨ��Ϊ����������
	 * @param annotations
	 *            ע�⣨ͨ��Ϊ���Է����ϵ�ע�⣩��
	 * @return ��������������Ϣ����
	 */
	public static Description createTestDescription(String className, String name, Annotation... annotations) {

		return new Description(null, formatDisplayName(name, className), annotations);
	}

	/**
	 * ����һ����������������Ϣ���󣬸�Descriptionһ����ΪҶ�ӽڵ㡣
	 * 
	 * @param clazz
	 *            �����ࡣ
	 * @param name
	 *            ���ƣ�ͨ��Ϊ����������
	 * @param annotations
	 *            ע�⣨ͨ��Ϊ���Է����ϵ�ע�⣩��
	 * @return ��������������Ϣ����
	 */
	public static Description createTestDescription(Class<?> clazz, String name, Annotation... annotations) {
		
		return new Description(clazz, formatDisplayName(name, clazz.getName()), annotations);
	}

	/**
	 * ����һ����������������Ϣ���󣬸�Descriptionһ����ΪҶ�ӽڵ㡣<br>
	 * 
	 * @param clazz
	 *            �����ࡣ
	 * @param name
	 *            ���ƣ�ͨ��Ϊ����������
	 * @return ��������������Ϣ����
	 */
	public static Description createTestDescription(Class<?> clazz, String name) {

		return new Description(clazz, formatDisplayName(name, clazz.getName()));
	}

	/**
	 * ����һ����������������Ϣ���󣬸�Descriptionһ����ΪҶ�ӽڵ㡣
	 * 
	 * @param className
	 *            ����������
	 * @param name
	 *            ���ƣ�ͨ��Ϊ����������
	 * @param uniqueId
	 *            ���кš�
	 * @return ��������������Ϣ����
	 */
	public static Description createTestDescription(String className, String name, Serializable uniqueId) {

		return new Description(null, formatDisplayName(name, className), uniqueId);
	}

	/**
	 * ��ʽ��չ������:����������������
	 * 
	 * @param name
	 *            ���ƣ�ͨ��Ϊ����������
	 * @param className
	 *            ������
	 * @return ��ʽ�����չ�����ơ�
	 */
	private static String formatDisplayName(String name, String className) {

		return String.format("%s(%s)", name, className);
	}

	/**
	 * �½�Ŀ�������Ĳ�������������Ϣ����Ҷ�ӽڵ㡣
	 * 
	 * @param testClass
	 *            Ŀ������ࡣ
	 * @return Ŀ�������Ĳ�������������Ϣ��
	 */
	public static Description createSuiteDescription(Class<?> testClass) {

		return new Description(testClass, testClass.getName(), testClass.getAnnotations());
	}

	/**
	 * ����Ҷ�ӽڵ㣺�յĲ�������������Ϣ�ڵ㡣
	 */
	public static final Description EMPTY= new Description(null, "No Tests");

	/**
	 * ����Ҷ�ӽڵ㣺�ض������Ĳ�������������Ϣ�ڵ㡣
	 */
	public static final Description TEST_MECHANISM= new Description(null, "Test mechanism");

	/**
	 * ��������������Ϣ�ӽڵ��б�
	 */
	private final ArrayList<Description> fChildren= new ArrayList<Description>();

	/**
	 * չ�����ơ�
	 */
	private final String fDisplayName;

	/**
	 * ���кš�
	 */
	private final Serializable fUniqueId;

	/**
	 * ע�����顣
	 */
	private final Annotation[] fAnnotations;

	/**
	 * ���������͡�
	 */
	private /* write-once */ Class<?> fTestClass;

	/**
	 * ����һ��������������������Ϣ����
	 * 
	 * @param clazz
	 *            �����ࡣ
	 * @param displayName
	 *            չ�����ƣ�ͬʱ��Ϊ���кţ���
	 * @param annotations
	 *            ע�⡣
	 */
	private Description(Class<?> clazz, String displayName, Annotation... annotations) {

		this(clazz, displayName, displayName, annotations);
	}

	/**
	 * ����һ����������������Ϣ����
	 * 
	 * @param clazz
	 *            �����ࡣ
	 * @param displayName
	 *            չ�����ơ�
	 * @param uniqueId
	 *            ���кš�
	 * @param annotations
	 *            ע�⡣
	 */
	private Description(Class<?> clazz, String displayName, Serializable uniqueId, Annotation... annotations) {
		
		if ((displayName == null) || (displayName.length() == 0)) {
			throw new IllegalArgumentException("The display name must not be empty.");
		}
		if ((uniqueId == null)) {
			throw new IllegalArgumentException("The unique id must not be null.");
		}
		fTestClass= clazz;
		fDisplayName= displayName;
		fUniqueId= uniqueId;
		fAnnotations= annotations;
	}

	/**
	 * ��ȡչ�����ơ�
	 * 
	 * @return չ�����ơ�
	 */
	public String getDisplayName() {

		return fDisplayName;
	}

	/**
	 * ��Ӳ�������������Ϣ�ӽڵ㡣
	 * 
	 * @param description
	 *            ����ӵĲ�������������Ϣ��
	 */
	public void addChild(Description description) {

		getChildren().add(description);
	}

	/**
	 * ��ȡ��������������Ϣ�ӽڵ��б�
	 * 
	 * @return ��������������Ϣ�ӽڵ��б�
	 */
	public ArrayList<Description> getChildren() {

		return fChildren;
	}

	/**
	 * �ж��Ƿ�Ϊ�����顣
	 * 
	 * @return ���Ϊ�������򷵻�true�����򷵻�false��
	 */
	public boolean isSuite() {

		return !isTest();
	}

	/**
	 * �ж��Ƿ�Ϊԭ�Ӳ��ԣ���һ�ķ������ԣ���
	 * 
	 * @return ���Ϊԭ�Ӳ����򷵻�true�����򷵻�false��
	 */
	public boolean isTest() {

		return getChildren().isEmpty();
	}

	/**
	 * ͳ��ԭ�Ӳ�������
	 * 
	 * @return ԭ�Ӳ�������
	 */
	public int testCount() {

		if (isTest()) {
			return 1;
		}
		int result= 0;
		for (Description child : getChildren()) {
			result+= child.testCount();
		}
		return result;
	}

	@Override
	public int hashCode() {

		return fUniqueId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Description)) {
			return false;
		}
		Description d= (Description) obj;
		return fUniqueId.equals(d.fUniqueId);
	}

	@Override
	public String toString() {

		return getDisplayName();
	}

	/**
	 * �ж��Ƿ�Ϊ�յĲ�������������Ϣ����
	 * 
	 * @return ���Ϊ�յĲ�������������Ϣ�����򷵻�true�����򷵻�false��
	 */
	public boolean isEmpty() {

		return equals(EMPTY);
	}

	/**
	 * ǳ������ǰ��������������Ϣ����������������������Ϣ�ӽڵ㡣<br>
	 * 
	 * @return ��ǰ��������������Ϣ��ǳ��������������������������Ϣ�ӽڵ㡣
	 */
	public Description childlessCopy() {

		return new Description(fTestClass, fDisplayName, fAnnotations);
	}

	/**
	 * ��ȡĿ�����͵�ע�⣬û���򷵻�null��
	 * 
	 * @param annotationType
	 *            ע�����͡�
	 * @return Ŀ�����͵�ע�⣬û���򷵻�null��
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {

		for (Annotation each : fAnnotations) {
			if (each.annotationType().equals(annotationType)) {
				return annotationType.cast(each);
			}
		}
		return null;
	}

	/**
	 * ��ȡע�⼯�ϡ�
	 * 
	 * @return ע�⼯�ϡ�
	 */
	public Collection<Annotation> getAnnotations() {

		return Arrays.asList(fAnnotations);
	}

	/**
	 * ��ȡ����������ͣ�û���򷵻�null��
	 * 
	 * @return ���������ͣ�û���򷵻�null��
	 */
	public Class<?> getTestClass() {

		if (fTestClass != null) {
			return fTestClass;
		}
		String name= getClassName();
		if (name == null) {
			return null;
		}
		try {
			fTestClass= Class.forName(name, false, getClass().getClassLoader());
			return fTestClass;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * ���Ի�ȡ����������û���򷵻�չ�����ơ�
	 * 
	 * @return ����������û���򷵻�չ�����ơ�
	 */
	public String getClassName() {

		return fTestClass != null ? fTestClass.getName() : methodAndClassNamePatternGroupOrDefault(2, toString());
	}

	/**
	 * ��ȡ�����������û���򷵻�null��
	 * 
	 * @return �����������û���򷵻�null��
	 */
	public String getMethodName() {

		return methodAndClassNamePatternGroupOrDefault(1, null);
	}

	/**
	 * ���Խ���ǰ��չ�����ƽ���Ϊģʽ��<b>��������������</b>����ȡ�����������������û���򷵻�Ĭ���ַ���<br>
	 * 
	 * @param group ���顣
	 * 
	 * @param defaultString Ĭ���ַ�����
	 * 
	 * @return ������������1��������������2����û���򷵻�Ĭ���ַ�����
	 */
	private String methodAndClassNamePatternGroupOrDefault(int group, String defaultString) {

		Matcher matcher= METHOD_AND_CLASS_NAME_PATTERN.matcher(toString());
		return matcher.matches() ? matcher.group(group) : defaultString;
	}
}