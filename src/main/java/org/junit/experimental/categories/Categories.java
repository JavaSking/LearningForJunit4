package org.junit.experimental.categories;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * From a given set of test classes, runs only the classes and methods that are
 * annotated with either the category given with the @IncludeCategory
 * annotation, or a subtype of that category.
 *
 * Note that, for now, annotating suites with {@code @Category} has no effect.
 * Categories must be annotated on the direct method or class.
 *
 * Example:
 *
 * <pre>
 * public interface FastTests {
 * }
 *
 * public interface SlowTests {
 * }
 *
 * public static class A {
 *  &#064;Test
 *  public void a() {
 *      fail();
 *     }
 *
 *  &#064;Category(SlowTests.class)
 *  &#064;Test
 *  public void b() {
 *     }
 * }
 *
 * &#064;Category( { SlowTests.class, FastTests.class })
 * public static class B {
 *  &#064;Test
 *  public void c() {
 *
 *     }
 * }
 *
 * &#064;RunWith(Categories.class)
 * &#064;IncludeCategory(SlowTests.class)
 * &#064;SuiteClasses( { A.class, B.class })
 * // Note that Categories is a kind of Suite
 * public static class SlowTestSuite {
 * }
 * </pre>
 */

/**
 * 
 * @author JavaSking 2017年2月6日
 */
public class Categories extends Suite {

	/**
	 * IncludeCategory注解用于标注包括的组。
	 * 
	 * @author 注释By JavaSking 2017年2月6日
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface IncludeCategory {

		/**
		 * 包括的组。
		 * 
		 * @return 包括的组。
		 */
		public Class<?> value();
	}

	/**
	 * ExcludeCategory注解用于标注排除的组。
	 * 
	 * @author 注释By JavaSking 2017年2月6日
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ExcludeCategory {

		/**
		 * 排除的组。
		 * 
		 * @return 排除的组。
		 */
		public Class<?> value();
	}

	/**
	 * 组过滤器。
	 * 
	 * @author 注释By JavaSking 2017年2月6日
	 */
	public static class CategoryFilter extends Filter {

		/**
		 * 包括目标组。
		 * 
		 * @param categoryType
		 *          目标组。
		 * @return 当前组过滤器。
		 */
		public static CategoryFilter include(Class<?> categoryType) {

			return new CategoryFilter(categoryType, null);
		}

		/**
		 * 包括的组。
		 */
		private final Class<?> fIncluded;

		/**
		 * 排除的组。
		 */
		private final Class<?> fExcluded;

		/**
		 * 构造一个组过滤器。
		 * 
		 * @param includedCategory
		 *          包括的组。
		 * @param excludedCategory
		 *          排除的组。
		 */
		public CategoryFilter(Class<?> includedCategory, Class<?> excludedCategory) {

			fIncluded = includedCategory;
			fExcluded = excludedCategory;
		}

		@Override
		public String describe() {

			return "category " + fIncluded;
		}

		@Override
		public boolean shouldRun(Description description) {

			if (hasCorrectCategoryAnnotation(description)) {
				return true;
			}
			for (Description each : description.getChildren()) {
				if (shouldRun(each)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 判断目标测试元素是否要被测试。<br>
		 * 需要测试的情况有：
		 * <ul>
		 * <li>1、未被分组且未指定IncludeCategory。
		 * <li>2、被分组，且被IncludeCategory包括。
		 * <li>3、被分组，且未指定IncludeCategory。
		 * <ul>
		 * @param description 目标测试元素的测试内容描述信息。
		 * @return 如果目标测试元素要被测试则返回true，否则返回false。
		 */
		private boolean hasCorrectCategoryAnnotation(Description description) {

			List<Class<?>> categories = categories(description);
			/* 1、未被分组且未指定IncludeCategory则需要测试 */
			if (categories.isEmpty()) {
				return fIncluded == null;
			}
			
			for (Class<?> each : categories) {
				/* 2、被分组且被IncludeCategory包括，则不需要测试 */
				if (fExcluded != null && fExcluded.isAssignableFrom(each)) {
					return false;
				}
			}
			for (Class<?> each : categories) {
				/* 2、被分组且（被指定IncludeCategory包括或未指定IncludeCategory），则需要测试 */
				if (fIncluded == null || fIncluded.isAssignableFrom(each)) {
					return true;
				}
			}
			/* 2、其他情况不需要测试 */
			return false;
		}

		/**
		 * 获取测试元素（测试方法或测试类）所属组。
		 * 
		 * @param description
		 *          测试元素的测试内容描述信息。
		 * @return 目标测试元素所属组。
		 */
		private List<Class<?>> categories(Description description) {

			ArrayList<Class<?>> categories = new ArrayList<Class<?>>();
			categories.addAll(Arrays.asList(directCategories(description)));
			categories.addAll(Arrays.asList(directCategories(parentDescription(description))));
			return categories;
		}

		/**
		 * 获取父测试元素的测试内容描述信息（到测试类为值，对于测试类的组合将直接返回null）。
		 * 
		 * @param description
		 *          测试元素的测试内容描述信息。
		 * @return 父测试元素的测试内容描述信息。
		 */
		private Description parentDescription(Description description) {

			Class<?> testClass = description.getTestClass();
			if (testClass == null) {
				return null;
			}
			return Description.createSuiteDescription(testClass);
		}

		/**
		 * 获取测试元素（测试方法或测试类）所属组，没有则返回空组。
		 * 
		 * @param description
		 *          测试元素的测试内容描述信息，可能为null。
		 * @return 目标测试元素所属组或空组。
		 */
		private Class<?>[] directCategories(Description description) {

			if (description == null) {
				return new Class<?>[0];
			}
			Category annotation = description.getAnnotation(Category.class);
			if (annotation == null) {
				return new Class<?>[0];
			}
			return annotation.value();
		}
	}

	/**
	 * 构造一个Categories运行器。
	 * 
	 * @param klass 测试类。
	 * @param builder 运行器创建工厂。
	 * @throws InitializationError 运行器初始化异常。
	 */
	public Categories(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		super(klass, builder);
		try {
			filter(new CategoryFilter(getIncludedCategory(klass), getExcludedCategory(klass)));
		} catch (NoTestsRemainException e) {
			throw new InitializationError(e);
		}
		assertNoCategorizedDescendentsOfUncategorizeableParents(getDescription());
	}

	/**
	 * 获取目标测试类的IncludedCategory注解属性值，没有则返回null。
	 * @param klass 测试类。
	 * @return 目标测试类的IncludedCategory注解属性值，没有则返回null。
	 */
	private Class<?> getIncludedCategory(Class<?> klass) {

		IncludeCategory annotation = klass.getAnnotation(IncludeCategory.class);
		return annotation == null ? null : annotation.value();
	}

	/**
	 * 获取目标测试类的ExcludeCategory注解属性值，没有则返回null。
	 * @param klass 测试类。
	 * @return 目标测试类的ExcludeCategory注解属性值，没有则返回null。
	 */
	private Class<?> getExcludedCategory(Class<?> klass) {

		ExcludeCategory annotation = klass.getAnnotation(ExcludeCategory.class);
		return annotation == null ? null : annotation.value();
	}

	/**
	 * 
	 * @param description 测试内容描述信息。
	 * @throws InitializationError 运行器初始化异常。
	 */
	private void assertNoCategorizedDescendentsOfUncategorizeableParents(Description description) throws InitializationError {

		if (!canHaveCategorizedChildren(description)) {
			assertNoDescendantsHaveCategoryAnnotations(description);
		}
		for (Description each : description.getChildren()) {
			assertNoCategorizedDescendentsOfUncategorizeableParents(each);
		}
	}

	private void assertNoDescendantsHaveCategoryAnnotations(Description description) throws InitializationError {

		for (Description each : description.getChildren()) {
			if (each.getAnnotation(Category.class) != null) {
				throw new InitializationError("Category annotations on Parameterized classes are not supported on individual methods.");
			}
			assertNoDescendantsHaveCategoryAnnotations(each);
		}
	}

	/**
	 * 
	 * @param description
	 * @return
	 */
	private static boolean canHaveCategorizedChildren(Description description) {

		for (Description each : description.getChildren()) {
			if (each.getTestClass() == null) {
				return false;
			}
		}
		return true;
	}
}