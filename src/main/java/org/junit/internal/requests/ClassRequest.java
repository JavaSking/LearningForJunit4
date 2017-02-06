package org.junit.internal.requests;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Request;
import org.junit.runner.Runner;
/**
 * 测试目标类的测试请求。
 * @author 注释 JavaSking
 * 2017年2月5日
 */
public class ClassRequest extends Request {
	/**
	 * 锁。
	 */
    private final Object fRunnerLock = new Object();
    /**
     * 潜在的目标测试类。
     */
    private final Class<?> fTestClass;
    
    private final boolean fCanUseSuiteMethod;
    /**
     * 负责运行的运行器。
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