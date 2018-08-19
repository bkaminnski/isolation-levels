package com.hclc.isolationlevels;

public interface TransactionAbScenario<F extends TransactionAbFlowControl> {

    void runTransactionAReadCommitted(F flowControl);

    void runTransactionBReadCommitted(F flowControl);

    void runTransactionARepeatableRead(F flowControl);

    void runTransactionBRepeatableRead(F flowControl);
}
