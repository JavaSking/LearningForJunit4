package junit.extensions;

import junit.framework.Test;
import junit.framework.TestResult;

/**
 * 重复执行目标测试的测试包装器。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class RepeatedTest extends TestDecorator {

	/**
	 * 重复次数。
	 */
	private int fTimesRepeat;

	/**
	 * 构造一个重复执行目标测试指定次数的测试包装器。
	 * 
	 * @param test
	 *            目标测试。
	 * @param repeat
	 *            重复执行次数。
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