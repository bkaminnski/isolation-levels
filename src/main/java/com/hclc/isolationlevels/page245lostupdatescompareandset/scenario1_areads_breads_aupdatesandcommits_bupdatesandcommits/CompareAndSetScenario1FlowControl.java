package com.hclc.isolationlevels.page245lostupdatescompareandset.scenario1_areads_breads_aupdatesandcommits_bupdatesandcommits;

import com.hclc.isolationlevels.TransactionAbFlowControl;

import java.util.concurrent.CountDownLatch;

public class CompareAndSetScenario1FlowControl implements TransactionAbFlowControl {

    private final CountDownLatch transactionABegan;
    private final CountDownLatch transactionAReadPage;
    private final CountDownLatch transactionAFinished;
    private final CountDownLatch transactionBReadPage;

    public CompareAndSetScenario1FlowControl() {
        transactionABegan = new CountDownLatch(1);
        transactionAReadPage = new CountDownLatch(1);
        transactionAFinished = new CountDownLatch(1);
        transactionBReadPage = new CountDownLatch(1);
    }

    public void waitUntilTransactionAIsBegan() {
        System.out.println("waitUntilTransactionAIsBegan");
        try {
            transactionABegan.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void transactionAWasBegan() {
        System.out.println("transactionAWasBegan");
        transactionABegan.countDown();
    }

    public void waitUntilTransactionAReadsPage() {
        System.out.println("waitUntilTransactionAReadsPage");
        try {
            transactionAReadPage.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void transactionAReadPage() {
        System.out.println("transactionAReadPage");
        transactionAReadPage.countDown();
    }

    public void waitUntilTransactionAIsFinished() {
        System.out.println("waitUntilTransactionAIsFinished");
        try {
            transactionAFinished.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void transactionAWasFinished() {
        System.out.println("transactionAWasFinished");
        transactionAFinished.countDown();
    }

    public void waitUntilTransactionBReadsPage() {
        System.out.println("waitUntilTransactionBReadsPage");
        try {
            transactionBReadPage.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void transactionBReadPage() {
        System.out.println("transactionBReadPage");
        transactionBReadPage.countDown();
    }
}
