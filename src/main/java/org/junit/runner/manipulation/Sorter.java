package org.junit.runner.manipulation;

import java.util.Comparator;

import org.junit.runner.Description;

/**
 * 测试排序器。<br>
 * 不建议直接使用，使用{@link org.junit.runner.Request#sortWith(Comparator)}代替。
 * 
 * @since 4.0
 * @author 注释By JavaSking 2017年2月4日
 */
public class Sorter implements Comparator<Description> {

    /**
     * 乱序测试排序器。
     */
    public static Sorter NULL = new Sorter(new Comparator<Description>() {
        public int compare(Description o1, Description o2) {
            return 0;
        }
    });

    /**
     * 排序器。
     */
    private final Comparator<Description> fComparator;

    /**
     * 构造一个测试排序器。
     * 
     * @param comparator
     *            测试排序器。
     */
    public Sorter(Comparator<Description> comparator) {

        fComparator = comparator;
    }

    /**
     * 对目标对象应用当前排序器进行排序。
     * 
     * @param object
     *            待排序对象，如果对象不可排序不做任何操作。
     */
    public void apply(Object object) {

        if (object instanceof Sortable) {
            Sortable sortable = (Sortable) object;
            sortable.sort(this);
        }
    }

    public int compare(Description o1, Description o2) {

        return fComparator.compare(o1, o2);
    }
}
