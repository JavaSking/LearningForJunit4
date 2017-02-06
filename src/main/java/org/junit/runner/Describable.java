package org.junit.runner;

/**
 * 自描述性接口。
 * 
 * @since 4.5
 * @author 注释By JavaSking 2017年2月4日
 */
public interface Describable {
    /**
     * 获取测试内容描述信息。
     * 
     * @return 测试内容描述信息。
     */
    public abstract Description getDescription();
}