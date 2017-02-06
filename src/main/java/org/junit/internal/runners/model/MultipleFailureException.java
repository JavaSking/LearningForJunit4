package org.junit.internal.runners.model;

import java.util.List;

/**
 * 多条异常的包装异常对象。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
@Deprecated
public class MultipleFailureException extends org.junit.runners.model.MultipleFailureException {
	
	/**
	 * 序列号。
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * 构造一个包装目标列表的异常的异常对象。
	 * 
	 * @param errors
	 *            目标异常列表。
	 */
	public MultipleFailureException(List<Throwable> errors) {

		super(errors);
	}
}
