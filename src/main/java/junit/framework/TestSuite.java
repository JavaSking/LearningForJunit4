package junit.framework;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.junit.internal.MethodSorter;

/**
 * 测试组，可以嵌套多个测试组或测试用例。<br>
 * 例子：
 * 
 * <pre>
 * TestSuite suite = new TestSuite();
 * suite.addTest(new MathTest("testAdd")); // 1、添加测试用例
 * suite.addTest(new MathTest("testDivideByZero"));
 * </pre>
 * 
 * <pre>
 * TestSuite suite = new TestSuite(MathTest.class);// 2、自动添加目标测试类的所有测试用例
 * </pre>
 * 
 * <pre>
 * Class[] testClasses = { MathTest.class, AnotherTest.class };
 * 
 * TestSuite suite = new TestSuite(testClasses);// 3、自动添加多个测试类的全部测试用例
 * </pre>
 * 
 * @see Test
 * @author 注释By JavaSking 2017年2月5日
 */
public class TestSuite implements Test {

    /**
     * 创建一个测试用例。<br>
     * 这个方法验证了运行每个测试方法前，都会创建新的类实例。
     * 
     * @param theClass
     *            测试类。
     * @param name
     *            测试用例名（测试方法名）。
     * @return 新建的测试用例。
     */
    static public Test createTest(Class<?> theClass, String name) {

        Constructor<?> constructor;
        try {
            constructor = getTestConstructor(theClass);
        } catch (NoSuchMethodException e) {
            return warning("Class " + theClass.getName() + " has no public constructor TestCase(String name) or TestCase()");
        }
        Object test;
        try {
            if (constructor.getParameterTypes().length == 0) {
                test = constructor.newInstance(new Object[0]);
                if (test instanceof TestCase) {
                    ((TestCase) test).setName(name);
                }
            } else {
                test = constructor.newInstance(new Object[] { name });
            }
        } catch (InstantiationException e) {
            return (warning("Cannot instantiate test case: " + name + " (" + exceptionToString(e) + ")"));
        } catch (InvocationTargetException e) {
            return (warning("Exception in constructor: " + name + " (" + exceptionToString(e.getTargetException()) + ")"));
        } catch (IllegalAccessException e) {
            return (warning("Cannot access test case: " + name + " (" + exceptionToString(e) + ")"));
        }
        return (Test) test;
    }

    /**
     * 尝试获取目标测试类的构造方法，获取失败则抛出NoSuchMethodException异常。<br>
     * 注意：<b>测试类的构造方法只能为XXX(String）或者XXX()。</b>
     * 
     * @param theClass
     *            目标测试类。
     * @return 目标测试类的构造方法。
     * @throws NoSuchMethodException
     *             方法不存在异常。
     */
    public static Constructor<?> getTestConstructor(Class<?> theClass) throws NoSuchMethodException {
        
        try {
            return theClass.getConstructor(String.class);
        } catch (NoSuchMethodException e) {

        }
        return theClass.getConstructor(new Class[0]);
    }

    /**
     * 将消息警示动作包装进一个测试并返回，该测试仅仅包含消息警示的动作。
     * 
     * @param message
     *            需警示的消息。
     * @return 仅仅包含警示目标消息动作的测试。
     */
    public static Test warning(final String message) {
        
        return new TestCase("warning") {
            @Override
            protected void runTest() {
                fail(message);
            }
        };
    }

    /**
     * 获取目标异常的堆栈信息。
     * 
     * @param t 目标异常。
     * 
     * @return
     * 			目标异常的堆栈信息。
     */
    private static String exceptionToString(Throwable t) {
    	
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        return stringWriter.toString();
    }
    /**
     * 测试组名称。
     */
    private String fName;

    /**
     * 测试集，元素可能为测试组或单一的测试用例。
     */
    private Vector<Test> fTests = new Vector<Test>(10);

    /**
     * 构造一个空的测试组。
     */
    public TestSuite() {

    }

    /**
     * 构造一个包含目标测试类全部测试用例的测试组。<br>
     * 所有test开头的方法都当作测试用例被添加到测试组。
     * 
     * @param theClass
     *            目标测试类。
     */
    public TestSuite(final Class<?> theClass) {

        addTestsFromTestCase(theClass);
    }

    /**
     * 添加目标测试类的所有测试用例到测试组。<br>
     * 所有test开头的方法都当作测试案例被添加到测试组。<br>
     * 方法的添加从子类向超类回溯，相同方法只被添加一次。
     * 
     * @param theClass
     *            目标测试类。
     */
    private void addTestsFromTestCase(final Class<?> theClass) {

        fName = theClass.getName();
        try {
            getTestConstructor(theClass);
        } catch (NoSuchMethodException e) {
            addTest(warning("Class " + theClass.getName() + " has no public constructor TestCase(String name) or TestCase()"));
            return;
        }

        if (!Modifier.isPublic(theClass.getModifiers())) {
            addTest(warning("Class " + theClass.getName() + " is not public"));
            return;
        }

        Class<?> superClass = theClass;
        List<String> names = new ArrayList<String>();
        while (Test.class.isAssignableFrom(superClass)) {
            for (Method each : MethodSorter.getDeclaredMethods(superClass)) {
                addTestMethod(each, names, theClass);
            }
            superClass = superClass.getSuperclass();
        }
        if (fTests.size() == 0) {
            addTest(warning("No tests found in " + theClass.getName()));
        }
    }

    /**
     * 构造一个包含目标测试类全部测试用例的指定名称的测试组。
     * 
     * @see TestSuite#TestSuite(Class)
     * @param theClass
     *            目标测试类。
     * @param name
     *            测试组名。
     */
    public TestSuite(Class<? extends TestCase> theClass, String name) {

        this(theClass);
        setName(name);
    }

    /**
     * 构造一个指定名称的空测试组。
     * 
     * @param name
     *            测试组名。
     */
    public TestSuite(String name) {

        setName(name);
    }

    /**
     * 构造一个包含目标测试类集全部测试用例的测试组。
     * 
     * @param classes
     *            目标测试类集。
     */
    public TestSuite(Class<?>... classes) {

        for (Class<?> each : classes) {
            addTest(testCaseForClass(each));
        }
    }

    /**
     * 创建一个包含目标测试类全部测试用例的测试组。
     * 
     * @param each
     *            目标测试类。
     * @return 包含目标测试类全部测试用例的测试组。
     */
    private Test testCaseForClass(Class<?> each) {

        if (TestCase.class.isAssignableFrom(each)) {
            return new TestSuite(each.asSubclass(TestCase.class));
        } else {
            return warning(each.getCanonicalName() + " does not extend TestCase");
        }
    }

    /**
     * 构造一个包含目标测试类集全部测试用例的指定名称的测试组。
     * 
     * @see TestSuite#TestSuite(Class[])
     * @param classes
     *            目标测试类集。
     * @param name
     *            测试组名。
     */
    public TestSuite(Class<? extends TestCase>[] classes, String name) {

        this(classes);
        setName(name);
    }

    /**
     * 添加目标测试（用例/组）到当前测试组中。
     * 
     * @param test
     *            待添加的目标测试（用例/组）。
     */
    public void addTest(Test test) {

        fTests.add(test);
    }

    /**
     * 添加目标测试类的所有测试用例到当前测试组。
     * 
     * @param testClass
     *            目标测试类（TestCase类型）。
     */
    public void addTestSuite(Class<? extends TestCase> testClass) {

        addTest(new TestSuite(testClass));
    }

    /**
     * 统计当前测试组包含的测试用例数。
     * 
     * @return 当前测试组包含的测试用例数。
     */
    public int countTestCases() {

        int count = 0;
        for (Test each : fTests) {
            count += each.countTestCases();
        }
        return count;
    }

    /**
     * 获取测试组名。
     * 
     * @return 测试组名，可能返回null。
     */
    public String getName() {

        return fName;
    }

    /**
     * 运行测试组，并收集测试结果，中途可能终止。
     */
    public void run(TestResult result) {

        for (Test each : fTests) {
            if (result.shouldStop()) {
                break;
            }
            runTest(each, result);
        }
    }

    /**
     * 运行目标测试并收集测试结果。
     * 
     * @param test
     *            目标测试。
     * @param result
     *            测试结果。
     */
    public void runTest(Test test, TestResult result) {

        test.run(result);
    }

    /**
     * 设置测试组名。
     * 
     * @param name
     *            待设置的测试组名。
     */
    public void setName(String name) {

        fName = name;
    }

    /**
     * 获取目标位置的测试组元素。
     * 
     * @param index
     *            位置索引。
     * @return 目标位置的测试组元素。
     */
    public Test testAt(int index) {

        return fTests.get(index);
    }

    /**
     * 统计测试组元素个数。
     * 
     * @return 测试组元素个数。
     */
    public int testCount() {

        return fTests.size();
    }

    /**
     * 枚举测试组元素。
     * 
     * @return 测试组元素的枚举。
     */
    public Enumeration<Test> tests() {

        return fTests.elements();
    }

    @Override
    public String toString() {
        if (getName() != null) {
            return getName();
        }
        return super.toString();
    }

    /**
     * 将目标类的目标测试方法做为测试用例添加到测试组中。
     * 
     * @param m
     *            目标测试方法。
     * @param names
     *            方法名列表，用于保证相同测试方法只被添加一次。
     * @param theClass
     *            目标类。
     */
    private void addTestMethod(Method m, List<String> names, Class<?> theClass) {
        
        String name = m.getName();
        if (names.contains(name)) {
            return;
        }
        if (!isPublicTestMethod(m)) {
            if (isTestMethod(m)) {
                addTest(warning("Test method isn't public: " + m.getName() + "(" + theClass.getCanonicalName() + ")"));
            }
            return;
        }
        names.add(name);
        addTest(createTest(theClass, name));
    }

    /**
     * 判断目标方法是否为公共的测试方法。
     * 
     * @param m
     *            目标方法。
     * @return 如果目标方法为公共的测试方法则返回true，否则返回false。
     */
    private boolean isPublicTestMethod(Method m) {

        return isTestMethod(m) && Modifier.isPublic(m.getModifiers());
    }

    /**
     * 判断目标方法是否为测试方法（以test开头的无参无返回方法）。
     * 
     * @param m
     *            目标方法。
     * @return 如果目标方法为测试方法则返回true，否则返回false。
     */
    private boolean isTestMethod(Method m) {
        
        return m.getParameterTypes().length == 0 && m.getName().startsWith("test") && m.getReturnType().equals(Void.TYPE);
    }
}