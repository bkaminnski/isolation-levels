package com.hclc.isolationlevels.page245lostupdatescompareandset.scenario2_areads_breads_aupdates_bupdates_acommits_bcommits;

import com.hclc.isolationlevels.IsolationLevelsApplicationTests;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetNonVersionedPage;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetVersionedScenariosSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.CONTENT_A;
import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.CONTENT_B;
import static org.junit.jupiter.api.Assertions.*;

public class CompareAndSetScenario2NonVersionedNoCompareTest extends IsolationLevelsApplicationTests {

    @Autowired
    private CompareAndSetVersionedScenariosSetup scenarioSetup;
    @Autowired
    private CompareAndSetScenario2NonVersionedNoCompare scenario;
    private ThreadPoolExecutor executor;

    @BeforeEach
    public void setupAccounts() {
        scenarioSetup.reset();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    }

    @Test
    public void scenario2NonVersionedNoCompareReadCommitted() throws ExecutionException, InterruptedException {
        CompareAndSetScenario2FlowControl flowControl = new CompareAndSetScenario2FlowControl();

        Future<?> transactionAFuture = executor.submit(() -> runTransactionAReadCommitted(flowControl));
        Future<?> transactionBFuture = executor.submit(() -> runTransactionBReadCommitted(flowControl));

        transactionAFuture.get();
        transactionBFuture.get();

        CompareAndSetNonVersionedPage pageAtTheEnd = (CompareAndSetNonVersionedPage) scenario.read();
        assertEquals(CONTENT_B, pageAtTheEnd.getContent());
        assertFalse(flowControl.transactionASawTransactionBUpdateComplete());
    }

    @Test
    public void scenario2NonVersionedNoCompareRepeatableRead() throws ExecutionException, InterruptedException {
        CompareAndSetScenario2FlowControl flowControl = new CompareAndSetScenario2FlowControl();

        Future<?> transactionAFuture = executor.submit(() -> runTransactionARepeatableRead(flowControl));
        Future<?> transactionBFuture = executor.submit(() -> runTransactionBRepeatableRead(flowControl));

        transactionAFuture.get();
        assertThrows(CannotAcquireLockException.class, () -> unwrapException(transactionBFuture));

        CompareAndSetNonVersionedPage pageAtTheEnd = (CompareAndSetNonVersionedPage) scenario.read();
        assertEquals(CONTENT_A, pageAtTheEnd.getContent());
        assertFalse(flowControl.transactionASawTransactionBUpdateComplete());
    }

    private void runTransactionAReadCommitted(CompareAndSetScenario2FlowControl flowControl) {
        scenario.runTransactionAReadCommitted(flowControl);
        flowControl.transactionAWasFinished();
    }

    private void runTransactionBReadCommitted(CompareAndSetScenario2FlowControl flowControl) {
        flowControl.waitUntilTransactionAIsBegan();
        scenario.runTransactionBReadCommitted(flowControl);
    }

    private void runTransactionARepeatableRead(CompareAndSetScenario2FlowControl flowControl) {
        scenario.runTransactionARepeatableRead(flowControl);
        flowControl.transactionAWasFinished();
    }

    private void runTransactionBRepeatableRead(CompareAndSetScenario2FlowControl flowControl) {
        flowControl.waitUntilTransactionAIsBegan();
        scenario.runTransactionBRepeatableRead(flowControl);
    }

    private void unwrapException(Future<?> transactionFuture) throws Throwable {
        try {
            transactionFuture.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }
}