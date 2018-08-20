package com.hclc.isolationlevels.page237repeatableread;

import com.hclc.isolationlevels.IsolationLevelsApplicationTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static com.hclc.isolationlevels.page237repeatableread.RepeatableReadFlowControl.withOptimisticLock;
import static com.hclc.isolationlevels.page237repeatableread.RepeatableReadFlowControl.withoutOptimisticLock;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RepeatableReadScenarioTest extends IsolationLevelsApplicationTests {

    @Autowired
    private RepeatableReadScenario scenario;
    private ThreadPoolExecutor executor;

    @BeforeEach
    public void setupAccounts() {
        scenario.reset();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    }

    @Test
    @DisplayName("When isolation level is read committed and optimistic lock is not applied, " +
            "should demonstrate nonrepeatable read anomaly.")
    public void whenReadCommittedAndNoOptimisticLock_shouldDemonstrateNonrepeatableReadAnomaly() throws ExecutionException, InterruptedException {
        RepeatableReadFlowControl flowControl = withoutOptimisticLock();
        Future<List<RepeatableReadAccount>> readBalanceFuture = runScenarioReadCommitted(flowControl);

        List<RepeatableReadAccount> accounts = readBalanceFuture.get();

        assertBalancesFor(accounts, 500, 600);
    }

    @Test
    @DisplayName("When isolation level is read committed and optimistic lock is applied, " +
            "should prevent nonrepeatable read anomaly by throwing optimistic lock exception.")
    public void whenReadCommittedAndOptimisticLock_shouldPreventNonrepeatableReadAnomalyByThrowingOptimisticLockException() {
        RepeatableReadFlowControl flowControl = withOptimisticLock();
        Future<List<RepeatableReadAccount>> readBalanceFuture = runScenarioReadCommitted(flowControl);

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> unwrapException(readBalanceFuture));
    }

    @Test
    @DisplayName("When isolation level is repeatable read and optimistic lock is not applied, " +
            "should prevent nonrepeatable read anomaly by reading both balances from before transaction.")
    public void whenRepeatableReadAndNoOptimisticLock_shouldPreventNonrepeatableReadAnomalyByReadingBothBalancesFromBeforeTransaction() throws ExecutionException, InterruptedException {
        RepeatableReadFlowControl flowControl = withoutOptimisticLock();
        Future<List<RepeatableReadAccount>> readBalanceFuture = runScenarioRepeatableRead(flowControl);

        List<RepeatableReadAccount> accounts = readBalanceFuture.get();

        assertBalancesFor(accounts, 500, 500);
    }

    @Test
    @DisplayName("When isolation level is repeatable read and optimistic lock is applied, " +
            "should prevent nonrepeatable read anomaly by reading both balances from before transaction ignoring optimistic lock.")
    public void whenRepeatableReadAndOptimisticLock_shouldPreventNonrepeatableReadAnomalyByReadingBothBalancesFromBeforeTransactionIgnoringOptimisticLock() throws ExecutionException, InterruptedException {
        RepeatableReadFlowControl flowControl = withOptimisticLock();
        Future<List<RepeatableReadAccount>> readBalanceFuture = runScenarioRepeatableRead(flowControl);

        List<RepeatableReadAccount> accounts = readBalanceFuture.get();

        assertBalancesFor(accounts, 500, 500);
    }

    private Future<List<RepeatableReadAccount>> runScenarioReadCommitted(RepeatableReadFlowControl flowControl) {
        Future<List<RepeatableReadAccount>> readBalanceFuture = executor.submit(() -> scenario.readBalanceReadCommitted(flowControl));
        executor.execute(() -> {
            flowControl.waitUntilAccountAIsFound();
            scenario.processTransactionReadCommitted();
            flowControl.transactionWasProcessed();
        });
        return readBalanceFuture;
    }

    private Future<List<RepeatableReadAccount>> runScenarioRepeatableRead(RepeatableReadFlowControl flowControl) {
        Future<List<RepeatableReadAccount>> readBalanceFuture = executor.submit(() -> scenario.readBalanceRepeatableRead(flowControl));
        executor.execute(() -> {
            flowControl.waitUntilAccountAIsFound();
            scenario.processTransactionRepeatableRead();
            flowControl.transactionWasProcessed();
        });
        return readBalanceFuture;
    }

    private void assertBalancesFor(List<RepeatableReadAccount> accounts, int... balances) {
        assertArrayEquals(balances, accounts.stream().mapToInt(a -> a.getBalance().intValue()).toArray());
    }

    private void unwrapException(Future<List<RepeatableReadAccount>> readBalanceFuture) throws Throwable {
        try {
            readBalanceFuture.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }
}