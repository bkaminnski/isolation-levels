package com.hclc.isolationlevels.page237repeatableread;

import java.util.concurrent.CountDownLatch;

public class RepeatableReadFlowControl {

    private final CountDownLatch accountAFound;
    private final CountDownLatch transactionProcessed;
    private final boolean applyOptimisticLock;

    private RepeatableReadFlowControl(boolean applyOptimisticLock) {
        this.applyOptimisticLock = applyOptimisticLock;
        accountAFound = new CountDownLatch(1);
        transactionProcessed = new CountDownLatch(1);
    }

    public static RepeatableReadFlowControl withOptimisticLock() {
        return new RepeatableReadFlowControl(true);
    }

    public static RepeatableReadFlowControl withoutOptimisticLock() {
        return new RepeatableReadFlowControl(false);
    }

    public void waitUntilAccountAIsFound() {
        try {
            accountAFound.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void accountAWasFound() {
        accountAFound.countDown();
    }

    public void waitUntilTransactionIsProcessed() {
        try {
            transactionProcessed.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void transactionWasProcessed() {
        transactionProcessed.countDown();
    }

    public boolean applyOptimisticLock() {
        return applyOptimisticLock;
    }
}
