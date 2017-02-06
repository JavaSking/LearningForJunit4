package org.junit.runners;

import static org.junit.internal.runners.rules.RuleFieldValidator.CLASS_RULE_METHOD_VALIDATOR;
import static org.junit.internal.runners.rules.RuleFieldValidator.CLASS_RULE_VALIDATOR;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * ����������е�ĳ����Ҷ�ӽڵ����������
 * @since 4.5
 * @author ע��By JavaSking
 * 2017��2��5��
 */
public abstract class ParentRunner<T> extends Runner implements Filterable, Sortable {
	/**
	 * �����������ʱ��װ����
	 */
    private final TestClass fTestClass;

    /**
     * ������������Ĭ��Ϊ���������������
     */
    private Sorter fSorter = Sorter.NULL;

    /**
     * �����б�
     * <pre>
     * �����ǰ������Ϊ��������������������������Ϊ����������{@code Suite}��
     * �����ǰ������Ϊ��һ������������������������ΪFrameworkMethod����{@code BlockJUnit4ClassRunner}��
     * </pre>
     */
    private List<T> fFilteredChildren = null;

    /**
     * ��������������
     */
    private RunnerScheduler fScheduler = new RunnerScheduler() {
    	
        public void schedule(Runnable childStatement) {
        	
            childStatement.run();
        }

        public void finished() {
            
        }
    };

    /**
     * ����һ������Ŀ����������������
     * 
     * @param testClass Ŀ������ࡣ
     * @throws InitializationError ��������ʼ���쳣��
     */
    protected ParentRunner(Class<?> testClass) throws InitializationError {
    	
        fTestClass = new TestClass(testClass);
        validate();//��������ĺϷ��ԡ�
    }

    /**
     * ��ȡ�����б�
     * 
     * @return �����б�
     */
    protected abstract List<T> getChildren();

    /**
     * ��ȡĿ�꺢�ӵĲ�������������Ϣ��
     * 
     * @param child Ŀ�꺢�ӡ�
     * @return Ŀ�꺢�ӵĲ�������������Ϣ��
     */
    protected abstract Description describeChild(T child);

    /**
     * ����Ŀ�꺢�ӹ����Ĳ��ԡ�
     * 
     * @param child Ŀ�꺢�ӡ�
     * @param notifier ���м����������ߡ�
     */
    protected abstract void runChild(T child, RunNotifier notifier);

    /**
     * У��BeforeClass��AfterClassע���ע����ȷ�ԡ�<br>
     * BeforeClass��AfterClassע��Ӧ�ñ�ע��{@code public static void}���޲η����ϡ�
     * 
     * @param errors �����б������ռ�����
     */
    protected void collectInitializationErrors(List<Throwable> errors) {
    	
        validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
        validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
        validateClassRules(errors);
    }

    /**
     * ���Ŀ��ע���ע����ȷ�ԡ�
     * 
     * @param annotation Ŀ��ע�����͡�
     * @param isStatic �Ƿ�Ҫ��Ϊ��̬������
     * @param errors �����б������ռ�����
     */
    protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
    	
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
        for (FrameworkMethod eachTestMethod : methods) {
            eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
        }
    }

    /**
     * ���ClassRule����ȷ�ԡ�
     * 
     * @param errors �����б������ռ�����
     */
    private void validateClassRules(List<Throwable> errors) {
    	
        CLASS_RULE_VALIDATOR.validate(getTestClass(), errors);
        CLASS_RULE_METHOD_VALIDATOR.validate(getTestClass(), errors);
    }

    /**
     * Constructs a {@code Statement} to run all of the tests in the test class. Override to add pre-/post-processing.
     * Here is an outline of the implementation:
     * <ul>
     * <li>Call {@link #runChild(Object, RunNotifier)} on each object returned by {@link #getChildren()} (subject to any imposed filter and sort).</li>
     * <li>ALWAYS run all non-overridden {@code @BeforeClass} methods on this class
     * and superclasses before the previous step; if any throws an
     * Exception, stop execution and pass the exception on.
     * <li>ALWAYS run all non-overridden {@code @AfterClass} methods on this class
     * and superclasses before any of the previous steps; all AfterClass methods are
     * always executed: exceptions thrown by previous steps are combined, if
     * necessary, with exceptions from AfterClass methods into a
     * {@link MultipleFailureException}.
     * </ul>
     *
     * @return {@code Statement}
     */
    protected Statement classBlock(final RunNotifier notifier) {
    	
    	/* ��������[AfterClass-->][BeforeClass-->]���в��Է��� */
        Statement statement = childrenInvoker(notifier);//���в��Է���
        statement = withBeforeClasses(statement);//BeforeClass
        statement = withAfterClasses(statement);//AfterClass
        statement = withClassRules(statement);
        return statement;
    }

    /**
     * �����ǰ���������BeforeClassע���ע�ķ��������ȡ��BeforeClass<br>
     * ע���ע�ķ�������Ķ����������������ֱ�ӷ���Ŀ�궯����<br>
     * �����Ŀ�궯��ʵ���Ͼ������в��Է���������ɵĶ�����
     * 
     * @param statement ��һ�����������в��Է���������ɵĶ�������
     * @return ������
     */
    protected Statement withBeforeClasses(Statement statement) {
        List<FrameworkMethod> befores = fTestClass
                .getAnnotatedMethods(BeforeClass.class);
        return befores.isEmpty() ? statement :
                new RunBefores(statement, befores, null);
    }

    /**
     * �����ǰ���������AfterClassע���ע�ķ��������ȡ��AfterClass<br>
     * ע���ע�ķ�������Ķ����������������ֱ�ӷ���Ŀ�궯����<br>
     * �����Ŀ�궯�������ǣ�
     * <pre>
     * 1���������BeforeClassע�⣬��ΪBeforeע���ע�ķ�������Ķ�����
     * 2�����������BeforeClassע�⣬��Ϊ���в��Է���������ɵĶ�����
     * </pre>
     * 
     * @param statement Ŀ�궯����
     * @return ������
     */
    protected Statement withAfterClasses(Statement statement) {
        List<FrameworkMethod> afters = fTestClass
                .getAnnotatedMethods(AfterClass.class);
        return afters.isEmpty() ? statement :
                new RunAfters(statement, afters, null);
    }

    /**
     * Returns a {@link Statement}: apply all
     * static fields assignable to {@link TestRule}
     * annotated with {@link ClassRule}.
     *
     * @param statement the base statement
     * @return a RunRules statement if any class-level {@link Rule}s are
     *         found, or the base statement
     */
    private Statement withClassRules(Statement statement) {
        List<TestRule> classRules = classRules();
        return classRules.isEmpty() ? statement :
                new RunRules(statement, classRules, getDescription());
    }

    /**
     * @return the {@code ClassRule}s that can transform the block that runs
     *         each method in the tested class.
     */
    protected List<TestRule> classRules() {
        List<TestRule> result = fTestClass.getAnnotatedMethodValues(null, ClassRule.class, TestRule.class);

        result.addAll(fTestClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class));

        return result;
    }

    /**
     * �������й��˻���ĺ����ǹ����Ĳ��ԵĶ�����
     * 
     * @param notifier ���м����������ߡ�
     * @return ���й��˻���ĺ����ǹ����Ĳ��ԵĶ�����
     */
    protected Statement childrenInvoker(final RunNotifier notifier) {
    	
        return new Statement() {
            @Override
            public void evaluate() {
            	
                runChildren(notifier);
            }
        };
    }

    /**
     * ���й��˻���ĺ����ǹ����Ĳ��ԡ�
     * 
     * @param notifier ���м����������ߡ�
     */
    private void runChildren(final RunNotifier notifier) {
    	
        for (final T each : getFilteredChildren()) {
            fScheduler.schedule(new Runnable() {
                public void run() {
                    ParentRunner.this.runChild(each, notifier);
                }
            });
        }
        fScheduler.finished();
    }

    /**
     * ��ȡ����������
     * 
     * @return ����������
     */
    protected String getName() {
    	
        return fTestClass.getName();
    }

    /**
     * ��ȡ�����������ʱ��װ����
     * 
     * @return �����������ʱ��װ����
     */
    public final TestClass getTestClass() {
    	
        return fTestClass;
    }

    /**
     * Runs a {@link Statement} that represents a leaf (aka atomic) test.
     */
    
    /**
     * ִ�д���ԭ�Ӳ��Ե�Ŀ�궯����
     * 
     * @param statement ��ִ�е�Ŀ�궯����
     * @param description ��������������Ϣ��
     * @param notifier ���м����������ߡ�
     */
    protected final void runLeaf(Statement statement, Description description, RunNotifier notifier) {
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();
        try {
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            eachNotifier.addFailure(e);
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    /**
     * ��ȡ�������б�ע��ע�⡣
     * 
     * @return �������б�ע��ע�⡣
     */
    protected Annotation[] getRunnerAnnotations() {
    	
        return fTestClass.getAnnotations();
    }


    @Override
    public Description getDescription() {
        Description description = Description.createSuiteDescription(getName(),
                getRunnerAnnotations());
        for (T child : getFilteredChildren()) {
            description.addChild(describeChild(child));
        }
        return description;
    }

    @Override
    public void run(final RunNotifier notifier) {
        EachTestNotifier testNotifier = new EachTestNotifier(notifier,
                getDescription());
        try {
            Statement statement = classBlock(notifier);
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            testNotifier.fireTestIgnored();
        } catch (StoppedByUserException e) {
            throw e;
        } catch (Throwable e) {
            testNotifier.addFailure(e);
        }
    }

    public void filter(Filter filter) throws NoTestsRemainException {
    	
        for (Iterator<T> iter = getFilteredChildren().iterator(); iter.hasNext(); ) {
            T each = iter.next();
            if (shouldRun(filter, each)) {
                try {
                    filter.apply(each);
                } catch (NoTestsRemainException e) {
                    iter.remove();
                }
            } else {
                iter.remove();
            }
        }
        if (getFilteredChildren().isEmpty()) {
            throw new NoTestsRemainException();
        }
    }

    public void sort(Sorter sorter) {
    	
        fSorter = sorter;
        for (T each : getFilteredChildren()) {
            sortChild(each);
        }
        Collections.sort(getFilteredChildren(), comparator());
    }

    /**
     * У�������ĺϷ��ԣ�������ִ������׳���������ʼ���쳣��
     * 
     * @throws InitializationError ��������ʼ���쳣��
     */
    private void validate() throws InitializationError {
    	
        List<Throwable> errors = new ArrayList<Throwable>();
        collectInitializationErrors(errors);
        if (!errors.isEmpty()) {
            throw new InitializationError(errors);
        }
    }

    /**
     * ��ȡ���˻��ĺ����б�
     * 
     * @return ���˻��ĺ����б�
     */
    private List<T> getFilteredChildren() {
    	
        if (fFilteredChildren == null) {
            fFilteredChildren = new ArrayList<T>(getChildren());
        }
        return fFilteredChildren;
    }

    /**
     * ��Ŀ�꺢��Ӧ����������
     * 
     * @param child ���ӡ�
     */
    private void sortChild(T child) {
    	
        fSorter.apply(child);
    }

    /**
     * �ж�Ŀ���Ƿ�ָ���Ĺ��������С�
     * 
     * @param filter ��������
     * @param each Ŀ�ꡣ
     * @return ���Ŀ�걻ָ���Ĺ����������򷵻�true�����򷵻�false��
     */
    private boolean shouldRun(Filter filter, T each) {
    	
        return filter.shouldRun(describeChild(each));
    }

    /**
     * �½������������ء�
     * 
     * @return �½�����������
     */
    private Comparator<? super T> comparator() {
    	
        return new Comparator<T>() {
            public int compare(T o1, T o2) {
            	
                return fSorter.compare(describeChild(o1), describeChild(o2));
            }
        };
    }

    /**
     * �������е�������
     * 
     * @param scheduler �����õ����е�������
     */
    public void setScheduler(RunnerScheduler scheduler) {
    	
        this.fScheduler = scheduler;
    }
}
