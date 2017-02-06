package org.junit.internal.runners;

import junit.extensions.TestDecorator;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

/**
 * 向后兼容JUnit3而定义的运行器。<br>
 * 该运行器负责运行旧风格的测试类（继承自TestCase）。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class JUnit38ClassRunner extends Runner implements Filterable, Sortable {
	
	/**
	 * 旧测试类的适配测试监听器。
	 * 
	 * @author 注释By JavaSking 2017年2月5日
	 */
	private final class OldTestClassAdaptingListener implements TestListener {

		/**
		 * 运行监听器管理者。
		 */
		private final RunNotifier fNotifier;

		/**
		 * 构造一个旧测试类的适配测试监听器。
		 * 
		 * @param notifier
		 *            运行监听器管理者。
		 */
		private OldTestClassAdaptingListener(RunNotifier notifier) {

			fNotifier= notifier;
		}

		public void endTest(Test test) {

			fNotifier.fireTestFinished(asDescription(test));
		}

		public void startTest(Test test) {

			fNotifier.fireTestStarted(asDescription(test));
		}

		public void addError(Test test, Throwable t) {

			Failure failure= new Failure(asDescription(test), t);
			fNotifier.fireTestFailure(failure);
		}

		/**
		 * 获取目标测试的测试内容描述信息对象。
		 * 
		 * @param test
		 *            目标测试。
		 * @return 目标测试的测试内容描述信息对象。
		 */
		private Description asDescription(Test test) {

			if (test instanceof Describable) {
				Describable facade= (Describable) test;
				return facade.getDescription();
			}
			return Description.createTestDescription(getEffectiveClass(test), getName(test));
		}

		/**
		 * 获取目标测试的类类型。
		 * 
		 * @param test
		 *            目标测试。
		 * @return 目标测试的类类型。
		 */
		private Class<? extends Test> getEffectiveClass(Test test) {

			return test.getClass();
		}

		/**
		 * 获取测试名。
		 * 
		 * @param test
		 *            目标测试。
		 * @return 测试名。
		 */
		private String getName(Test test) {

			if (test instanceof TestCase) {
				return ((TestCase) test).getName();
			} else {
				return test.toString();
			}
		}

		public void addFailure(Test test, AssertionFailedError t) {

			addError(test, t);
		}
	}

	/**
	 * 潜在的测试对象。
	 */
	private Test fTest;

	/**
	 * 构造一个目标测试类的运行器。
	 * 
	 * @param klass
	 *            目标测试类。
	 */
	public JUnit38ClassRunner(Class<?> klass) {

		this(new TestSuite(klass.asSubclass(TestCase.class)));
	}

	/**
	 * 构造一个目标测试的运行器。
	 * 
	 * @param test
	 *            目标测试。
	 */
	public JUnit38ClassRunner(Test test) {

		super();
		setTest(test);
	}

	@Override
	public void run(RunNotifier notifier) {

		TestResult result= new TestResult();
		result.addListener(createAdaptingListener(notifier));
		getTest().run(result);
	}

	/**
	 * 新建一个旧测试类的适配测试监听器。
	 * 
	 * @param notifier
	 *            运行监听器管理者。
	 * @return 旧测试类的适配测试监听器。
	 */
	public TestListener createAdaptingListener(final RunNotifier notifier) {

		return new OldTestClassAdaptingListener(notifier);
	}

	@Override
	public Description getDescription() {

		return makeDescription(getTest());
	}

	/**
	 * 获取目标测试对象的测试内容描述信息对象。
	 * 
	 * @param test
	 *            目标测试对象。
	 * @return 目标测试对象的测试内容描述信息对象。
	 */
	private static Description makeDescription(Test test) {
		
		if (test instanceof TestCase) {
			TestCase tc= (TestCase) test;
			return Description.createTestDescription(tc.getClass(), tc.getName());
		} else if (test instanceof TestSuite) {
			TestSuite ts= (TestSuite) test;
			String name= ts.getName() == null ? createSuiteDescription(ts) : ts.getName();
			Description description= Description.createSuiteDescription(name);
			int n= ts.testCount();
			for (int i= 0; i < n; i++) {
				Description made= makeDescription(ts.testAt(i));
				description.addChild(made);
			}
			return description;
		} else if (test instanceof Describable) {
			Describable adapter= (Describable) test;
			return adapter.getDescription();
		} else if (test instanceof TestDecorator) {
			TestDecorator decorator= (TestDecorator) test;
			return makeDescription(decorator.getTest());
		} else {
			return Description.createSuiteDescription(test.getClass());
		}
	}

	/**
	 * 描述目标测试组。
	 * 
	 * @param ts
	 *            目标测试组。
	 * @return 目标测试组的描述性语句。
	 */
	private static String createSuiteDescription(TestSuite ts) {

		int count= ts.countTestCases();
		String example= count == 0 ? "" : String.format(" [example: %s]", ts.testAt(0));
		return String.format("TestSuite with %s tests%s", count, example);
	}

	public void filter(Filter filter) throws NoTestsRemainException {

		if (getTest() instanceof Filterable) {
			Filterable adapter= (Filterable) getTest();
			adapter.filter(filter);
		} else if (getTest() instanceof TestSuite) {
			TestSuite suite= (TestSuite) getTest();
			TestSuite filtered= new TestSuite(suite.getName());
			int n= suite.testCount();
			for (int i= 0; i < n; i++) {
				Test test= suite.testAt(i);
				if (filter.shouldRun(makeDescription(test))) {
					filtered.addTest(test);
				}
			}
			setTest(filtered);
		}
	}

	public void sort(Sorter sorter) {

		if (getTest() instanceof Sortable) {
			Sortable adapter= (Sortable) getTest();
			adapter.sort(sorter);
		}
	}

	/**
	 * 设置潜在的测试对象。
	 * 
	 * @param test
	 *            待设置的潜在的测试对象。
	 */
	private void setTest(Test test) {

		fTest= test;
	}

	/**
	 * 获取潜在的测试对象。
	 * 
	 * @return 潜在的测试对象。
	 */
	private Test getTest() {

		return fTest;
	}
}
