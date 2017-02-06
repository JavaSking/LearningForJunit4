package org.junit.internal;

import java.io.PrintStream;

/**
 * “真实”系统。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class RealSystem implements JUnitSystem {
	
	public PrintStream out() {
		
		return System.out;
	}
}
