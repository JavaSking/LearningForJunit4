package org.junit.runner.manipulation;

/**
 * �ɹ��˽ӿڡ�
 * 
 * @since 4.0
 * @author ע��By JavaSking 2017��2��4��
 */
public interface Filterable {

    /**
     * Ӧ��Ŀ���������
     * 
     * @param filter
     *            Ŀ���������
     * @throws NoTestsRemainException
     *             �ޱ��������쳣��
     */
    void filter(Filter filter) throws NoTestsRemainException;

}
