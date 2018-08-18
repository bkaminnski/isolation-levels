package com.hclc.isolationlevels.page245lostupdatescompareandset.scenario2_areads_breads_aupdates_bupdates_acommits_bcommits;

import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class CompareAndSetScenario2FlowControl {

    private final CountDownLatch transactionABegan;
    private final CountDownLatch transactionAReadPage;
    private final CountDownLatch transactionAUpdatedPage;
    private final CountDownLatch transactionAFinished;
    private final CountDownLatch transactionBReadPage;
    private final CountDownLatch transactionBUpdatedPage;
    private boolean transactionASawTransactionBUpdateComplete;

    public CompareAndSetScenario2FlowControl() {
        transactionABegan = new CountDownLatch(1);
        transactionAReadPage = new CountDownLatch(1);
        transactionAUpdatedPage = new CountDownLatch(1);
        transactionAFinished = new CountDownLatch(1);
        transactionBReadPage = new CountDownLatch(1);
        transactionBUpdatedPage = new CountDownLatch(1);
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

    public void waitUntilTransactionAUpdatesPage() {
        System.out.println("waitUntilTransactionAUpdatesPage");
        try {
            transactionAUpdatedPage.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void transactionAUpdatedPage() {
        System.out.println("transactionAUpdatedPage");
        transactionAUpdatedPage.countDown();
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

    public void waitUntilTransactionBUpdatesPage() {
        System.out.println("waitUntilTransactionBUpdatesPage");
        try {
            transactionASawTransactionBUpdateComplete = transactionBUpdatedPage.await(200, MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void transactionBUpdatedPage() {
        System.out.println("transactionBUpdatedPage");
        transactionBUpdatedPage.countDown();
    }

    public boolean transactionASawTransactionBUpdateComplete() {
        return transactionASawTransactionBUpdateComplete;
    }
}
