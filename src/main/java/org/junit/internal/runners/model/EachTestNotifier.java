package org.junit.internal.runners.model;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.MultipleFailureException;

public class EachTestNotifier {
	/**
	 * 运行监听器管理者。
	 */
    private final RunNotifier fNotifier;

    /**
     * 测试内容描述信息。
     */
    private final Description fDescription;

    /**
     * 
     * @param notifier 运行监听器管理者。
     * @param description 测试内容描述信息。
     */
    public EachTestNotifier(RunNotifier notifier, Description description) {
    	
        fNotifier = notifier;
        fDescription = description;
    }

    public void addFailure(Throwable targetException) {
    	
        if (targetException instanceof MultipleFailureException) {
            addMultipleFailureException((MultipleFailureException) targetException);
        } else {
            fNotifier
                    .fireTestFailure(new Failure(fDescription, targetException));
        }
    }

    private void addMultipleFailureException(MultipleFailureException mfe) {
        for (Throwable each : mfe.getFailures()) {
            addFailure(each);
        }
    }

    public void addFailedAssumption(AssumptionViolatedException e) {
        fNotifier.fireTestAssumptionFailed(new Failure(fDescription, e));
    }

    public void fireTestFinished() {
    	
        fNotifier.fireTestFinished(fDescription);
    }

    public void fireTestStarted() {
    	
        fNotifier.fireTestStarted(fDescription);
    }

    public void fireTestIgnored() {
    	
        fNotifier.fireTestIgnored(fDescription);
    }
}