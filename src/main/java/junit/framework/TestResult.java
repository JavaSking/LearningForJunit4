package junit.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 测试结果，是收集TestSuite的运行结果。一般情况下，一个TestSuite对应一个TestResult。<br>
 * 注意：应该区别测试失败和测试发成错误。<br>
 * <ul>
 * <li>测试失败：测试过程并未异常，但是结果不是所期望的，即断言失败，这是可预料的问题。
 * <li>测试发生错误：测试过程中发生了异常，这是不可预料的问题。
 * </ul>
 * 设计模式：收集参数模式。
 * 
 * @see Test
 * @author 注释By JavaSking 2017年2月5日
 */
public class TestResult extends Object {
    /**
     * 失败的测试列表。
     */
    protected List<TestFailure> fFailures;

    /**
     * 发生错误的测试列表。
     */
    protected List<TestFailure> fErrors;

    /**
     * 测试监听器列表。
     */
    protected List<TestListener> fListeners;

    /**
     * 测试数。
     */
    protected int fRunTests;

    /**
     * 是否人为终止。
     */
    private boolean fStop;

    /**
     * 构造一个空的测试结果。
     */
    public TestResult() {

        fFailures = new ArrayList<TestFailure>();
        fErrors = new ArrayList<TestFailure>();
        fListeners = new ArrayList<TestListener>();
        fRunTests = 0;
        fStop = false;
    }

    /**
     * 登记目标测试发生错误，并通知所有注册的监听器。
     * 
     * @param test
     *            目标测试。
     * @param e
     *            目标测试发生的异常。
     */
    public synchronized void addError(Test test, Throwable t) {
        fErrors.add(new TestFailure(test, t));
        for (TestListener each : cloneListeners()) {
            each.addError(test, t);
        }
    }

    /**
     * 登记目标测试失败，并通知所有注册的监听器。
     * 
     * @param test
     *            目标测试。
     * @param e
     *            目标测试发生的断言失败错误。
     */
    public synchronized void addFailure(Test test, AssertionFailedError t) {
        fFailures.add(new TestFailure(test, t));
        for (TestListener each : cloneListeners()) {
            each.addFailure(test, t);
        }
    }

    /**
     * 注册目标测试监听器。
     * 
     * @param listener
     *            待注册的测试监听器。
     */
    public synchronized void addListener(TestListener listener) {

        fListeners.add(listener);
    }

    /**
     * 移除目标测试监听器。
     * 
     * @param listener
     *            待移除的目标测试监听器。
     */
    public synchronized void removeListener(TestListener listener) {

        fListeners.remove(listener);
    }

    /**
     * 获取注册的测试监听器的副本。
     * 
     * @return 注册的测试监听器的副本
     */
    private synchronized List<TestListener> cloneListeners() {

        List<TestListener> result = new ArrayList<TestListener>();
        result.addAll(fListeners);
        return result;
    }

    /**
     * 通知所有注册的测试监听器目标测试结束。
     * 
     * @param test
     *            目标测试。
     */
    public void endTest(Test test) {

        for (TestListener each : cloneListeners()) {
            each.endTest(test);
        }
    }

    /**
     * 统计发生错误的测试数。
     * 
     * @return 发生错误的测试数。
     */
    public synchronized int errorCount() {

        return fErrors.size();
    }

    /**
     * 枚举发生错误的测试。
     * 
     * @return 发生错误的测试的枚举。
     */
    public synchronized Enumeration<TestFailure> errors() {

        return Collections.enumeration(fErrors);
    }

    /**
     * 统计失败测试数。
     * 
     * @return 失败测试数。
     */
    public synchronized int failureCount() {

        return fFailures.size();
    }

    /**
     * 枚举失败的测试。
     * 
     * @return 失败测试的枚举。
     */
    public synchronized Enumeration<TestFailure> failures() {

        return Collections.enumeration(fFailures);
    }

    /**
     * 运行目标测试。
     * 
     * @param test
     *            目标测试。
     */
    protected void run(final TestCase test) {

        startTest(test);
        Protectable p = new Protectable() {

            public void protect() throws Throwable {
                test.runBare();
            }
        };
        runProtected(test, p);
        endTest(test);
    }

    /**
     * 统计测试数。
     * 
     * @return 测试数。
     */
    public synchronized int runCount() {

        return fRunTests;
    }

    /**
     * 保护目标测试运行。
     * 
     * @param test
     *            目标测试。
     * 
     * @param p
     *            测试运行时保护程序。
     */
    public void runProtected(final Test test, Protectable p) {

        try {
            p.protect();
        } catch (AssertionFailedError e) {
            addFailure(test, e);// 登记测试失败。
        } catch (ThreadDeath e) { // 不捕获ThreadDeath
            throw e;
        } catch (Throwable e) {//登记测试错误。
            addError(test, e);
        }
    }

    /**
     * 判断测试是否应该停止。
     * 
     * @return 如果测试应该停止则返回true，否则返回false。
     */
    public synchronized boolean shouldStop() {

        return fStop;
    }

    /**
     * 通知所有注册的测试监听器目标测试开始。
     * 
     * @param test
     *            目标测试。
     */
    public void startTest(Test test) {

        final int count = test.countTestCases();
        synchronized (this) {
            fRunTests += count;
        }
        for (TestListener each : cloneListeners()) {
            each.startTest(test);
        }
    }

    /**
     * 标记测试应该停止。
     */
    public synchronized void stop() {

        fStop = true;
    }

    /**
     * 判断测试是否成功。
     * 
     * @return 如果成功则返回true，否则返回false。
     */
    public synchronized boolean wasSuccessful() {

        return failureCount() == 0 && errorCount() == 0;
    }
}