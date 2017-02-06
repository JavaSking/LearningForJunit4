package org.junit.internal.runners.model;

import java.lang.reflect.InvocationTargetException;

/**
 * ���ཫͨ�����������÷��������׳���InvocationTargetException�쳣��ΪThrowableֱ���׳���
 * 
 * @author ע��By JavaSking 2017��2��4��
 */
public abstract class ReflectiveCallable {

    /**
     * ���С�
     * 
     * @return ���н����
     * @throws Throwable
     *             �쳣��
     */
    public Object run() throws Throwable {
        
        try {
            return runReflectiveCall();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    /**
     * ͨ�����������÷�����ʧ�����׳�<code>Throwable</code>��
     * 
     * @return ���ý����
     * @throws Throwable
     *             ��������ʧ�ܡ�
     */
    protected abstract Object runReflectiveCall() throws Throwable;
}