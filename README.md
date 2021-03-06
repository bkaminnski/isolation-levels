# Isolation levels demo

This project implements couple of examples from [Designing Data-Intensive Applications by Martin Kleppmann](https://dataintensive.net/), chapter 7, *Transactions*. Each scenario is presented in two isolation levels: read committed and repeatable read running against PostgreSQL database. Couple of locking modes are also used throughout examples, starting with no locks and (almost) plain entities, through implicit optimistic locks achieved by `@Version` annotation in JPA entities, to explicit locking on `EntityManager` with `LockModeType`: `OPTIMISTIC` and `OPTIMISTIC_FORCE_INCREMENT`. Each scenario uses a particular combination of isolation level and locking mode in step-by-step execution of two transactions running concurrently. In order to guarantee deterministic and repeatable behaviour, transactions flow is explicitely controlled by `CountDownLatches`. Each scenario is presented as a separate integration test running in Spring context.

## Page 237 - Repeatable Read

- [RepeatableReadScenarioTest](https://github.com/bkaminnski/isolation-levels/blob/master/src/test/java/com/hclc/isolationlevels/page237repeatableread/RepeatableReadScenarioTest.java)
    - read committed, no optimistic lock - should demonstrate nonrepeatable read anomaly
    - read committed, optimistic lock - should prevent nonrepeatable read anomaly by throwing optimistic lock exception
    - repeatable read, no optimistic lock - should prevent nonrepeatable read anomaly by reading both balances from before transaction
    - repeatable read, optimistic lock - should prevent nonrepeatable read anomaly by reading both balances from before transaction ignoring optimistic lock

## Page 245 - Lost Updates

### Scenario 1: A reads, B reads, A updates and commits, B updates and commits

- [CompareAndSetScenario1NonVersionedNoCompareTest](https://github.com/bkaminnski/isolation-levels/blob/master/src/test/java/com/hclc/isolationlevels/page245lostupdatescompareandset/scenario1_areads_breads_aupdatesandcommits_bupdatesandcommits/CompareAndSetScenario1NonVersionedNoCompareTest.java)
    - scenario 1, non versioned, no compare, read committed - update was lost - no mechanism could have prevented it
    - scenario 1, non versioned, no compare, repeatable read - lost update was prevented due to failure to acquire lock in database

- [CompareAndSetScenario1NonVersionedCompareOnContentTest](https://github.com/bkaminnski/isolation-levels/blob/master/src/test/java/com/hclc/isolationlevels/page245lostupdatescompareandset/scenario1_areads_breads_aupdatesandcommits_bupdatesandcommits/CompareAndSetScenario1NonVersionedCompareOnContentTest.java)
    - scenario 1, non versioned, compare on content, read committed - lost update was prevented due to compare after A committed
    - scenario 1, non versioned, compare on content, repeatable read - lost update was prevented due to failure to acquire lock in database

- [CompareAndSetScenario1VersionedTest](https://github.com/bkaminnski/isolation-levels/blob/master/src/test/java/com/hclc/isolationlevels/page245lostupdatescompareandset/scenario1_areads_breads_aupdatesandcommits_bupdatesandcommits/CompareAndSetScenario1VersionedTest.java)
    - scenario 1, versioned, read committed - lost update was prevented due to optimistic lock
    - scenario 1, versioned, repeatable read - lost update was prevented due to failure to acquire lock in database

### Scenario 2: A reads, B reads, A updates, B updates, A commits, B commits

- [CompareAndSetScenario2NonVersionedNoCompareTest](https://github.com/bkaminnski/isolation-levels/blob/master/src/test/java/com/hclc/isolationlevels/page245lostupdatescompareandset/scenario2_areads_breads_aupdates_bupdates_acommits_bcommits/CompareAndSetScenario2NonVersionedNoCompareTest.java)
    - scenario 2, non versioned, no compare, read committed - update was lost - and B kindly waited until A committed preventing dirty write 
    - scenario 2, non versioned, no compare, repeatable read - lost update was prevented due to failure to acquire lock in database - and B kindly waited until A committed preventing dirty write

- [CompareAndSetScenario2NonVersionedCompareOnContentTest](https://github.com/bkaminnski/isolation-levels/blob/master/src/test/java/com/hclc/isolationlevels/page245lostupdatescompareandset/scenario2_areads_breads_aupdates_bupdates_acommits_bcommits/CompareAndSetScenario2NonVersionedCompareOnContentTest.java)
    - scenario 2, non versioned, compare on content, read committed - update was lost because B compared before A committed making comparison useless - and B kindly waited until A committed preventing dirty write
    - scenario 2, non versioned, compare on content, repeatable read - lost update was prevented due to failure to acquire lock in database - and B kindly waited until A committed preventing dirty write

- [CompareAndSetScenario2VersionedTest](https://github.com/bkaminnski/isolation-levels/blob/master/src/test/java/com/hclc/isolationlevels/page245lostupdatescompareandset/scenario2_areads_breads_aupdates_bupdates_acommits_bcommits/CompareAndSetScenario2VersionedTest.java)
    - scenario 2, versioned, read committed - lost update was prevented due to optimistic lock - and B kindly waited until A committed preventing dirty write
    - scenario 2, versioned, repeatable read - lost update was prevented due to failure to acquire lock in database - and B kindly waited until A committed preventing dirty write

## Page 247 - Write Skew

- [WriteSkewScenarioNonVersionedTest](https://github.com/bkaminnski/isolation-levels/blob/master/src/test/java/com/hclc/isolationlevels/page247writeskew/nonversioned/WriteSkewScenarioNonVersionedTest.java)
    - write skew scenario, non versioned, read committed - invariant violated
    - write skew scenario, non versioned, repeatable read - invariant violated

- [WriteSkewScenarioVersionedNoExplicitLockTest](https://github.com/bkaminnski/isolation-levels/blob/master/src/test/java/com/hclc/isolationlevels/page247writeskew/versioned/WriteSkewScenarioVersionedNoLockTest.java)
    - write skew scenario, versioned, no explicit lock, read committed - invariant violated
    - write skew scenario, versioned, no explicit lock, repeatable read - invariant violated

- [WriteSkewScenarioVersionedOptimisticLockTest](https://github.com/bkaminnski/isolation-levels/blob/master/src/test/java/com/hclc/isolationlevels/page247writeskew/versioned/WriteSkewScenarioVersionedOptimisticLockTest.java)
    - write skew scenario, versioned, optimistic lock, read committed - invariant violated
    - write skew scenario, versioned, optimistic lock, repeatable read - invariant violated

- [WriteSkewScenarioVersionedOptimisticLockForceIncrementTest](https://github.com/bkaminnski/isolation-levels/blob/master/src/test/java/com/hclc/isolationlevels/page247writeskew/versioned/WriteSkewScenarioVersionedOptimisticLockForceIncrementTest.java)
    - write skew scenario, versioned, optimistic lock force increment, read committed - invariant preserved due to explicit optimistic lock
    - write skew scenario, versioned, optimistic lock force increment, repeatable read - invariant preserved due to failure to acquire lock in database - optimistic lock being only excuse for update