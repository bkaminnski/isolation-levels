package com.hclc.isolationlevels.page237repeatableread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class RepeatableReadExample {

    private static final String ACCOUNT_A = "A";
    private static final int ACCOUNT_A_INITIAL_AMOUNT = 500;
    private static final String ACCOUNT_B = "B";
    private static final int ACCOUNT_B_INITIAL_AMOUNT = 500;
    private static final int TRANSFERRED_AMOUNT = 100;

    @Autowired
    private RepeatableReadAccountRepository accountRepository;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void reset() {
        accountRepository.deleteAll();
        RepeatableReadAccount account = new RepeatableReadAccount();
        account.setName(ACCOUNT_A);
        account.setBalance(new BigDecimal(ACCOUNT_A_INITIAL_AMOUNT));
        accountRepository.save(account);
        account = new RepeatableReadAccount();
        account.setName(ACCOUNT_B);
        account.setBalance(new BigDecimal(ACCOUNT_B_INITIAL_AMOUNT));
        accountRepository.save(account);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<RepeatableReadAccount> readBalanceReadCommitted(RepeatableReadFlowControl flowControl) {
        return readBalance(flowControl);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<RepeatableReadAccount> readBalanceRepeatableRead(RepeatableReadFlowControl flowControl) {
        return readBalance(flowControl);
    }

    private List<RepeatableReadAccount> readBalance(RepeatableReadFlowControl flowControl) {
        RepeatableReadAccount a = accountRepository.findByName(ACCOUNT_A);
        if (flowControl.applyOptimisticLock()) {
            em.lock(a, LockModeType.OPTIMISTIC);
        }
        flowControl.accountAWasFound();
        flowControl.waitUntilTransactionIsProcessed();
        RepeatableReadAccount b = accountRepository.findByName(ACCOUNT_B);
        return Stream.of(a, b).collect(toList());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void processTransactionReadCommitted() {
        processTransaction();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processTransactionRepeatableRead() {
        processTransaction();
    }

    private void processTransaction() {
        RepeatableReadAccount from = accountRepository.findByName(ACCOUNT_A);
        RepeatableReadAccount to = accountRepository.findByName(ACCOUNT_B);
        from.setBalance(from.getBalance().subtract(new BigDecimal(TRANSFERRED_AMOUNT)));
        to.setBalance(to.getBalance().add(new BigDecimal(TRANSFERRED_AMOUNT)));
    }
}
