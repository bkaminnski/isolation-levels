package com.hclc.isolationlevels.page245lostupdatescompareandset.scenario1_areads_breads_aupdatesandcommits_bupdatesandcommits;

import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.CONTENT_A;
import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.CONTENT_B;

public abstract class CompareAndSetScenario1 {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void runTransactionAReadCommitted(CompareAndSetScenario1FlowControl flowControl) {
        runTransactionA(flowControl);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void runTransactionARepeatableRead(CompareAndSetScenario1FlowControl flowControl) {
        runTransactionA(flowControl);
    }

    private void runTransactionA(CompareAndSetScenario1FlowControl flowControl) {
        flowControl.transactionAWasBegan();
        CompareAndSetPage page = read();
        flowControl.transactionAReadPage();
        flowControl.waitUntilTransactionBReadsPage();
        update(page, CONTENT_A);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void runTransactionBReadCommitted(CompareAndSetScenario1FlowControl flowControl) {
        runTransactionB(flowControl);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void runTransactionBRepeatableRead(CompareAndSetScenario1FlowControl flowControl) {
        runTransactionB(flowControl);
    }

    private void runTransactionB(CompareAndSetScenario1FlowControl flowControl) {
        flowControl.waitUntilTransactionAReadsPage();
        CompareAndSetPage page = read();
        flowControl.transactionBReadPage();
        flowControl.waitUntilTransactionAIsFinished();
        update(page, CONTENT_B);
    }

    abstract CompareAndSetPage read();

    abstract void update(CompareAndSetPage page, String newContent);
}
