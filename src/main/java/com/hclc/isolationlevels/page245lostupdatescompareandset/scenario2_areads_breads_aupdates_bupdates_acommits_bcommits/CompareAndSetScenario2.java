package com.hclc.isolationlevels.page245lostupdatescompareandset.scenario2_areads_breads_aupdates_bupdates_acommits_bcommits;

import com.hclc.isolationlevels.TransactionAbScenario;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.CONTENT_A;
import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.CONTENT_B;

public abstract class CompareAndSetScenario2 implements TransactionAbScenario<CompareAndSetScenario2FlowControl> {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void runTransactionAReadCommitted(CompareAndSetScenario2FlowControl flowControl) {
        runTransactionA(flowControl);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void runTransactionARepeatableRead(CompareAndSetScenario2FlowControl flowControl) {
        runTransactionA(flowControl);
    }

    private void runTransactionA(CompareAndSetScenario2FlowControl flowControl) {
        flowControl.transactionAWasBegan();
        CompareAndSetPage page = read();
        flowControl.transactionAReadPage();
        flowControl.waitUntilTransactionBReadsPage();
        update(page, CONTENT_A);
        flowControl.transactionAUpdatedPage();
        flowControl.waitUntilTransactionBUpdatesPage();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void runTransactionBReadCommitted(CompareAndSetScenario2FlowControl flowControl) {
        runTransactionB(flowControl);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void runTransactionBRepeatableRead(CompareAndSetScenario2FlowControl flowControl) {
        runTransactionB(flowControl);
    }

    private void runTransactionB(CompareAndSetScenario2FlowControl flowControl) {
        flowControl.waitUntilTransactionAReadsPage();
        CompareAndSetPage page = read();
        flowControl.transactionBReadPage();
        flowControl.waitUntilTransactionAUpdatesPage();
        update(page, CONTENT_B);
        flowControl.transactionBUpdatedPage();
        flowControl.waitUntilTransactionAIsFinished();
    }

    abstract CompareAndSetPage read();

    abstract void update(CompareAndSetPage page, String newContent);
}
