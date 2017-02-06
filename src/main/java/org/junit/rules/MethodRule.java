package org.junit.rules;

import org.junit.Rule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * 提供了测试用例执行过程中的规则，使用{@code TestRule}代替以提供类级别的规则。<br>
 * 通过改写源动作并应用规则获取结果动作。
 * 
 * @since 4.7
 * @author 注释By JavaSking 2017年2月6日
 */
public interface MethodRule {

	/**
	 * 改写源动作并返回结果动作。
	 * 
	 * @param base
	 *            源动作。
	 * @param method
	 *            测试方法。
	 * @param target
	 *            调用测试方法的对象。
	 * @return 结果动作。
	 */
	Statement apply(Statement base, FrameworkMethod method, Object target);
}