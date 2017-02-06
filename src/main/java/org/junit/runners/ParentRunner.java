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
 * 代表测试树中的某个非叶子节点的运行器。
 * @since 4.5
 * @author 注释By JavaSking
 * 2017年2月5日
 */
public abstract class ParentRunner<T> extends Runner implements Filterable, Sortable {
	/**
	 * 测试类的运行时包装对象。
	 */
    private final TestClass fTestClass;

    /**
     * 测试排序器，默认为乱序测试排序器。
     */
    private Sorter fSorter = Sorter.NULL;

    /**
     * 孩子列表。
     * <pre>
     * 如果当前运行器为多类测试组的运行器，则孩子类型为运行器，如{@code Suite}。
     * 如果当前运行器为单一类测试组的运行器，则孩子类型为FrameworkMethod，如{@code BlockJUnit4ClassRunner}。
     * </pre>
     */
    private List<T> fFilteredChildren = null;

    /**
     * 运行器调度器。
     */
    private RunnerScheduler fScheduler = new RunnerScheduler() {
    	
        public void schedule(Runnable childStatement) {
        	
            childStatement.run();
        }

        public void finished() {
            
        }
    };

    /**
     * 构造一个负责目标测试类的运行器。
     * 
     * @param testClass 目标测试类。
     * @throws InitializationError 运行器初始化异常。
     */
    protected ParentRunner(Class<?> testClass) throws InitializationError {
    	
        fTestClass = new TestClass(testClass);
        validate();//检查测试类的合法性。
    }

    /**
     * 获取孩子列表。
     * 
     * @return 孩子列表。
     */
    protected abstract List<T> getChildren();

    /**
     * 获取目标孩子的测试内容描述信息。
     * 
     * @param child 目标孩子。
     * @return 目标孩子的测试内容描述信息。
     */
    protected abstract Description describeChild(T child);

    /**
     * 运行目标孩子关联的测试。
     * 
     * @param child 目标孩子。
     * @param notifier 运行监听器管理者。
     */
    protected abstract void runChild(T child, RunNotifier notifier);

    /**
     * 校验BeforeClass和AfterClass注解标注的正确性。<br>
     * BeforeClass和AfterClass注解应该标注于{@code public static void}的无参方法上。
     * 
     * @param errors 错误列表，用于收集错误。
     */
    protected void collectInitializationErrors(List<Throwable> errors) {
    	
        validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
        validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
        validateClassRules(errors);
    }

    /**
     * 检查目标注解标注的正确性。
     * 
     * @param annotation 目标注解类型。
     * @param isStatic 是否要求为静态方法。
     * @param errors 错误列表，用于收集错误。
     */
    protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
    	
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
        for (FrameworkMethod eachTestMethod : methods) {
            eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
        }
    }

    /**
     * 检查ClassRule的正确性。
     * 
     * @param errors 错误列表，用于收集错误。
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
    	
    	/* 动作链：[AfterClass-->][BeforeClass-->]所有测试方法 */
        Statement statement = childrenInvoker(notifier);//所有测试方法
        statement = withBeforeClasses(statement);//BeforeClass
        statement = withAfterClasses(statement);//AfterClass
        statement = withClassRules(statement);
        return statement;
    }

    /**
     * 如果当前测试类存在BeforeClass注解标注的方法，则获取由BeforeClass<br>
     * 注解标注的方法代表的动作，如果不存在则直接返回目标动作。<br>
     * 这里的目标动作实际上就是所有测试方法运行组成的动作。
     * 
     * @param statement 下一条动作（所有测试方法运行组成的动作）。
     * @return 动作。
     */
    protected Statement withBeforeClasses(Statement statement) {
        List<FrameworkMethod> befores = fTestClass
                .getAnnotatedMethods(BeforeClass.class);
        return befores.isEmpty() ? statement :
                new RunBefores(statement, befores, null);
    }

    /**
     * 如果当前测试类存在AfterClass注解标注的方法，则获取由AfterClass<br>
     * 注解标注的方法代表的动作，如果不存在则直接返回目标动作。<br>
     * 这里的目标动作可能是：
     * <pre>
     * 1、如果存在BeforeClass注解，则为Before注解标注的方法代表的动作。
     * 2、如果不存在BeforeClass注解，则为所有测试方法运行组成的动作。
     * </pre>
     * 
     * @param statement 目标动作。
     * @return 动作。
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
     * 创建运行过滤化后的孩子们关联的测试的动作。
     * 
     * @param notifier 运行监听器管理者。
     * @return 运行过滤化后的孩子们关联的测试的动作。
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
     * 运行过滤化后的孩子们关联的测试。
     * 
     * @param notifier 运行监听器管理者。
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
     * 获取测试类名。
     * 
     * @return 测试类名。
     */
    protected String getName() {
    	
        return fTestClass.getName();
    }

    /**
     * 获取测试类的运行时包装对象。
     * 
     * @return 测试类的运行时包装对象。
     */
    public final TestClass getTestClass() {
    	
        return fTestClass;
    }

    /**
     * Runs a {@link Statement} that represents a leaf (aka atomic) test.
     */
    
    /**
     * 执行代表原子测试的目标动作。
     * 
     * @param statement 待执行的目标动作。
     * @param description 测试内容描述信息。
     * @param notifier 运行监听器管理者。
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
     * 获取测试类中标注的注解。
     * 
     * @return 测试类中标注的注解。
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
     * 校验测试类的合法性，如果出现错误则抛出运行器初始化异常。
     * 
     * @throws InitializationError 运行器初始化异常。
     */
    private void validate() throws InitializationError {
    	
        List<Throwable> errors = new ArrayList<Throwable>();
        collectInitializationErrors(errors);
        if (!errors.isEmpty()) {
            throw new InitializationError(errors);
        }
    }

    /**
     * 获取过滤化的孩子列表。
     * 
     * @return 过滤化的孩子列表。
     */
    private List<T> getFilteredChildren() {
    	
        if (fFilteredChildren == null) {
            fFilteredChildren = new ArrayList<T>(getChildren());
        }
        return fFilteredChildren;
    }

    /**
     * 对目标孩子应用排序器。
     * 
     * @param child 孩子。
     */
    private void sortChild(T child) {
    	
        fSorter.apply(child);
    }

    /**
     * 判断目标是否被指定的过滤器放行。
     * 
     * @param filter 过滤器。
     * @param each 目标。
     * @return 如果目标被指定的过滤器放行则返回true，否则返回false。
     */
    private boolean shouldRun(Filter filter, T each) {
    	
        return filter.shouldRun(describeChild(each));
    }

    /**
     * 新建排序器并返回。
     * 
     * @return 新建的排序器。
     */
    private Comparator<? super T> comparator() {
    	
        return new Comparator<T>() {
            public int compare(T o1, T o2) {
            	
                return fSorter.compare(describeChild(o1), describeChild(o2));
            }
        };
    }

    /**
     * 设置运行调度器。
     * 
     * @param scheduler 待设置的运行调度器。
     */
    public void setScheduler(RunnerScheduler scheduler) {
    	
        this.fScheduler = scheduler;
    }
}
