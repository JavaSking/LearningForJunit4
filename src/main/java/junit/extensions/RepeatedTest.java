package junit.extensions;

import junit.framework.Test;
import junit.framework.TestResult;

/**
 * �ظ�ִ��Ŀ����ԵĲ��԰�װ����
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class RepeatedTest extends TestDecorator {

	/**
	 * �ظ�������
	 */
	private int fTimesRepeat;

	/**
	 * ����һ���ظ�ִ��Ŀ�����ָ�������Ĳ��԰�װ����
	 * 
	 * @param test
	 *            Ŀ����ԡ�
	 * @param repeat
	 *            �ظ�ִ�д�����
	 */
	public RepeatedTest(Test test, int repeat) {
		super(test);
		if (repeat < 0) {
			throw new IllegalArgumentException("Repetition count must be >= 0");
		}
		fTimesRepeat= repeat;
	}

	@Override
	public int countTestCases() {

		return super.countTestCases() * fTimesRepeat;
	}

	@Override
	public void run(TestResult result) {

		for (int i= 0; i < fTimesRepeat; i++) {
			if (result.shouldStop()) {
				break;
			}
			super.run(result);
		}
	}

	@Override
	public String toString() {

		return super.toString() + "(repeated)";
	}
}