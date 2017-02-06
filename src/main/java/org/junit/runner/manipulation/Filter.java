package org.junit.runner.manipulation;

import org.junit.runner.Description;
import org.junit.runner.Request;

/**
 * �������������������Ŀ�����ʹ�䲻�����У���̳в�����ʵ�֣����� {@link org.junit.runner.Request}������Ӧ�á�
 * 
 * @since 4.0
 * @author ע��By JavaSking 2017��2��4��
 */
public abstract class Filter {

    /**
     * �������в��ԵĹ�������
     */
    public static Filter ALL = new Filter() {
        @Override
        public boolean shouldRun(Description description) {

            return true;
        }

        @Override
        public String describe() {

            return "all tests";
        }

        @Override
        public void apply(Object child) throws NoTestsRemainException {

        }

        @Override
        public Filter intersect(Filter second) {

            return second;
        }
    };

    /**
     * ��ȡֻ����Ŀ����Է����Ĺ�������
     * 
     * @param desiredDescription
     *            ��������������Ϣ�����ｫ����Ŀ����Է�������Ϣ��
     * @return ֻ����Ŀ����Է����Ĺ�������
     */
    public static Filter matchMethodDescription(final Description desiredDescription) {
        
        return new Filter() {
            @Override
            public boolean shouldRun(Description description) {

                if (description.isTest()) {//ԭ�Ӳ���
                    return desiredDescription.equals(description);
                }
                for (Description each : description.getChildren()) {//���Լ�
                    if (shouldRun(each)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String describe() {

                return String.format("Method %s", desiredDescription.getDisplayName());
            }
        };
    }

    /**
     * �ж�Ŀ������Ƿ�Ӧ�����С�
     * 
     * @param description
     *            ��������������Ϣ��
     * @return ���Ŀ�����Ӧ�������򷵻�true�����򷵻�false��
     */
    public abstract boolean shouldRun(Description description);

    /**
     * ������ǰ��������
     * 
     * @return ������ǰ���������ı���
     */
    public abstract String describe();

    /**
     * ��Ŀ��Ӧ�õ�ǰ��������
     * 
     * @param child
     *            �������˵�Ŀ�ꡣ
     * @throws NoTestsRemainException
     *             �ޱ��������쳣��
     */
    public void apply(Object child) throws NoTestsRemainException {

        if (!(child instanceof Filterable)) {
            return;
        }
        Filterable filterable = (Filterable) child;
        filterable.filter(this);
    }

    /**
     * ���������������㡱���������µĹ��������ù�����������ԭ������������ͬ���ص�Ŀ�ꡣ
     * 
     * @param second
     *            ����һ����������
     * @return �µĹ��������ù�����������ԭ������������ͬ���ص�Ŀ�ꡣ
     */
    public Filter intersect(final Filter second) {

        if (second == this || second == ALL) {
            return this;
        }
        final Filter first = this;
        return new Filter() {
            @Override
            public boolean shouldRun(Description description) {

                return first.shouldRun(description) && second.shouldRun(description);
            }

            @Override
            public String describe() {

                return first.describe() + " and " + second.describe();
            }
        };
    }
}
