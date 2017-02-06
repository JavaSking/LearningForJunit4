package junit.framework;

/**
 * 测试运行时保护程序，这个类被设计来保护测试运行时。<br>
 * 出现任何Error或Exception都将被抛出为Throwable而不会导致程序终止。
 * 
 * @see TestResult
 * @author 注释By JavaSking 2017年2月5日
 */
public interface Protectable {

    /**
     * 运行测试保护方法，出现任何Error或Exception都将被化解并抛出Throwable。
     * 
     * @throws Throwable
     *             异常。
     */
    public abstract void protect() throws Throwable;
}