package junit.framework;

/**
 * 测试的基础接口。<br>
 * 在Junit设计中，一个测试实例可能不包含有效测试动作，而仅仅为了警示一条消息。<br>
 * 例子：详见{@link TestSuite.createTest}方法实现。
 * 
 * @see TestResult
 * @author JavaSking 2017年2月5日
 */
public interface Test {

    /**
     * 统计测试用例数。
     * 
     * @return 测试用例数。
     */
    public abstract int countTestCases();

    /**
     * 运行当前测试，并收集测试结果。
     * 
     * @param result
     *            测试结果。
     */
    public abstract void run(TestResult result);
}