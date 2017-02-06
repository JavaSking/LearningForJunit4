package org.junit.runner;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试内容描述信息。被设计为树状的结构<br>
 * 设计模式：组合模式。
 * 
 * <pre>
 * 通过createTestDescription方法创建的Description为叶子节点。
 * </pre>
 * 
 * 叶子节点通常代表单一的方法测试。<br>
 * 非叶子节点通常代表一个类测试或测试组。<br>
 * 
 * @see org.junit.runner.Request
 * @see org.junit.runner.Runner
 * @since 4.0
 * @author 注释By JavaSking 2017年2月5日
 */
public class Description implements Serializable {
	/**
	 * 序列号。
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * 方法或类名模式：方法名（类名）。
	 */
	private static final Pattern METHOD_AND_CLASS_NAME_PATTERN= Pattern.compile("(.*)\\((.*)\\)");

	/**
	 * 创建指定名称的测试内容描述信息。非叶子节点<br>
	 * 
	 * @param name
	 *            名称。
	 * @param annotations
	 *            注解。
	 * @return 新建的测试内容描述信息对象。
	 */
	public static Description createSuiteDescription(String name, Annotation... annotations) {

		return new Description(null, name, annotations);
	}

	/**
	 * 创建指定名称的测试内容描述信息对象。非叶子节点。<br>
	 * 
	 * @param name
	 *            名称。
	 * @param uniqueId
	 *            序列号。
	 * @param annotations
	 *            注解。
	 * @return 新建的测试内容描述信息对象。
	 */
	public static Description createSuiteDescription(String name, Serializable uniqueId, Annotation... annotations) {

		return new Description(null, name, uniqueId, annotations);
	}

	/**
	 * 创建一个测试内容描述信息对象，该Description一般作为叶子节点。
	 * 
	 * @param className
	 *            测试类名。
	 * @param name
	 *            名称（通常为方法名）。
	 * @param annotations
	 *            注解（通常为测试方法上的注解）。
	 * @return 测试内容描述信息对象。
	 */
	public static Description createTestDescription(String className, String name, Annotation... annotations) {

		return new Description(null, formatDisplayName(name, className), annotations);
	}

	/**
	 * 创建一个测试内容描述信息对象，该Description一般作为叶子节点。
	 * 
	 * @param clazz
	 *            测试类。
	 * @param name
	 *            名称（通常为方法名）。
	 * @param annotations
	 *            注解（通常为测试方法上的注解）。
	 * @return 测试内容描述信息对象。
	 */
	public static Description createTestDescription(Class<?> clazz, String name, Annotation... annotations) {
		
		return new Description(clazz, formatDisplayName(name, clazz.getName()), annotations);
	}

	/**
	 * 创建一个测试内容描述信息对象，该Description一般作为叶子节点。<br>
	 * 
	 * @param clazz
	 *            测试类。
	 * @param name
	 *            名称（通常为方法名）。
	 * @return 测试内容描述信息对象。
	 */
	public static Description createTestDescription(Class<?> clazz, String name) {

		return new Description(clazz, formatDisplayName(name, clazz.getName()));
	}

	/**
	 * 创建一个测试内容描述信息对象，该Description一般作为叶子节点。
	 * 
	 * @param className
	 *            测试类名。
	 * @param name
	 *            名称（通常为方法名）。
	 * @param uniqueId
	 *            序列号。
	 * @return 测试内容描述信息对象。
	 */
	public static Description createTestDescription(String className, String name, Serializable uniqueId) {

		return new Description(null, formatDisplayName(name, className), uniqueId);
	}

	/**
	 * 格式化展现名称:方法名（类名）。
	 * 
	 * @param name
	 *            名称（通常为方法名）。
	 * @param className
	 *            类名。
	 * @return 格式化后的展现名称。
	 */
	private static String formatDisplayName(String name, String className) {

		return String.format("%s(%s)", name, className);
	}

	/**
	 * 新建目标测试类的测试内容描述信息，非叶子节点。
	 * 
	 * @param testClass
	 *            目标测试类。
	 * @return 目标测试类的测试内容描述信息。
	 */
	public static Description createSuiteDescription(Class<?> testClass) {

		return new Description(testClass, testClass.getName(), testClass.getAnnotations());
	}

	/**
	 * 特殊叶子节点：空的测试内容描述信息节点。
	 */
	public static final Description EMPTY= new Description(null, "No Tests");

	/**
	 * 特殊叶子节点：特定描述的测试内容描述信息节点。
	 */
	public static final Description TEST_MECHANISM= new Description(null, "Test mechanism");

	/**
	 * 测试内容描述信息子节点列表。
	 */
	private final ArrayList<Description> fChildren= new ArrayList<Description>();

	/**
	 * 展现名称。
	 */
	private final String fDisplayName;

	/**
	 * 序列号。
	 */
	private final Serializable fUniqueId;

	/**
	 * 注解数组。
	 */
	private final Annotation[] fAnnotations;

	/**
	 * 测试类类型。
	 */
	private /* write-once */ Class<?> fTestClass;

	/**
	 * 构造一个测试描述内容描述信息对象。
	 * 
	 * @param clazz
	 *            测试类。
	 * @param displayName
	 *            展现名称（同时作为序列号）。
	 * @param annotations
	 *            注解。
	 */
	private Description(Class<?> clazz, String displayName, Annotation... annotations) {

		this(clazz, displayName, displayName, annotations);
	}

	/**
	 * 构造一个测试内容描述信息对象。
	 * 
	 * @param clazz
	 *            测试类。
	 * @param displayName
	 *            展现名称。
	 * @param uniqueId
	 *            序列号。
	 * @param annotations
	 *            注解。
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
	 * 获取展现名称。
	 * 
	 * @return 展现名称。
	 */
	public String getDisplayName() {

		return fDisplayName;
	}

	/**
	 * 添加测试内容描述信息子节点。
	 * 
	 * @param description
	 *            待添加的测试内容描述信息。
	 */
	public void addChild(Description description) {

		getChildren().add(description);
	}

	/**
	 * 获取测试内容描述信息子节点列表。
	 * 
	 * @return 测试内容描述信息子节点列表。
	 */
	public ArrayList<Description> getChildren() {

		return fChildren;
	}

	/**
	 * 判断是否为测试组。
	 * 
	 * @return 如果为测试组则返回true，否则返回false。
	 */
	public boolean isSuite() {

		return !isTest();
	}

	/**
	 * 判断是否为原子测试（单一的方法测试）。
	 * 
	 * @return 如果为原子测试则返回true，否则返回false。
	 */
	public boolean isTest() {

		return getChildren().isEmpty();
	}

	/**
	 * 统计原子测试数。
	 * 
	 * @return 原子测试数。
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
	 * 判断是否为空的测试内容描述信息对象。
	 * 
	 * @return 如果为空的测试内容描述信息对象则返回true，否则返回false。
	 */
	public boolean isEmpty() {

		return equals(EMPTY);
	}

	/**
	 * 浅拷贝当前测试内容描述信息，不包含测试内容描述信息子节点。<br>
	 * 
	 * @return 当前测试内容描述信息的浅拷贝，不包含测试内容描述信息子节点。
	 */
	public Description childlessCopy() {

		return new Description(fTestClass, fDisplayName, fAnnotations);
	}

	/**
	 * 获取目标类型的注解，没有则返回null。
	 * 
	 * @param annotationType
	 *            注解类型。
	 * @return 目标类型的注解，没有则返回null。
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
	 * 获取注解集合。
	 * 
	 * @return 注解集合。
	 */
	public Collection<Annotation> getAnnotations() {

		return Arrays.asList(fAnnotations);
	}

	/**
	 * 获取测试类的类型，没有则返回null。
	 * 
	 * @return 测试类类型，没有则返回null。
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
	 * 尝试获取测试类名，没有则返回展现名称。
	 * 
	 * @return 测试类名，没有则返回展现名称。
	 */
	public String getClassName() {

		return fTestClass != null ? fTestClass.getName() : methodAndClassNamePatternGroupOrDefault(2, toString());
	}

	/**
	 * 获取方法名，如果没有则返回null。
	 * 
	 * @return 方法名，如果没有则返回null。
	 */
	public String getMethodName() {

		return methodAndClassNamePatternGroupOrDefault(1, null);
	}

	/**
	 * 尝试将当前的展现名称解析为模式：<b>方法名（类名）</b>并获取方法名或类名，如果没有则返回默认字符串<br>
	 * 
	 * @param group 分组。
	 * 
	 * @param defaultString 默认字符串。
	 * 
	 * @return 方法名（分组1）或类名（分组2），没有则返回默认字符串。
	 */
	private String methodAndClassNamePatternGroupOrDefault(int group, String defaultString) {

		Matcher matcher= METHOD_AND_CLASS_NAME_PATTERN.matcher(toString());
		return matcher.matches() ? matcher.group(group) : defaultString;
	}
}