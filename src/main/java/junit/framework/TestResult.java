package junit.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * ���Խ�������ռ�TestSuite�����н����һ������£�һ��TestSuite��Ӧһ��TestResult��<br>
 * ע�⣺Ӧ���������ʧ�ܺͲ��Է��ɴ���<br>
 * <ul>
 * <li>����ʧ�ܣ����Թ��̲�δ�쳣�����ǽ�������������ģ�������ʧ�ܣ����ǿ�Ԥ�ϵ����⡣
 * <li>���Է������󣺲��Թ����з������쳣�����ǲ���Ԥ�ϵ����⡣
 * </ul>
 * ���ģʽ���ռ�����ģʽ��
 * 
 * @see Test
 * @author ע��By JavaSking 2017��2��5��
 */
public class TestResult extends Object {
    /**
     * ʧ�ܵĲ����б�
     */
    protected List<TestFailure> fFailures;

    /**
     * ��������Ĳ����б�
     */
    protected List<TestFailure> fErrors;

    /**
     * ���Լ������б�
     */
    protected List<TestListener> fListeners;

    /**
     * ��������
     */
    protected int fRunTests;

    /**
     * �Ƿ���Ϊ��ֹ��
     */
    private boolean fStop;

    /**
     * ����һ���յĲ��Խ����
     */
    public TestResult() {

        fFailures = new ArrayList<TestFailure>();
        fErrors = new ArrayList<TestFailure>();
        fListeners = new ArrayList<TestListener>();
        fRunTests = 0;
        fStop = false;
    }

    /**
     * �Ǽ�Ŀ����Է������󣬲�֪ͨ����ע��ļ�������
     * 
     * @param test
     *            Ŀ����ԡ�
     * @param e
     *            Ŀ����Է������쳣��
     */
    public synchronized void addError(Test test, Throwable t) {
        fErrors.add(new TestFailure(test, t));
        for (TestListener each : cloneListeners()) {
            each.addError(test, t);
        }
    }

    /**
     * �Ǽ�Ŀ�����ʧ�ܣ���֪ͨ����ע��ļ�������
     * 
     * @param test
     *            Ŀ����ԡ�
     * @param e
     *            Ŀ����Է����Ķ���ʧ�ܴ���
     */
    public synchronized void addFailure(Test test, AssertionFailedError t) {
        fFailures.add(new TestFailure(test, t));
        for (TestListener each : cloneListeners()) {
            each.addFailure(test, t);
        }
    }

    /**
     * ע��Ŀ����Լ�������
     * 
     * @param listener
     *            ��ע��Ĳ��Լ�������
     */
    public synchronized void addListener(TestListener listener) {

        fListeners.add(listener);
    }

    /**
     * �Ƴ�Ŀ����Լ�������
     * 
     * @param listener
     *            ���Ƴ���Ŀ����Լ�������
     */
    public synchronized void removeListener(TestListener listener) {

        fListeners.remove(listener);
    }

    /**
     * ��ȡע��Ĳ��Լ������ĸ�����
     * 
     * @return ע��Ĳ��Լ������ĸ���
     */
    private synchronized List<TestListener> cloneListeners() {

        List<TestListener> result = new ArrayList<TestListener>();
        result.addAll(fListeners);
        return result;
    }

    /**
     * ֪ͨ����ע��Ĳ��Լ�����Ŀ����Խ�����
     * 
     * @param test
     *            Ŀ����ԡ�
     */
    public void endTest(Test test) {

        for (TestListener each : cloneListeners()) {
            each.endTest(test);
        }
    }

    /**
     * ͳ�Ʒ�������Ĳ�������
     * 
     * @return ��������Ĳ�������
     */
    public synchronized int errorCount() {

        return fErrors.size();
    }

    /**
     * ö�ٷ�������Ĳ��ԡ�
     * 
     * @return ��������Ĳ��Ե�ö�١�
     */
    public synchronized Enumeration<TestFailure> errors() {

        return Collections.enumeration(fErrors);
    }

    /**
     * ͳ��ʧ�ܲ�������
     * 
     * @return ʧ�ܲ�������
     */
    public synchronized int failureCount() {

        return fFailures.size();
    }

    /**
     * ö��ʧ�ܵĲ��ԡ�
     * 
     * @return ʧ�ܲ��Ե�ö�١�
     */
    public synchronized Enumeration<TestFailure> failures() {

        return Collections.enumeration(fFailures);
    }

    /**
     * ����Ŀ����ԡ�
     * 
     * @param test
     *            Ŀ����ԡ�
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
     * ͳ�Ʋ�������
     * 
     * @return ��������
     */
    public synchronized int runCount() {

        return fRunTests;
    }

    /**
     * ����Ŀ��������С�
     * 
     * @param test
     *            Ŀ����ԡ�
     * 
     * @param p
     *            ��������ʱ��������
     */
    public void runProtected(final Test test, Protectable p) {

        try {
            p.protect();
        } catch (AssertionFailedError e) {
            addFailure(test, e);// �Ǽǲ���ʧ�ܡ�
        } catch (ThreadDeath e) { // ������ThreadDeath
            throw e;
        } catch (Throwable e) {//�Ǽǲ��Դ���
            addError(test, e);
        }
    }

    /**
     * �жϲ����Ƿ�Ӧ��ֹͣ��
     * 
     * @return �������Ӧ��ֹͣ�򷵻�true�����򷵻�false��
     */
    public synchronized boolean shouldStop() {

        return fStop;
    }

    /**
     * ֪ͨ����ע��Ĳ��Լ�����Ŀ����Կ�ʼ��
     * 
     * @param test
     *            Ŀ����ԡ�
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
     * ��ǲ���Ӧ��ֹͣ��
     */
    public synchronized void stop() {

        fStop = true;
    }

    /**
     * �жϲ����Ƿ�ɹ���
     * 
     * @return ����ɹ��򷵻�true�����򷵻�false��
     */
    public synchronized boolean wasSuccessful() {

        return failureCount() == 0 && errorCount() == 0;
    }
}