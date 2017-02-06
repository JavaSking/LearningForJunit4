package org.junit.runners.model;

import java.util.Arrays;
import java.util.List;

/**
 * ��������ʼ���쳣��
 * 
 * @since 4.5
 * @author ע��By JavaSking 2017��2��4��
 */
public class InitializationError extends Exception {
    /**
     * ���кš�
     */
    private static final long serialVersionUID = 1L;

    /**
     * �쳣�б�
     */
    private final List<Throwable> fErrors;

    /**
     * ����һ����Ŀ���쳣������ɵ���������ʼ���쳣��
     * 
     * @param errors
     *            �쳣�б�
     */
    public InitializationError(List<Throwable> errors) {

        this.fErrors = errors;
    }

    /**
     * ����һ����Ŀ���쳣��ɵ���������ʼ���쳣��
     * 
     * @param error
     *            Ŀ���쳣��
     */
    public InitializationError(Throwable error) {

        this(Arrays.asList(error));
    }

    /**
     * ����һ��ָ���쳣��Ϣ����������ʼ���쳣��
     * 
     * @param string
     *            �쳣��Ϣ��
     */
    public InitializationError(String string) {

        this(new Exception(string));
    }

    /**
     * ��ȡ�����������ʼ���쳣���쳣�б�
     * 
     * @return �����������ʼ���쳣���쳣�б�
     */
    public List<Throwable> getCauses() {

        return fErrors;
    }
}
