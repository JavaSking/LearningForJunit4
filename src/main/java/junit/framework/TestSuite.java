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
 * �����飬����Ƕ�׶������������������<br>
 * ���ӣ�
 * 
 * <pre>
 * TestSuite suite = new TestSuite();
 * suite.addTest(new MathTest("testAdd")); // 1����Ӳ�������
 * suite.addTest(new MathTest("testDivideByZero"));
 * </pre>
 * 
 * <pre>
 * TestSuite suite = new TestSuite(MathTest.class);// 2���Զ����Ŀ�����������в�������
 * </pre>
 * 
 * <pre>
 * Class[] testClasses = { MathTest.class, AnotherTest.class };
 * 
 * TestSuite suite = new TestSuite(testClasses);// 3���Զ���Ӷ���������ȫ����������
 * </pre>
 * 
 * @see Test
 * @author ע��By JavaSking 2017��2��5��
 */
public class TestSuite implements Test {

    /**
     * ����һ������������<br>
     * ���������֤������ÿ�����Է���ǰ�����ᴴ���µ���ʵ����
     * 
     * @param theClass
     *            �����ࡣ
     * @param name
     *            ���������������Է���������
     * @return �½��Ĳ���������
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
     * ���Ի�ȡĿ�������Ĺ��췽������ȡʧ�����׳�NoSuchMethodException�쳣��<br>
     * ע�⣺<b>������Ĺ��췽��ֻ��ΪXXX(String������XXX()��</b>
     * 
     * @param theClass
     *            Ŀ������ࡣ
     * @return Ŀ�������Ĺ��췽����
     * @throws NoSuchMethodException
     *             �����������쳣��
     */
    public static Constructor<?> getTestConstructor(Class<?> theClass) throws NoSuchMethodException {
        
        try {
            return theClass.getConstructor(String.class);
        } catch (NoSuchMethodException e) {

        }
        return theClass.getConstructor(new Class[0]);
    }

    /**
     * ����Ϣ��ʾ������װ��һ�����Բ����أ��ò��Խ���������Ϣ��ʾ�Ķ�����
     * 
     * @param message
     *            �辯ʾ����Ϣ��
     * @return ����������ʾĿ����Ϣ�����Ĳ��ԡ�
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
     * ��ȡĿ���쳣�Ķ�ջ��Ϣ��
     * 
     * @param t Ŀ���쳣��
     * 
     * @return
     * 			Ŀ���쳣�Ķ�ջ��Ϣ��
     */
    private static String exceptionToString(Throwable t) {
    	
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        return stringWriter.toString();
    }
    /**
     * ���������ơ�
     */
    private String fName;

    /**
     * ���Լ���Ԫ�ؿ���Ϊ�������һ�Ĳ���������
     */
    private Vector<Test> fTests = new Vector<Test>(10);

    /**
     * ����һ���յĲ����顣
     */
    public TestSuite() {

    }

    /**
     * ����һ������Ŀ�������ȫ�����������Ĳ����顣<br>
     * ����test��ͷ�ķ���������������������ӵ������顣
     * 
     * @param theClass
     *            Ŀ������ࡣ
     */
    public TestSuite(final Class<?> theClass) {

        addTestsFromTestCase(theClass);
    }

    /**
     * ���Ŀ�����������в��������������顣<br>
     * ����test��ͷ�ķ������������԰�������ӵ������顣<br>
     * ��������Ӵ�����������ݣ���ͬ����ֻ�����һ�Ρ�
     * 
     * @param theClass
     *            Ŀ������ࡣ
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
     * ����һ������Ŀ�������ȫ������������ָ�����ƵĲ����顣
     * 
     * @see TestSuite#TestSuite(Class)
     * @param theClass
     *            Ŀ������ࡣ
     * @param name
     *            ����������
     */
    public TestSuite(Class<? extends TestCase> theClass, String name) {

        this(theClass);
        setName(name);
    }

    /**
     * ����һ��ָ�����ƵĿղ����顣
     * 
     * @param name
     *            ����������
     */
    public TestSuite(String name) {

        setName(name);
    }

    /**
     * ����һ������Ŀ������༯ȫ�����������Ĳ����顣
     * 
     * @param classes
     *            Ŀ������༯��
     */
    public TestSuite(Class<?>... classes) {

        for (Class<?> each : classes) {
            addTest(testCaseForClass(each));
        }
    }

    /**
     * ����һ������Ŀ�������ȫ�����������Ĳ����顣
     * 
     * @param each
     *            Ŀ������ࡣ
     * @return ����Ŀ�������ȫ�����������Ĳ����顣
     */
    private Test testCaseForClass(Class<?> each) {

        if (TestCase.class.isAssignableFrom(each)) {
            return new TestSuite(each.asSubclass(TestCase.class));
        } else {
            return warning(each.getCanonicalName() + " does not extend TestCase");
        }
    }

    /**
     * ����һ������Ŀ������༯ȫ������������ָ�����ƵĲ����顣
     * 
     * @see TestSuite#TestSuite(Class[])
     * @param classes
     *            Ŀ������༯��
     * @param name
     *            ����������
     */
    public TestSuite(Class<? extends TestCase>[] classes, String name) {

        this(classes);
        setName(name);
    }

    /**
     * ���Ŀ����ԣ�����/�飩����ǰ�������С�
     * 
     * @param test
     *            ����ӵ�Ŀ����ԣ�����/�飩��
     */
    public void addTest(Test test) {

        fTests.add(test);
    }

    /**
     * ���Ŀ�����������в�����������ǰ�����顣
     * 
     * @param testClass
     *            Ŀ������ࣨTestCase���ͣ���
     */
    public void addTestSuite(Class<? extends TestCase> testClass) {

        addTest(new TestSuite(testClass));
    }

    /**
     * ͳ�Ƶ�ǰ����������Ĳ�����������
     * 
     * @return ��ǰ����������Ĳ�����������
     */
    public int countTestCases() {

        int count = 0;
        for (Test each : fTests) {
            count += each.countTestCases();
        }
        return count;
    }

    /**
     * ��ȡ����������
     * 
     * @return �������������ܷ���null��
     */
    public String getName() {

        return fName;
    }

    /**
     * ���в����飬���ռ����Խ������;������ֹ��
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
     * ����Ŀ����Բ��ռ����Խ����
     * 
     * @param test
     *            Ŀ����ԡ�
     * @param result
     *            ���Խ����
     */
    public void runTest(Test test, TestResult result) {

        test.run(result);
    }

    /**
     * ���ò���������
     * 
     * @param name
     *            �����õĲ���������
     */
    public void setName(String name) {

        fName = name;
    }

    /**
     * ��ȡĿ��λ�õĲ�����Ԫ�ء�
     * 
     * @param index
     *            λ��������
     * @return Ŀ��λ�õĲ�����Ԫ�ء�
     */
    public Test testAt(int index) {

        return fTests.get(index);
    }

    /**
     * ͳ�Ʋ�����Ԫ�ظ�����
     * 
     * @return ������Ԫ�ظ�����
     */
    public int testCount() {

        return fTests.size();
    }

    /**
     * ö�ٲ�����Ԫ�ء�
     * 
     * @return ������Ԫ�ص�ö�١�
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
     * ��Ŀ�����Ŀ����Է�����Ϊ����������ӵ��������С�
     * 
     * @param m
     *            Ŀ����Է�����
     * @param names
     *            �������б����ڱ�֤��ͬ���Է���ֻ�����һ�Ρ�
     * @param theClass
     *            Ŀ���ࡣ
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
     * �ж�Ŀ�귽���Ƿ�Ϊ�����Ĳ��Է�����
     * 
     * @param m
     *            Ŀ�귽����
     * @return ���Ŀ�귽��Ϊ�����Ĳ��Է����򷵻�true�����򷵻�false��
     */
    private boolean isPublicTestMethod(Method m) {

        return isTestMethod(m) && Modifier.isPublic(m.getModifiers());
    }

    /**
     * �ж�Ŀ�귽���Ƿ�Ϊ���Է�������test��ͷ���޲��޷��ط�������
     * 
     * @param m
     *            Ŀ�귽����
     * @return ���Ŀ�귽��Ϊ���Է����򷵻�true�����򷵻�false��
     */
    private boolean isTestMethod(Method m) {
        
        return m.getParameterTypes().length == 0 && m.getName().startsWith("test") && m.getReturnType().equals(Void.TYPE);
    }
}