package junit.framework;

/**
 * ���Թ��̼�������Ӧ���������ʧ�ܺͲ��Է��ɴ���<br>
 * <ul>
 * <li>����ʧ�ܣ����Թ��̲�δ�쳣�����ǽ�������������ģ�������ʧ�ܡ�
 * <li>���Է������󣺲��Թ����з������쳣��
 * </ul>
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public interface TestListener {

    /**
     * ����Ŀ������쳣��������ζ�Ų��Է�������
     * 
     * @param test
     *            Ŀ����ԡ�
     * @param e
     *            �쳣��
     */
    public void addError(Test test, Throwable e);

    /**
     * ����Ŀ����Զ���ʧ�ܴ�����������ζ�Ų���ʧ�ܡ�
     * 
     * @param test
     *            Ŀ����ԡ�
     * @param e
     *            ����ʧ�ܴ���
     */
    public void addFailure(Test test, AssertionFailedError e);

    /**
     * ����Ŀ����Խ�����
     * 
     * @param test
     *            Ŀ����ԡ�
     */
    public void endTest(Test test);

    /**
     * ����Ŀ����Կ�ʼ��
     * 
     * @param test
     *            Ŀ����ԡ�
     */
    public void startTest(Test test);
}