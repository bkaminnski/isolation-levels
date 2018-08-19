package com.hclc.isolationlevels.page245lostupdatescompareandset.scenario1_areads_breads_aupdatesandcommits_bupdatesandcommits;

import com.hclc.isolationlevels.TransactionAbScenario;
import com.hclc.isolationlevels.TransactionAbTest;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetVersionedPage;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetVersionedScenariosSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.CONTENT_A;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompareAndSetScenario1VersionedTest extends TransactionAbTest<CompareAndSetScenario1FlowControl> {

    @Autowired
    private CompareAndSetVersionedScenariosSetup scenarioSetup;
    @Autowired
    private CompareAndSetScenario1Versioned scenario;
    private ThreadPoolExecutor executor;

    @BeforeEach
    public void setupAccounts() {
        scenarioSetup.reset();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    }

    @Test
    public void scenario1VersionedReadCommitted() throws ExecutionException, InterruptedException {
        CompareAndSetScenario1FlowControl flowControl = new CompareAndSetScenario1FlowControl();

        Future<?> transactionAFuture = executor.submit(() -> runTransactionAReadCommitted(flowControl));
        Future<?> transactionBFuture = executor.submit(() -> runTransactionBReadCommitted(flowControl));

        transactionAFuture.get();
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> unwrapException(transactionBFuture));

        CompareAndSetVersionedPage pageAtTheEnd = (CompareAndSetVersionedPage) scenario.read();
        assertEquals(CONTENT_A, pageAtTheEnd.getContent());
    }

    @Test
    public void scenario1VersionedRepeatableRead() throws ExecutionException, InterruptedException {
        CompareAndSetScenario1FlowControl flowControl = new CompareAndSetScenario1FlowControl();

        Future<?> transactionAFuture = executor.submit(() -> runTransactionARepeatableRead(flowControl));
        Future<?> transactionBFuture = executor.submit(() -> runTransactionBRepeatableRead(flowControl));

        transactionAFuture.get();
        assertThrows(CannotAcquireLockException.class, () -> unwrapException(transactionBFuture));

        CompareAndSetVersionedPage pageAtTheEnd = (CompareAndSetVersionedPage) scenario.read();
        assertEquals(CONTENT_A, pageAtTheEnd.getContent());
    }

    private void unwrapException(Future<?> transactionFuture) throws Throwable {
        try {
            transactionFuture.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Override
    protected TransactionAbScenario getScenario() {
        return scenario;
    }
}