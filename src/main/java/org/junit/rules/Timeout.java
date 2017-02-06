package org.junit.rules;

import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * 超时测试规则，@Test(timeout=xxx)用法本质就是利用了这个规则。<br>
 * 负责为某个类的所有测试方法设置超时属性用于测试超时。<br>
 * 例子：
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
 * @author 注释By JavaSking
 * 2017年2月6日
 */
public class Timeout implements TestRule {
	/**
	 * 限时时长（毫秒）。
	 */
    private final int fMillis;

    /**
     * 构造一个超时测试规则。
     * 
     * @param millis 限时时长（毫秒）。
     */
    public Timeout(int millis) {
    	
        fMillis = millis;
    }

    public Statement apply(Statement base, Description description) {
    	
        return new FailOnTimeout(base, fMillis);
    }
}