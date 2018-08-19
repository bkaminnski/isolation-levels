package com.hclc.isolationlevels;

public interface TransactionAbFlowControl {

    void waitUntilTransactionAIsBegan();

    void transactionAWasFinished();
}
