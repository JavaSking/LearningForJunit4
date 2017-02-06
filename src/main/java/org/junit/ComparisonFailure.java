package org.junit;

/**
 * �ַ����Ƚ϶��Դ��󡣵�{@link org.junit.Assert#assertEquals(Object, Object)
 * assertEquals(String, String)} ʧ��ʱ�׳���
 * 
 * @author ע��By JavaSking
 * @since 4.0 2017��2��6��
 */
public class ComparisonFailure extends AssertionError {
	
	/**
	 * �����ַ���/ʵ���ַ�������󳤶ȣ��������ضϡ�
	 * @see ComparisonCompactor
	 */
	private static final int MAX_CONTEXT_LENGTH = 20;

	/**
	 * ���кš�
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * �����ַ�����
	 */
	private String fExpected;

	/**
	 * ʵ���ַ�����
	 */
	private String fActual;

	/**
	 * ����һ���ַ����Ƚ϶��Դ���
	 * 
	 * @param message ������Ϣ������Ϊnull��
	 * @param expected �����ַ�����
	 * @param actual ʵ���ַ�����
	 */
	public ComparisonFailure(String message, String expected, String actual) {
		
		super(message);
		fExpected = expected;
		fActual = actual;
	}

	/**
	 * Returns "..." in place of common prefix and "..." in place of common suffix
	 * between expected and actual.
	 *
	 * @see Throwable#getMessage()
	 */
	
	@Override
	public String getMessage() {

		return new ComparisonCompactor(MAX_CONTEXT_LENGTH, fExpected, fActual).compact(super.getMessage());
	}

	/**
	 * ��ȡʵ���ַ�����
	 * 
	 * @return ʵ���ַ�����
	 */
	public String getActual() {

		return fActual;
	}

	/**
	 * ��ȡ�����ַ�����
	 * 
	 * @return �����ַ�����
	 */
	public String getExpected() {

		return fExpected;
	}

	/**
	 * 
	 * @author JavaSking
	 * 2017��2��6��
	 */
	private static class ComparisonCompactor {

		/**
		 * ������ͬǰ��׺��
		 */
		private static final String ELLIPSIS = "...";

		private static final String DELTA_END = "]";

		private static final String DELTA_START = "[";

		/**
		 * ��󳤶ȣ�������ضϡ�
		 */
		private int fContextLength;

		/**
		 * �����ַ�����
		 */
		private String fExpected;

		/**
		 * ʵ���ַ�����
		 */
		private String fActual;

		/**
		 * �����ַ�����ʵ���ַ�������ͬǰ׺���ȡ�
		 */
		private int fPrefix;

		/**
		 * �����ַ�����ʵ���ַ�������ͬ��׺���ȡ�
		 */
		private int fSuffix;

		/**
		 * ���췽����
		 * 
		 * @param contextLength ��󳤶ȣ�������ضϡ�
		 * @param expected �����ַ�����
		 * @param actual ʵ���ַ�����
		 */
		public ComparisonCompactor(int contextLength, String expected, String actual) {
			
			fContextLength = contextLength;
			fExpected = expected;
			fActual = actual;
		}

		/**
		 * 
		 * @param message
		 * @return
		 */
		private String compact(String message) {

			if (fExpected == null || fActual == null || areStringsEqual()) {
				return Assert.format(message, fExpected, fActual);
			}

			findCommonPrefix();
			findCommonSuffix();
			String expected = compactString(fExpected);
			String actual = compactString(fActual);
			return Assert.format(message, expected, actual);
		}

		private String compactString(String source) {

			String result = DELTA_START + source.substring(fPrefix, source.length() - fSuffix + 1) + DELTA_END;
			if (fPrefix > 0) {
				result = computeCommonPrefix() + result;
			}
			if (fSuffix > 0) {
				result = result + computeCommonSuffix();
			}
			return result;
		}

		/**
		 * Ѱ�������ַ�����ʵ���ַ�������ͬǰ׺��
		 */
		private void findCommonPrefix() {

			fPrefix = 0;
			int end = Math.min(fExpected.length(), fActual.length());
			for (; fPrefix < end; fPrefix++) {
				if (fExpected.charAt(fPrefix) != fActual.charAt(fPrefix)) {
					break;
				}
			}
		}

		/**
		 * Ѱ�������ַ�����ʵ���ַ�������ͬ��׺��
		 */
		private void findCommonSuffix() {

			int expectedSuffix = fExpected.length() - 1;
			int actualSuffix = fActual.length() - 1;
			for (; actualSuffix >= fPrefix && expectedSuffix >= fPrefix; actualSuffix--, expectedSuffix--) {
				if (fExpected.charAt(expectedSuffix) != fActual.charAt(actualSuffix)) {
					break;
				}
			}
			fSuffix = fExpected.length() - expectedSuffix;
		}

		/**
		 * ��ȡ�����ַ�����ʵ���ַ�������ͬǰ׺�����ء�
		 * 
		 * @return �����ַ�����ʵ���ַ�������ͬǰ׺��
		 */
		private String computeCommonPrefix() {

			return (fPrefix > fContextLength ? ELLIPSIS : "") + fExpected.substring(Math.max(0, fPrefix - fContextLength), fPrefix);
		}

		/**
		 * ��ȡ�����ַ�����ʵ���ַ�������ͬ��׺�����ء�
		 * 
		 * @return �����ַ�����ʵ���ַ�������ͬ��׺��
		 */
		private String computeCommonSuffix() {

			int end = Math.min(fExpected.length() - fSuffix + 1 + fContextLength, fExpected.length());
			return fExpected.substring(fExpected.length() - fSuffix + 1, end) + (fExpected.length() - fSuffix + 1 < fExpected.length() - fContextLength ? ELLIPSIS : "");
		}

		/**
		 * �ж������ַ�����ʵ���ַ����ַ������Ƿ���ͬ��
		 * 
		 * @return ��������ַ�����ʵ���ַ����ַ�������ͬ�򷵻�true�����򷵻�false��
		 */
		private boolean areStringsEqual() {

			return fExpected.equals(fActual);
		}
	}
}