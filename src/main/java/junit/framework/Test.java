package junit.framework;

/**
 * ���ԵĻ����ӿڡ�<br>
 * ��Junit����У�һ������ʵ�����ܲ�������Ч���Զ�����������Ϊ�˾�ʾһ����Ϣ��<br>
 * ���ӣ����{@link TestSuite.createTest}����ʵ�֡�
 * 
 * @see TestResult
 * @author JavaSking 2017��2��5��
 */
public interface Test {

    /**
     * ͳ�Ʋ�����������
     * 
     * @return ������������
     */
    public abstract int countTestCases();

    /**
     * ���е�ǰ���ԣ����ռ����Խ����
     * 
     * @param result
     *            ���Խ����
     */
    public abstract void run(TestResult result);
}