package junit.framework;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * ��ʧ�ܡ����Եİ�װ����װ�ˡ�ʧ�ܡ��Ĳ��Ժ���ɡ�ʧ�ܡ����쳣��<br>
 * <b>ע�⣺����ʧ�ܲ��Ժͷ�������Ĳ��Ե�ͳһ��װ</b>��
 * 
 * @author ע��By JavaSking
 * @see TestResult 2017��2��5��
 */
public class TestFailure extends Object {
    /**
     * Ǳ�ڵĲ��ԡ�
     */
    protected Test fFailedTest;

    /**
     * �׳����쳣��
     */
    protected Throwable fThrownException;

    /**
     * ����һ���µġ�ʧ�ܡ����Եİ�װ��
     * 
     * @param failedTest Ǳ�ڵĲ��ԡ�
     * @param thrownException �׳����쳣��
     */
    public TestFailure(Test failedTest, Throwable thrownException) {
        
        fFailedTest = failedTest;
        fThrownException = thrownException;
    }

    /**
     * ��ȡǱ�ڵĲ��ԡ�
     * 
     * @return Ǳ�ڵĲ��ԡ�
     */
    public Test failedTest() {
        
        return fFailedTest;
    }

    /**
     * ��ȡ�׳����쳣��
     * 
     * @return �׳����쳣��
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
     * ��ȡ�쳣�Ķ�ջ��Ϣ��
     * 
     * @return �쳣�Ķ�ջ��Ϣ��
     */
    public String trace() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        thrownException().printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    /**
     * ��ȡʧ����Ϣ��
     * 
     * @return ʧ����Ϣ��
     */
    public String exceptionMessage() {
        
        return thrownException().getMessage();
    }

    /**
     * �жϲ����Ƿ�ʧ�ܶ����Ƿ�������
     * 
     * @return �������ʧ�ܶ����Ƿ��������򷵻�true�����򷵻�false��
     */
    public boolean isFailure() {
        
        return thrownException() instanceof AssertionFailedError;
    }
}