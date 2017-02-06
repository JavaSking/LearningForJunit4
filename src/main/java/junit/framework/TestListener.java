package junit.framework;

/**
 * 测试过程监听器，应该区别测试失败和测试发成错误。<br>
 * <ul>
 * <li>测试失败：测试过程并未异常，但是结果不是所期望的，即断言失败。
 * <li>测试发生错误：测试过程中发生了异常。
 * </ul>
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public interface TestListener {

    /**
     * 监听目标测试异常发生，意味着测试发生错误。
     * 
     * @param test
     *            目标测试。
     * @param e
     *            异常。
     */
    public void addError(Test test, Throwable e);

    /**
     * 监听目标测试断言失败错误发生，这意味着测试失败。
     * 
     * @param test
     *            目标测试。
     * @param e
     *            断言失败错误。
     */
    public void addFailure(Test test, AssertionFailedError e);

    /**
     * 监听目标测试结束。
     * 
     * @param test
     *            目标测试。
     */
    public void endTest(Test test);

    /**
     * 监听目标测试开始。
     * 
     * @param test
     *            目标测试。
     */
    public void startTest(Test test);
}