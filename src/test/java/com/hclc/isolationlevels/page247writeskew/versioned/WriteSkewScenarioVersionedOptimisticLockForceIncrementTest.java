package com.hclc.isolationlevels.page247writeskew.versioned;

import com.hclc.isolationlevels.TransactionAbScenario;
import com.hclc.isolationlevels.page247writeskew.WriteSkewFlowControl;
import com.hclc.isolationlevels.page247writeskew.WriteSkewScenariosSetup;
import com.hclc.isolationlevels.page247writeskew.WriteSkewScenariosTest;
import com.hclc.isolationlevels.page247writeskew.WriteSkewShift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static com.hclc.isolationlevels.page247writeskew.WriteSkewShift.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WriteSkewScenarioVersionedOptimisticLockForceIncrementTest extends WriteSkewScenariosTest {

    @Autowired
    private WriteSkewScenariosSetup scenarioSetup;
    @Autowired
    private WriteSkewScenarioVersionedOptimisticLockForceIncrement scenario;
    @Autowired
    private WriteSkewVersionedShiftRepository shiftRepository;

    private ThreadPoolExecutor executor;

    @BeforeEach
    public void setupAccounts() {
        scenarioSetup.reset();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    }

    @Test
    public void writeSkewScenarioVersionedNoLockReadCommitted_invariantPreserved() throws ExecutionException, InterruptedException {
        WriteSkewFlowControl flowControl = new WriteSkewFlowControl();

        Future<?> transactionAFuture = executor.submit(() -> runTransactionAReadCommitted(flowControl));
        Future<?> transactionBFuture = executor.submit(() -> runTransactionBReadCommitted(flowControl));

        transactionAFuture.get();
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> unwrapException(transactionBFuture));

        WriteSkewShift shiftAtTheEnd = shiftRepository.findByStartDay(START_DAY);
        assertDoctorsOnShift(shiftAtTheEnd, BOB, CAROL);
    }

    @Test
    public void writeSkewScenarioVersionedNoLockRepeatableRead_invariantPreserved() throws ExecutionException, InterruptedException {
        WriteSkewFlowControl flowControl = new WriteSkewFlowControl();

        Future<?> transactionAFuture = executor.submit(() -> runTransactionARepeatableRead(flowControl));
        Future<?> transactionBFuture = executor.submit(() -> runTransactionBRepeatableRead(flowControl));

        transactionAFuture.get();
        assertThrows(CannotAcquireLockException.class, () -> unwrapException(transactionBFuture));

        WriteSkewShift shiftAtTheEnd = shiftRepository.findByStartDay(START_DAY);
        assertDoctorsOnShift(shiftAtTheEnd, BOB, CAROL);
    }

    private void unwrapException(Future<?> transactionFuture) throws Throwable {
        try {
            transactionFuture.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Override
    protected TransactionAbScenario<WriteSkewFlowControl> getScenario() {
        return scenario;
    }
}