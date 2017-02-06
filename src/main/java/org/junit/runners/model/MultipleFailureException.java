package org.junit.runners.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * �����쳣�İ�װ�쳣����
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class MultipleFailureException extends Exception {

	/**
	 * ���кš�
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * Ǳ�ڵ��쳣�б�
	 */
	private final List<Throwable> fErrors;

	/**
	 * ����һ����װĿ���б���쳣���쳣����
	 * 
	 * @param errors
	 *            Ǳ�ڵ��쳣�б�
	 */
	public MultipleFailureException(List<Throwable> errors) {

		fErrors= new ArrayList<Throwable>(errors);
	}

	/**
	 * ��ȡǱ�ڵ��쳣�б�
	 * 
	 * @return Ǳ�ڵ��쳣�б�
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
	 * ����Ŀ���쳣�б�Ϊ�ա�<br>
	 * �������ʧ�ܲ����쳣�б�ֻ��Ψһһ���쳣���׳����쳣�������׳�MultipleFailureException�쳣��
	 * 
	 * @param errors
	 *            Ŀ���쳣�б�
	 * @throws Throwable
	 *             Ŀ���쳣�б�Ϊ�ա�
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
