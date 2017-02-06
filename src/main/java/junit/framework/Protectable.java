package junit.framework;

/**
 * ��������ʱ������������౻�����������������ʱ��<br>
 * �����κ�Error��Exception�������׳�ΪThrowable�����ᵼ�³�����ֹ��
 * 
 * @see TestResult
 * @author ע��By JavaSking 2017��2��5��
 */
public interface Protectable {

    /**
     * ���в��Ա��������������κ�Error��Exception���������Ⲣ�׳�Throwable��
     * 
     * @throws Throwable
     *             �쳣��
     */
    public abstract void protect() throws Throwable;
}