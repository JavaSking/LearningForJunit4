package org.junit.runner.manipulation;

/**
 * 可过滤接口。
 * 
 * @since 4.0
 * @author 注释By JavaSking 2017年2月4日
 */
public interface Filterable {

    /**
     * 应用目标过滤器。
     * 
     * @param filter
     *            目标过滤器。
     * @throws NoTestsRemainException
     *             无保留测试异常。
     */
    void filter(Filter filter) throws NoTestsRemainException;

}
