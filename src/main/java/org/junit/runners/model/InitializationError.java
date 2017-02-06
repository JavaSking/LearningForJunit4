package org.junit.runners.model;

import java.util.Arrays;
import java.util.List;

/**
 * 运行器初始化异常。
 * 
 * @since 4.5
 * @author 注释By JavaSking 2017年2月4日
 */
public class InitializationError extends Exception {
    /**
     * 序列号。
     */
    private static final long serialVersionUID = 1L;

    /**
     * 异常列表。
     */
    private final List<Throwable> fErrors;

    /**
     * 构造一个由目标异常集合造成的运行器初始化异常。
     * 
     * @param errors
     *            异常列表。
     */
    public InitializationError(List<Throwable> errors) {

        this.fErrors = errors;
    }

    /**
     * 构造一个由目标异常造成的运行器初始化异常。
     * 
     * @param error
     *            目标异常。
     */
    public InitializationError(Throwable error) {

        this(Arrays.asList(error));
    }

    /**
     * 构造一个指定异常信息的运行器初始化异常。
     * 
     * @param string
     *            异常信息。
     */
    public InitializationError(String string) {

        this(new Exception(string));
    }

    /**
     * 获取造成运行器初始化异常的异常列表。
     * 
     * @return 造成运行器初始化异常的异常列表。
     */
    public List<Throwable> getCauses() {

        return fErrors;
    }
}
