package org.junit.runner.manipulation;

import java.util.Comparator;

import org.junit.runner.Description;

/**
 * ������������<br>
 * ������ֱ��ʹ�ã�ʹ��{@link org.junit.runner.Request#sortWith(Comparator)}���档
 * 
 * @since 4.0
 * @author ע��By JavaSking 2017��2��4��
 */
public class Sorter implements Comparator<Description> {

    /**
     * ���������������
     */
    public static Sorter NULL = new Sorter(new Comparator<Description>() {
        public int compare(Description o1, Description o2) {
            return 0;
        }
    });

    /**
     * ��������
     */
    private final Comparator<Description> fComparator;

    /**
     * ����һ��������������
     * 
     * @param comparator
     *            ������������
     */
    public Sorter(Comparator<Description> comparator) {

        fComparator = comparator;
    }

    /**
     * ��Ŀ�����Ӧ�õ�ǰ��������������
     * 
     * @param object
     *            ���������������󲻿��������κβ�����
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
