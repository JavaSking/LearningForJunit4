package org.junit.internal.requests;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Request;
import org.junit.runner.Runner;
/**
 * ����Ŀ����Ĳ�������
 * @author ע�� JavaSking
 * 2017��2��5��
 */
public class ClassRequest extends Request {
	/**
	 * ����
	 */
    private final Object fRunnerLock = new Object();
    /**
     * Ǳ�ڵ�Ŀ������ࡣ
     */
    private final Class<?> fTestClass;
    
    private final boolean fCanUseSuiteMethod;
    /**
     * �������е���������
     */
    private Runner fRunner;

    public ClassRequest(Class<?> testClass, boolean canUseSuiteMethod) {
    	
        fTestClass = testClass;
        fCanUseSuiteMethod = canUseSuiteMethod;
    }

    public ClassRequest(Class<?> testClass) {
    	
        this(testClass, true);
    }

    @Override
    public Runner getRunner() {
    	
        synchronized (fRunnerLock) {
            if (fRunner == null) {
                fRunner = new AllDefaultPossibilitiesBuilder(fCanUseSuiteMethod).safeRunnerForClass(fTestClass);
            }
            return fRunner;
        }
    }
}