package com.hclc.isolationlevels.page247writeskew.versioned;

import com.hclc.isolationlevels.TransactionAbScenario;
import com.hclc.isolationlevels.page247writeskew.WriteSkewFlowControl;
import com.hclc.isolationlevels.page247writeskew.WriteSkewScenariosSetup;
import com.hclc.isolationlevels.page247writeskew.WriteSkewScenariosTest;
import com.hclc.isolationlevels.page247writeskew.WriteSkewShift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static com.hclc.isolationlevels.page247writeskew.WriteSkewShift.CAROL;

public class WriteSkewScenarioVersionedNoLockTest extends WriteSkewScenariosTest {

    @Autowired
    private WriteSkewScenariosSetup scenarioSetup;
    @Autowired
    private WriteSkewScenarioVersionedNoLock scenario;
    private ThreadPoolExecutor executor;

    @BeforeEach
    public void setupAccounts() {
        scenarioSetup.reset();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    }

    @Test
    public void writeSkewScenarioVersionedNoLockReadCommitted_invariantViolated() throws ExecutionException, InterruptedException {
        WriteSkewFlowControl flowControl = new WriteSkewFlowControl();

        Future<?> transactionAFuture = executor.submit(() -> runTransactionAReadCommitted(flowControl));
        Future<?> transactionBFuture = executor.submit(() -> runTransactionBReadCommitted(flowControl));

        transactionAFuture.get();
        transactionBFuture.get();

        WriteSkewShift shiftAtTheEnd = scenario.read();
        assertDoctorsOnShift(shiftAtTheEnd, CAROL);
    }

    @Test
    public void writeSkewScenarioVersionedNoLockRepeatableRead_invariantViolated() throws ExecutionException, InterruptedException {
        WriteSkewFlowControl flowControl = new WriteSkewFlowControl();

        Future<?> transactionAFuture = executor.submit(() -> runTransactionARepeatableRead(flowControl));
        Future<?> transactionBFuture = executor.submit(() -> runTransactionBRepeatableRead(flowControl));

        transactionAFuture.get();
        transactionBFuture.get();

        WriteSkewShift shiftAtTheEnd = scenario.read();
        assertDoctorsOnShift(shiftAtTheEnd, CAROL);
    }

    @Override
    protected TransactionAbScenario<WriteSkewFlowControl> getScenario() {
        return scenario;
    }
}