package org.junit.internal.runners.model;

import java.util.List;

/**
 * �����쳣�İ�װ�쳣����
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
@Deprecated
public class MultipleFailureException extends org.junit.runners.model.MultipleFailureException {
	
	/**
	 * ���кš�
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * ����һ����װĿ���б���쳣���쳣����
	 * 
	 * @param errors
	 *            Ŀ���쳣�б�
	 */
	public MultipleFailureException(List<Throwable> errors) {

		super(errors);
	}
}
