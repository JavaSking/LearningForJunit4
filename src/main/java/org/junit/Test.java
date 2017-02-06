package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Testע�⣬��ע <b><code>public void</code></b> ����Ϊ���Է�����<br>
 * ע�⣺<b>���в��Է���ǰ���ṹ��һ����ʵ������</b>��<br>
 * ��ѡ����:
 * <ul>
 * <li><code>expected</code>��ʾ�����׳����쳣���͡�
 * <li><code>timeout</code>��ʾ������ʱʱ�����������ʾ����ʧ�ܡ�
 * </ul>
 * ע�⣺<b>��Ȼ<code>timeout</code>���Զ��ڻ�ȡ����ֹ��ѭ����Ч�������ǲ�ȷ���ġ�<br>
 * ������Ĳ����Ƿ�ɹ�ȡ���ڲ���ϵͳ�����̡߳�
 * 
 * <pre>
 *    &#064;Test(<b>timeout=100</b>) 
 *    public void sleep100() {
 *       Thread.sleep(100);
 *    }
 * </pre>
 * 
 * <b>�̰߳�ȫ����:</b> 
 * <pre>
 * 1��ʹ������timeout��Test�����ǵ��̵߳�,�����@link FailOnTimeout��evaluateStatement()������
 * 2��ʹ��{@link org.junit.rules.Timeout}���򽫱�֤����@Before��@After������ͬһ���߳��С�
 * </pre>
 * 
 * @since 4.0
 * @author ע��By JavaSking 2017��2��4��
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Test {

	/**
	 * ���쳣��
	 * 
	 * @author ע��By JavaSking 2017��2��4��
	 */
	static class None extends Throwable {
		/**
		 * ���кš�
		 */
		private static final long serialVersionUID= 1L;

		/**
		 * ����һ�����쳣��
		 */
		private None() {

		}
	}

	/**
	 * �����׳����쳣���ͣ�Ĭ��Ϊ���쳣��
	 * 
	 * @return �����׳����쳣���͡�
	 */
	Class<? extends Throwable> expected() default None.class;

	/**
	 * ��ʱʱ�䣨���룩��
	 * 
	 * @return ��ʱʱ�䣨���룩��
	 */
	long timeout() default 0L;
}
