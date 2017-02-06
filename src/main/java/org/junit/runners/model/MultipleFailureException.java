package org.junit.runners.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 多条异常的包装异常对象。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class MultipleFailureException extends Exception {

	/**
	 * 序列号。
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * 潜在的异常列表。
	 */
	private final List<Throwable> fErrors;

	/**
	 * 构造一个包装目标列表的异常的异常对象。
	 * 
	 * @param errors
	 *            潜在的异常列表。
	 */
	public MultipleFailureException(List<Throwable> errors) {

		fErrors= new ArrayList<Throwable>(errors);
	}

	/**
	 * 获取潜在的异常列表。
	 * 
	 * @return 潜在的异常列表。
	 */
	public List<Throwable> getFailures() {

		return Collections.unmodifiableList(fErrors);
	}

	@Override
	public String getMessage() {
		
		StringBuilder sb= new StringBuilder(String.format("There were %d errors:", fErrors.size()));
		for (Throwable e : fErrors) {
			sb.append(String.format("\n  %s(%s)", e.getClass().getName(), e.getMessage()));
		}
		return sb.toString();
	}

	/**
	 * 断言目标异常列表为空。<br>
	 * 如果断言失败并且异常列表只有唯一一个异常则抛出该异常，否则抛出MultipleFailureException异常。
	 * 
	 * @param errors
	 *            目标异常列表。
	 * @throws Throwable
	 *             目标异常列表不为空。
	 */
	@SuppressWarnings("deprecation")
	public static void assertEmpty(List<Throwable> errors) throws Throwable {
		
		if (errors.isEmpty()) {
			return;
		}
		if (errors.size() == 1) {
			throw errors.get(0);
		}
		throw new org.junit.internal.runners.model.MultipleFailureException(errors);
	}
}
