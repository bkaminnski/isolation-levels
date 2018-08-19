package com.hclc.isolationlevels.page247writeskew;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

@MappedSuperclass
public abstract class WriteSkewShift {

    public static final String ALICE = "Alice";
    public static final String BOB = "Bob";
    public static final String CAROL = "Carol";
    public static final String START_DAY_ISO8601 = "2018-08-19";
    public static final Date START_DAY = Date.from(LocalDate.parse(START_DAY_ISO8601).atStartOfDay(ZoneId.systemDefault()).toInstant());

    protected static final int MIN_DOCTORS_ON_SHIFT = 2;

    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date startDay;

    public WriteSkewShift() {
    }

    public WriteSkewShift(Date startDay) {
        this.startDay = startDay;
    }

    public abstract Set<WriteSkewDoctor> getDoctors();
}
