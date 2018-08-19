package com.hclc.isolationlevels.page247writeskew;

import com.hclc.isolationlevels.TransactionAbTest;
import org.junit.jupiter.api.Assertions;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public abstract class WriteSkewScenariosTest extends TransactionAbTest<WriteSkewFlowControl> {

    protected void assertDoctorsOnShift(WriteSkewShift shiftAtTheEnd, String... doctors) {
        Set<String> actual = shiftAtTheEnd.getDoctors().stream().map(WriteSkewDoctor::getName).collect(toSet());
        Set<String> expected = Stream.of(doctors).collect(toSet());
        Assertions.assertEquals(expected.size(), expected.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }
}