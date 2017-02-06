package org.junit;

/**
 * 字符串比较断言错误。当{@link org.junit.Assert#assertEquals(Object, Object)
 * assertEquals(String, String)} 失败时抛出。
 * 
 * @author 注释By JavaSking
 * @since 4.0 2017年2月6日
 */
public class ComparisonFailure extends AssertionError {
	
	/**
	 * 期望字符串/实际字符串的最大长度，超出将截断。
	 * @see ComparisonCompactor
	 */
	private static final int MAX_CONTEXT_LENGTH = 20;

	/**
	 * 序列号。
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 期望字符串。
	 */
	private String fExpected;

	/**
	 * 实际字符串。
	 */
	private String fActual;

	/**
	 * 构造一个字符串比较断言错误。
	 * 
	 * @param message 错误信息，可能为null。
	 * @param expected 期望字符串。
	 * @param actual 实际字符串。
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
	 * 获取实际字符串。
	 * 
	 * @return 实际字符串。
	 */
	public String getActual() {

		return fActual;
	}

	/**
	 * 获取期望字符串。
	 * 
	 * @return 期望字符串。
	 */
	public String getExpected() {

		return fExpected;
	}

	/**
	 * 
	 * @author JavaSking
	 * 2017年2月6日
	 */
	private static class ComparisonCompactor {

		/**
		 * 代替相同前后缀。
		 */
		private static final String ELLIPSIS = "...";

		private static final String DELTA_END = "]";

		private static final String DELTA_START = "[";

		/**
		 * 最大长度，超出则截断。
		 */
		private int fContextLength;

		/**
		 * 期望字符串。
		 */
		private String fExpected;

		/**
		 * 实际字符串。
		 */
		private String fActual;

		/**
		 * 期望字符串和实际字符串的相同前缀长度。
		 */
		private int fPrefix;

		/**
		 * 期望字符串和实际字符串的相同后缀长度。
		 */
		private int fSuffix;

		/**
		 * 构造方法。
		 * 
		 * @param contextLength 最大长度，超出则截断。
		 * @param expected 期望字符串。
		 * @param actual 实际字符串。
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
		 * 寻找期望字符串和实际字符串的相同前缀。
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
		 * 寻找期望字符串和实际字符串的相同后缀。
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
		 * 获取期望字符串和实际字符串的相同前缀并返回。
		 * 
		 * @return 期望字符串和实际字符串的相同前缀。
		 */
		private String computeCommonPrefix() {

			return (fPrefix > fContextLength ? ELLIPSIS : "") + fExpected.substring(Math.max(0, fPrefix - fContextLength), fPrefix);
		}

		/**
		 * 获取期望字符串和实际字符串的相同后缀并返回。
		 * 
		 * @return 期望字符串和实际字符串的相同后缀。
		 */
		private String computeCommonSuffix() {

			int end = Math.min(fExpected.length() - fSuffix + 1 + fContextLength, fExpected.length());
			return fExpected.substring(fExpected.length() - fSuffix + 1, end) + (fExpected.length() - fSuffix + 1 < fExpected.length() - fContextLength ? ELLIPSIS : "");
		}

		/**
		 * 判断期望字符串和实际字符串字符序列是否相同。
		 * 
		 * @return 如果期望字符串和实际字符串字符序列相同则返回true，否则返回false。
		 */
		private boolean areStringsEqual() {

			return fExpected.equals(fActual);
		}
	}
}