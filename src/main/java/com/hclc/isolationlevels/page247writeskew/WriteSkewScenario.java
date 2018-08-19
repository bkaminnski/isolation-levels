package com.hclc.isolationlevels.page247writeskew;

import com.hclc.isolationlevels.TransactionAbScenario;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static com.hclc.isolationlevels.page247writeskew.WriteSkewShift.ALICE;
import static com.hclc.isolationlevels.page247writeskew.WriteSkewShift.BOB;

public abstract class WriteSkewScenario implements TransactionAbScenario<WriteSkewFlowControl> {

    protected abstract WriteSkewShift read();

    protected abstract void removeDoctor(WriteSkewShift writeSkewShift, String name);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void runTransactionAReadCommitted(WriteSkewFlowControl flowControl) {
        runTransactionA(flowControl);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void runTransactionARepeatableRead(WriteSkewFlowControl flowControl) {
        runTransactionA(flowControl);
    }

    private void runTransactionA(WriteSkewFlowControl flowControl) {
        flowControl.transactionAWasBegan();
        WriteSkewShift writeSkewShift = read();
        flowControl.transactionAReadPage();
        flowControl.waitUntilTransactionBReadsPage();
        removeDoctor(writeSkewShift, ALICE);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void runTransactionBReadCommitted(WriteSkewFlowControl flowControl) {
        runTransactionB(flowControl);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void runTransactionBRepeatableRead(WriteSkewFlowControl flowControl) {
        runTransactionB(flowControl);
    }

    private void runTransactionB(WriteSkewFlowControl flowControl) {
        flowControl.waitUntilTransactionAReadsPage();
        WriteSkewShift writeSkewShift = read();
        flowControl.transactionBReadPage();
        flowControl.waitUntilTransactionAIsFinished();
        removeDoctor(writeSkewShift, BOB);
    }
}
