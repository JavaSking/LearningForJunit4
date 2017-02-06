package junit.extensions;

import junit.framework.Protectable;
import junit.framework.Test;
import junit.framework.TestResult;

/**
 * ���Զ���İ�װ����������Ӳ���ǰ�ö����ͺ��ö�����
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class TestSetup extends TestDecorator {

	/**
	 * ����һ��������Ӳ���ǰ�ö����ͺ��ö����Ĳ��Զ���İ�װ����
	 * 
	 * @param test
	 *            Ŀ����Զ���
	 */
	public TestSetup(Test test) {

		super(test);
	}

	@Override
	public void run(final TestResult result) {

		Protectable p= new Protectable() {

			public void protect() throws Exception {
				setUp();
				basicRun(result);
				tearDown();
			}
		};
		result.runProtected(this, p);
	}

	/**
	 * ���Ե�ǰ�ö�����
	 * 
	 * @throws Exception
	 *             �쳣��
	 */
	protected void setUp() throws Exception {

	}

	/**
	 * ���Եĺ��ö�����
	 * 
	 * @throws Exception
	 *             �쳣��
	 */
	protected void tearDown() throws Exception {

	}
}