package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 测试类的类成员的运行时包装。<br>
 * <ul>
 * <li>成员变量（域）被包装为{@link FrameworkField}。
 * <li>方法被包装{@link FrameworkMethod}。
 * </ul>
 * 
 * @since 4.7
 * @author 注释By JavaSking 2017年2月4日
 */
public abstract class FrameworkMember<T extends FrameworkMember<T>> {

	/**
	 * 获取标注在此成员上的注解。
	 * 
	 * @return 标注在此成员上的注解。
	 */
	abstract Annotation[] getAnnotations();

	/**
	 * 判断当前成员是否被目标成员重载。
	 * <ul>
	 * <li>如果是方法，则表示是否发生方法重载。
	 * <li>如果是成员变量（域），则表示是否发生域值覆盖。
	 * </ul>
	 * 
	 * @param otherMember
	 *            目标成员。
	 * @return 如果当前成员被目标成员重载则返回true，否则返回false。
	 */
	abstract boolean isShadowedBy(T otherMember);

	/**
	 * 判断当前成员是否被目标成员之一重载。
	 * <ul>
	 * <li>如果是方法，则表示是否发生方法重载。
	 * <li>如果是成员变量（域），则表示是否发生域值覆盖。
	 * </ul>
	 * 
	 * @param members
	 *            目标成员列表。
	 * @return 如果当前成员被目标成员之一重载则返回true，否则返回false。
	 */
	boolean isShadowedBy(List<T> members) {

		for (T each : members) {
			if (isShadowedBy(each)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否为公共成员。
	 * 
	 * @return 如果是公共成员则返回true，否则返回false。
	 */
	public abstract boolean isPublic();

	/**
	 * 判断是否为静态成员。
	 * 
	 * @return 如果为静态成员则返回true，否则返回false。
	 */
	public abstract boolean isStatic();

	/**
	 * 获取成员名。
	 * 
	 * @return 成员名。
	 */
	public abstract String getName();

	/**
	 * 获取成员类型。
	 * <ul>
	 * <li>如果是成员是成员变量，则为成员变量的类型。
	 * <li>如果是成员是方法，则为方法的返回值。
	 * </ul>
	 * 
	 * @return 成员类型。
	 */
	public abstract Class<?> getType();
}
