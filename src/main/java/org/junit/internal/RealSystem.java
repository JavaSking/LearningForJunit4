package org.junit.internal;

import java.io.PrintStream;

/**
 * ����ʵ��ϵͳ��
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class RealSystem implements JUnitSystem {
	
	public PrintStream out() {
		
		return System.out;
	}
}
