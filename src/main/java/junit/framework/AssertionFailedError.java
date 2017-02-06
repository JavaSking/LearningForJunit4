package junit.framework;

/**
 * 断言失败错误。出错此错误表明测试过程完成了，但是结果是失败的。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class AssertionFailedError extends AssertionError {

	/**
	 * 序列号。
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * 构造一个空的断言失败错误。
	 */
	public AssertionFailedError() {

	}

	/**
	 * 构造一个指定错误信息的断言失败错误。
	 * 
	 * @param message
	 *            错误信息。
	 */
	public AssertionFailedError(String message) {

		super(defaultString(message));
	}

	private static String defaultString(String message) {

		return message == null ? "" : message;
	}
}