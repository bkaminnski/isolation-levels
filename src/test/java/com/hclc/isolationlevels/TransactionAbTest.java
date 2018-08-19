package com.hclc.isolationlevels;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class TransactionAbTest<F extends TransactionAbFlowControl> extends IsolationLevelsApplicationTests {

    protected abstract TransactionAbScenario<F> getScenario();

    protected void runTransactionAReadCommitted(F flowControl) {
        getScenario().runTransactionAReadCommitted(flowControl);
        flowControl.transactionAWasFinished();
    }

    protected void runTransactionBReadCommitted(F flowControl) {
        flowControl.waitUntilTransactionAIsBegan();
        getScenario().runTransactionBReadCommitted(flowControl);
    }

    protected void runTransactionARepeatableRead(F flowControl) {
        getScenario().runTransactionARepeatableRead(flowControl);
        flowControl.transactionAWasFinished();
    }

    protected void runTransactionBRepeatableRead(F flowControl) {
        flowControl.waitUntilTransactionAIsBegan();
        getScenario().runTransactionBRepeatableRead(flowControl);
    }

    protected void unwrapException(Future<?> transactionFuture) throws Throwable {
        try {
            transactionFuture.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }
}
