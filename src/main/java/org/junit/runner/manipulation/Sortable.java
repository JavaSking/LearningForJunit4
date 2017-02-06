package org.junit.runner.manipulation;

/**
 * 可排序接口。
 * 
 * @since 4.0
 * @author 注释By JavaSking 2017年2月4日
 */
public interface Sortable {

    /**
     * 应用目标排序器进行排序。
     * 
     * @param sorter
     *            排序器。
     */
    public void sort(Sorter sorter);

}
