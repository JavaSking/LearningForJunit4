package org.junit.internal.runners.model;

import java.lang.reflect.InvocationTargetException;

/**
 * 此类将通过反射来调用方法可能抛出的InvocationTargetException异常改为Throwable直接抛出。
 * 
 * @author 注释By JavaSking 2017年2月4日
 */
public abstract class ReflectiveCallable {

    /**
     * 运行。
     * 
     * @return 运行结果。
     * @throws Throwable
     *             异常。
     */
    public Object run() throws Throwable {
        
        try {
            return runReflectiveCall();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    /**
     * 通过反射来调用方法，失败则抛出<code>Throwable</code>。
     * 
     * @return 调用结果。
     * @throws Throwable
     *             方法调用失败。
     */
    protected abstract Object runReflectiveCall() throws Throwable;
}