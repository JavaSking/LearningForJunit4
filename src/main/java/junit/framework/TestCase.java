package junit.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A test case defines the fixture to run multiple tests. To define a test case<br/>
 * <ol>
 *   <li>implement a subclass of <code>TestCase</code></li>
 *   <li>define instance variables that store the state of the fixture</li>
 *   <li>initialize the fixture state by overriding {@link #setUp()}</li>
 *   <li>clean-up after a test by overriding {@link #tearDown()}.</li>
 * </ol>
 * Each test runs in its own fixture so there
 * can be no side effects among test runs.
 * Here is an example:
 * <pre>
 * public class MathTest extends TestCase {
 *    protected double fValue1;
 *    protected double fValue2;
 *
 *    protected void setUp() {
 *       fValue1= 2.0;
 *       fValue2= 3.0;
 *    }
 * }
 * </pre>
 *
 * For each test implement a method which interacts
 * with the fixture. Verify the expected results with assertions specified
 * by calling {@link junit.framework.Assert#assertTrue(String, boolean)} with a boolean.
 * <pre>
 *    public void testAdd() {
 *       double result= fValue1 + fValue2;
 *       assertTrue(result == 5.0);
 *    }
 * </pre>
 *
 * Once the methods are defined you can run them. The framework supports
 * both a static type safe and more dynamic way to run a test.
 * In the static way you override the runTest method and define the method to
 * be invoked. A convenient way to do so is with an anonymous inner class.
 * <pre>
 * TestCase test= new MathTest("add") {
 *    public void runTest() {
 *       testAdd();
 *    }
 * };
 * test.run();
 * </pre>
 * The dynamic way uses reflection to implement {@link #runTest()}. It dynamically finds
 * and invokes a method.
 * In this case the name of the test case has to correspond to the test method
 * to be run.
 * <pre>
 * TestCase test= new MathTest("testAdd");
 * test.run();
 * </pre>
 *
 * The tests to be run can be collected into a TestSuite. JUnit provides
 * different <i>test runners</i> which can run a test suite and collect the results.
 * A test runner either expects a static method <code>suite</code> as the entry
 * point to get a test to run or it will extract the suite automatically.
 * <pre>
 * public static Test suite() {
 *    suite.addTest(new MathTest("testAdd"));
 *    suite.addTest(new MathTest("testDivideByZero"));
 *    return suite;
 * }
 * </pre>
 *
 * @see TestResult
 * @see TestSuite
 */

/**
 * 测试用例。
 * 
 * @author JavaSking
 * 2017年2月5日
 */
public abstract class TestCase extends Assert implements Test {

	/**
	 * 测试用例名。
	 */
    private String fName;

    /**
     * 构造一个测试用例，名称为空。
     */
    public TestCase() {
    	
        fName = null;
    }

    /**
     * 构造一个指定名称的测试用例。
     * 
     * @param name 测试用例名。
     */
    public TestCase(String name) {
    	
        fName = name;
    }

    /**
     * @return 1。
     */
    public int countTestCases() {
    	
        return 1;
    }

    /**
     * 新建一个测试结果。
     * 
     * @return 新建的测试结果。
     */
    protected TestResult createResult() {
    	
        return new TestResult();
    }

    /**
     * 运行当前测试用例，返回测试结果。
     * 
     * @return 测试结果。
     */
    public TestResult run() {
    	
        TestResult result = createResult();
        run(result);
        return result;
    }

    public void run(TestResult result) {
    	
        result.run(this);
    }

    /**
     * 执行基本的测试动作。
     * 
     * @throws Throwable 异常。
     */
    public void runBare() throws Throwable {
    	
        Throwable exception = null;
        setUp();//测试前置动作
        try {
            runTest();
        } catch (Throwable running) {
            exception = running;
        } finally {
            try {
                tearDown();//测试后置动作
            } catch (Throwable tearingDown) {
                if (exception == null) exception = tearingDown;
            }
        }
        if (exception != null) throw exception;
    }

    /**
     * 运行测试。
     * 
     * @throws Throwable 异常。
     */
    protected void runTest() throws Throwable {
    	
    	/* 一些虚拟器在调用getMethod(null,null)时发生奔溃。*/
        assertNotNull("TestCase.fName cannot be null", fName); 
        Method runMethod = null;
        try {
            runMethod = getClass().getMethod(fName, (Class[]) null);
        } catch (NoSuchMethodException e) {
            fail("Method \"" + fName + "\" not found");
        }
        if (!Modifier.isPublic(runMethod.getModifiers())) {
            fail("Method \"" + fName + "\" should be public");
        }
        try {
            runMethod.invoke(this);
        } catch (InvocationTargetException e) {
            e.fillInStackTrace();
            throw e.getTargetException();
        } catch (IllegalAccessException e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    /**
     * Asserts that a condition is true. If it isn't it throws
     * an AssertionFailedError with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertTrue(String message, boolean condition) {
    	
        Assert.assertTrue(message, condition);
    }

    /**
     * Asserts that a condition is true. If it isn't it throws
     * an AssertionFailedError.
     */
    @SuppressWarnings("deprecation")
    public static void assertTrue(boolean condition) {
    	
        Assert.assertTrue(condition);
    }

    /**
     * Asserts that a condition is false. If it isn't it throws
     * an AssertionFailedError with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertFalse(String message, boolean condition) {
    	
        Assert.assertFalse(message, condition);
    }

    /**
     * Asserts that a condition is false. If it isn't it throws
     * an AssertionFailedError.
     */
    @SuppressWarnings("deprecation")
    public static void assertFalse(boolean condition) {
    	
        Assert.assertFalse(condition);
    }

    /**
     * Fails a test with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void fail(String message) {
    	
        Assert.fail(message);
    }

    /**
     * Fails a test with no message.
     */
    @SuppressWarnings("deprecation")
    public static void fail() {
    	
        Assert.fail();
    }

    /**
     * Asserts that two objects are equal. If they are not
     * an AssertionFailedError is thrown with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(String message, Object expected, Object actual) {
    	
        Assert.assertEquals(message, expected, actual);
    }

    /**
     * Asserts that two objects are equal. If they are not
     * an AssertionFailedError is thrown.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(Object expected, Object actual) {
    	
        Assert.assertEquals(expected, actual);
    }

    /**
     * Asserts that two Strings are equal.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(String message, String expected, String actual) {
    	
        Assert.assertEquals(message, expected, actual);
    }

    /**
     * Asserts that two Strings are equal.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(String expected, String actual) {
    	
        Assert.assertEquals(expected, actual);
    }

    /**
     * Asserts that two doubles are equal concerning a delta.  If they are not
     * an AssertionFailedError is thrown with the given message.  If the expected
     * value is infinity then the delta value is ignored.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(String message, double expected, double actual, double delta) {
    	
        Assert.assertEquals(message, expected, actual, delta);
    }

    /**
     * Asserts that two doubles are equal concerning a delta. If the expected
     * value is infinity then the delta value is ignored.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(double expected, double actual, double delta) {
    	
        Assert.assertEquals(expected, actual, delta);
    }

    /**
     * Asserts that two floats are equal concerning a positive delta. If they
     * are not an AssertionFailedError is thrown with the given message. If the
     * expected value is infinity then the delta value is ignored.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(String message, float expected, float actual, float delta) {
    	
        Assert.assertEquals(message, expected, actual, delta);
    }

    /**
     * Asserts that two floats are equal concerning a delta. If the expected
     * value is infinity then the delta value is ignored.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(float expected, float actual, float delta) {
    	
        Assert.assertEquals(expected, actual, delta);
    }

    /**
     * Asserts that two longs are equal. If they are not
     * an AssertionFailedError is thrown with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(String message, long expected, long actual) {
    	
        Assert.assertEquals(message, expected, actual);
    }

    /**
     * Asserts that two longs are equal.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(long expected, long actual) {
    	
        Assert.assertEquals(expected, actual);
    }

    /**
     * Asserts that two booleans are equal. If they are not
     * an AssertionFailedError is thrown with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(String message, boolean expected, boolean actual) {
    	
        Assert.assertEquals(message, expected, actual);
    }

    /**
     * Asserts that two booleans are equal.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(boolean expected, boolean actual) {
    	
        Assert.assertEquals(expected, actual);
    }

    /**
     * Asserts that two bytes are equal. If they are not
     * an AssertionFailedError is thrown with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(String message, byte expected, byte actual) {
    	
        Assert.assertEquals(message, expected, actual);
    }

    /**
     * Asserts that two bytes are equal.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(byte expected, byte actual) {
    	
        Assert.assertEquals(expected, actual);
    }

    /**
     * Asserts that two chars are equal. If they are not
     * an AssertionFailedError is thrown with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(String message, char expected, char actual) {
    	
        Assert.assertEquals(message, expected, actual);
    }

    /**
     * Asserts that two chars are equal.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(char expected, char actual) {
    	
        Assert.assertEquals(expected, actual);
    }

    /**
     * Asserts that two shorts are equal. If they are not
     * an AssertionFailedError is thrown with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(String message, short expected, short actual) {
    	
        Assert.assertEquals(message, expected, actual);
    }

    /**
     * Asserts that two shorts are equal.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(short expected, short actual) {
    	
        Assert.assertEquals(expected, actual);
    }

    /**
     * Asserts that two ints are equal. If they are not
     * an AssertionFailedError is thrown with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(String message, int expected, int actual) {
    	
        Assert.assertEquals(message, expected, actual);
    }

    /**
     * Asserts that two ints are equal.
     */
    @SuppressWarnings("deprecation")
    public static void assertEquals(int expected, int actual) {
    	
        Assert.assertEquals(expected, actual);
    }

    /**
     * Asserts that an object isn't null.
     */
    @SuppressWarnings("deprecation")
    public static void assertNotNull(Object object) {
    	
        Assert.assertNotNull(object);
    }

    /**
     * Asserts that an object isn't null. If it is
     * an AssertionFailedError is thrown with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertNotNull(String message, Object object) {
    	
        Assert.assertNotNull(message, object);
    }

    /**
     * Asserts that an object is null. If it isn't an {@link AssertionError} is
     * thrown.
     * Message contains: Expected: <null> but was: object
     *
     * @param object Object to check or <code>null</code>
     */
    @SuppressWarnings("deprecation")
    public static void assertNull(Object object) {
    	
        Assert.assertNull(object);
    }

    /**
     * Asserts that an object is null.  If it is not
     * an AssertionFailedError is thrown with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertNull(String message, Object object) {
    	
        Assert.assertNull(message, object);
    }

    /**
     * Asserts that two objects refer to the same object. If they are not
     * an AssertionFailedError is thrown with the given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertSame(String message, Object expected, Object actual) {
    	
        Assert.assertSame(message, expected, actual);
    }

    /**
     * Asserts that two objects refer to the same object. If they are not
     * the same an AssertionFailedError is thrown.
     */
    @SuppressWarnings("deprecation")
    public static void assertSame(Object expected, Object actual) {
    	
        Assert.assertSame(expected, actual);
    }

    /**
     * Asserts that two objects do not refer to the same object. If they do
     * refer to the same object an AssertionFailedError is thrown with the
     * given message.
     */
    @SuppressWarnings("deprecation")
    public static void assertNotSame(String message, Object expected, Object actual) {
    	
        Assert.assertNotSame(message, expected, actual);
    }

    /**
     * Asserts that two objects do not refer to the same object. If they do
     * refer to the same object an AssertionFailedError is thrown.
     */
    @SuppressWarnings("deprecation")
    public static void assertNotSame(Object expected, Object actual) {
    	
        Assert.assertNotSame(expected, actual);
    }

    @SuppressWarnings("deprecation")
    public static void failSame(String message) {
    	
        Assert.failSame(message);
    }

    @SuppressWarnings("deprecation")
    public static void failNotSame(String message, Object expected, Object actual) {
    	
        Assert.failNotSame(message, expected, actual);
    }

    @SuppressWarnings("deprecation")
    public static void failNotEquals(String message, Object expected, Object actual) {
    	
        Assert.failNotEquals(message, expected, actual);
    }

    @SuppressWarnings("deprecation")
    public static String format(String message, Object expected, Object actual) {
    	
        return Assert.format(message, expected, actual);
    }

    /**
     * 测试前置动作，在测试开始前执行。
     * 
     * @throws Exception 异常。
     */
    protected void setUp() throws Exception {
    	
    }

    /**
     * 测试后置动作，在测试结束后执行。
     * 
     * @throws Exception 异常。
     */
    protected void tearDown() throws Exception {
    	
    }

    /**
     * 描述测试用例：用例名（类名）。
     * @return 描述测试用例：用例名（类名）。
     */
    @Override
    public String toString() {
    	
        return getName() + "(" + getClass().getName() + ")";
    }

    /**
     * 获取测试用例名。
     * 
     * @return 测试用例名。
     */
    public String getName() {
    	
        return fName;
    }

    /**
     * 设置测试用例名。
     * 
     * @param name 待设置的测试用例名。
     */
    public void setName(String name) {
    	
        fName = name;
    }
}
