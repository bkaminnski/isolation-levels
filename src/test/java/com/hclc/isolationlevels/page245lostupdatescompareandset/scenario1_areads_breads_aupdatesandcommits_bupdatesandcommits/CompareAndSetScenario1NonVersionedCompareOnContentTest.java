package com.hclc.isolationlevels.page245lostupdatescompareandset.scenario1_areads_breads_aupdatesandcommits_bupdatesandcommits;

import com.hclc.isolationlevels.IsolationLevelsApplicationTests;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetNonVersionedPage;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetNonVersionedScenariosSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.CONTENT_A;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompareAndSetScenario1NonVersionedCompareOnContentTest extends IsolationLevelsApplicationTests {

    @Autowired
    private CompareAndSetNonVersionedScenariosSetup scenarioSetup;
    @Autowired
    private CompareAndSetScenario1NonVersionedCompareOnContent scenario;
    private ThreadPoolExecutor executor;

    @BeforeEach
    public void setupAccounts() {
        scenarioSetup.reset();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    }

    @Test
    public void scenario1NonVersionedCompareOnContentReadCommitted() throws ExecutionException, InterruptedException {
        CompareAndSetScenario1FlowControl flowControl = new CompareAndSetScenario1FlowControl();

        Future<?> transactionAFuture = executor.submit(() -> runTransactionAReadCommitted(flowControl));
        Future<?> transactionBFuture = executor.submit(() -> runTransactionBReadCommitted(flowControl));

        transactionAFuture.get();
        transactionBFuture.get();

        CompareAndSetNonVersionedPage pageAtTheEnd = (CompareAndSetNonVersionedPage) scenario.read();
        assertEquals(CONTENT_A, pageAtTheEnd.getContent());
    }

    @Test
    public void scenario1NonVersionedCompareOnContentRepeatableRead() throws ExecutionException, InterruptedException {
        CompareAndSetScenario1FlowControl flowControl = new CompareAndSetScenario1FlowControl();

        Future<?> transactionAFuture = executor.submit(() -> runTransactionARepeatableRead(flowControl));
        Future<?> transactionBFuture = executor.submit(() -> runTransactionBRepeatableRead(flowControl));

        transactionAFuture.get();
        assertThrows(CannotAcquireLockException.class, () -> unwrapException(transactionBFuture));

        CompareAndSetNonVersionedPage pageAtTheEnd = (CompareAndSetNonVersionedPage) scenario.read();
        assertEquals(CONTENT_A, pageAtTheEnd.getContent());
    }

    private void runTransactionAReadCommitted(CompareAndSetScenario1FlowControl flowControl) {
        scenario.runTransactionAReadCommitted(flowControl);
        flowControl.transactionAWasFinished();
    }

    private void runTransactionBReadCommitted(CompareAndSetScenario1FlowControl flowControl) {
        flowControl.waitUntilTransactionAIsBegan();
        scenario.runTransactionBReadCommitted(flowControl);
    }

    private void runTransactionARepeatableRead(CompareAndSetScenario1FlowControl flowControl) {
        scenario.runTransactionARepeatableRead(flowControl);
        flowControl.transactionAWasFinished();
    }

    private void runTransactionBRepeatableRead(CompareAndSetScenario1FlowControl flowControl) {
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