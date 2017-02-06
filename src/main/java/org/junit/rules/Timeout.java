package org.junit.rules;

import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * ��ʱ���Թ���@Test(timeout=xxx)�÷����ʾ����������������<br>
 * ����Ϊĳ��������в��Է������ó�ʱ�������ڲ��Գ�ʱ��<br>
 * ���ӣ�
 * <pre>
 * public static class HasGlobalTimeout {
 * 
 *  public static String log;
 *
 *  &#064;Rule
 *  public Timeout globalTimeout= new Timeout(20);
 *
 *  &#064;Test
 *  public void testInfiniteLoop1() {
 *      log+= &quot;ran1&quot;;
 *      for (;;) {
 *         }
 *     }
 *
 *  &#064;Test
 *  public void testInfiniteLoop2() {
 *      log+= &quot;ran2&quot;;
 *      for (;;) {
 *         }
 *     }
 * }
 * </pre>
 *
 * @since 4.7
 * 
 * @author ע��By JavaSking
 * 2017��2��6��
 */
public class Timeout implements TestRule {
	/**
	 * ��ʱʱ�������룩��
	 */
    private final int fMillis;

    /**
     * ����һ����ʱ���Թ���
     * 
     * @param millis ��ʱʱ�������룩��
     */
    public Timeout(int millis) {
    	
        fMillis = millis;
    }

    public Statement apply(Statement base, Description description) {
    	
        return new FailOnTimeout(base, fMillis);
    }
}