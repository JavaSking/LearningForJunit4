package org.junit.runner.manipulation;

import org.junit.runner.Description;
import org.junit.runner.Request;

/**
 * 过滤器。如果你想拦截目标测试使其不被运行，请继承并给出实现，并在 {@link org.junit.runner.Request}对象上应用。
 * 
 * @since 4.0
 * @author 注释By JavaSking 2017年2月4日
 */
public abstract class Filter {

    /**
     * 放行所有测试的过滤器。
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
     * 获取只放行目标测试方法的过滤器。
     * 
     * @param desiredDescription
     *            测试内容描述信息，这里将给出目标测试方法的信息。
     * @return 只放行目标测试方法的过滤器。
     */
    public static Filter matchMethodDescription(final Description desiredDescription) {
        
        return new Filter() {
            @Override
            public boolean shouldRun(Description description) {

                if (description.isTest()) {//原子测试
                    return desiredDescription.equals(description);
                }
                for (Description each : description.getChildren()) {//测试集
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
     * 判断目标测试是否应该运行。
     * 
     * @param description
     *            测试内容描述信息。
     * @return 如果目标测试应该运行则返回true，否则返回false。
     */
    public abstract boolean shouldRun(Description description);

    /**
     * 描述当前过滤器。
     * 
     * @return 描述当前过滤器的文本。
     */
    public abstract String describe();

    /**
     * 对目标应用当前过滤器。
     * 
     * @param child
     *            将被过滤的目标。
     * @throws NoTestsRemainException
     *             无保留测试异常。
     */
    public void apply(Object child) throws NoTestsRemainException {

        if (!(child instanceof Filterable)) {
            return;
        }
        Filterable filterable = (Filterable) child;
        filterable.filter(this);
    }

    /**
     * 过滤器“交集运算”，并返回新的过滤器。该过滤器仅拦截原来两过滤器共同拦截的目标。
     * 
     * @param second
     *            另外一个过滤器。
     * @return 新的过滤器。该过滤器仅拦截原来两过滤器共同拦截的目标。
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
