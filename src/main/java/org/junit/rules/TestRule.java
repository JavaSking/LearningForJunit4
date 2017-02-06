package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * 提供了测试用例执行过程中通用功能的共享的规则。<br>
 * 通过改写源动作并应用规则获取结果动作。
 * <ul>
 * <li>{@link ErrorCollector}: 收集测试方法中出现的错误信息，测试不会中断，如果有错误发生测试结束后会标记失败。</li>
 * <li>{@link ExpectedException}: 提供灵活的异常验证功能。</li>
 * <li>{@link ExternalResource}: 提供外部资源管理能力。</li>
 * <li>{@link TemporaryFolder}: 在JUnit的测试执行前后，创建和删除新的临时目录。</li>
 * <li>{@link TestName}: 在测试方法执行过程中提供获取测试名字的能力。</li>
 * <li>{@link TestWatcher}: 监视测试方法生命周期的各个阶段。</li>
 * <li>{@link Timeout}: 用于测试超时的Rule。</li>
 * <li>{@link Verifier}: 验证测试执行结果的正确性。</li>
 * </ul>
 *
 * @since 4.9
 * @author 注释By JavaSking 2017年2月6日
 */
public interface TestRule {

	/**
	 * 改写源动作并应用规则，返回结果动作。
	 * 
	 * @param base
	 *            源动作。
	 * @param description
	 *            测试内容描述信息。
	 * @return 结果动作。
	 */
	Statement apply(Statement base, Description description);
}
