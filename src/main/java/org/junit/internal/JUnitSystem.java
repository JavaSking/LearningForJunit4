package org.junit.internal;

import java.io.PrintStream;

/**
 * Junit封装的系统接口。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public interface JUnitSystem {
	/**
	 * 获取打印流。
	 * 
	 * @return 打印流。
	 */
	PrintStream out();
}
