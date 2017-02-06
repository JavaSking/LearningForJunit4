package junit.framework;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * “失败”测试的包装，包装了“失败”的测试和造成“失败”的异常。<br>
 * <b>注意：这是失败测试和发生错误的测试的统一包装</b>。
 * 
 * @author 注释By JavaSking
 * @see TestResult 2017年2月5日
 */
public class TestFailure extends Object {
    /**
     * 潜在的测试。
     */
    protected Test fFailedTest;

    /**
     * 抛出的异常。
     */
    protected Throwable fThrownException;

    /**
     * 构造一个新的“失败”测试的包装。
     * 
     * @param failedTest 潜在的测试。
     * @param thrownException 抛出的异常。
     */
    public TestFailure(Test failedTest, Throwable thrownException) {
        
        fFailedTest = failedTest;
        fThrownException = thrownException;
    }

    /**
     * 获取潜在的测试。
     * 
     * @return 潜在的测试。
     */
    public Test failedTest() {
        
        return fFailedTest;
    }

    /**
     * 获取抛出的异常。
     * 
     * @return 抛出的异常。
     */
    public Throwable thrownException() {
        
        return fThrownException;
    }

    /**
     * Returns a short description of the failure.
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(fFailedTest + ": " + fThrownException.getMessage());
        return buffer.toString();
    }

    /**
     * 获取异常的堆栈信息。
     * 
     * @return 异常的堆栈信息。
     */
    public String trace() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        thrownException().printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    /**
     * 获取失败信息。
     * 
     * @return 失败信息。
     */
    public String exceptionMessage() {
        
        return thrownException().getMessage();
    }

    /**
     * 判断测试是否失败而不是发生错误。
     * 
     * @return 如果测试失败而不是发生错误则返回true，否则返回false。
     */
    public boolean isFailure() {
        
        return thrownException() instanceof AssertionFailedError;
    }
}