package junit.framework;

/**
 * ����ʧ�ܴ��󡣳���˴���������Թ�������ˣ����ǽ����ʧ�ܵġ�
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class AssertionFailedError extends AssertionError {

	/**
	 * ���кš�
	 */
	private static final long serialVersionUID= 1L;

	/**
	 * ����һ���յĶ���ʧ�ܴ���
	 */
	public AssertionFailedError() {

	}

	/**
	 * ����һ��ָ��������Ϣ�Ķ���ʧ�ܴ���
	 * 
	 * @param message
	 *            ������Ϣ��
	 */
	public AssertionFailedError(String message) {

		super(defaultString(message));
	}

	private static String defaultString(String message) {

		return message == null ? "" : message;
	}
}