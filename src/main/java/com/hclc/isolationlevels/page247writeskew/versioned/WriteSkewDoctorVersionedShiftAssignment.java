package com.hclc.isolationlevels.page247writeskew.versioned;

import com.hclc.isolationlevels.page247writeskew.WriteSkewAssignmentState;
import com.hclc.isolationlevels.page247writeskew.WriteSkewDoctor;

import javax.persistence.*;

import static com.hclc.isolationlevels.page247writeskew.WriteSkewAssignmentState.ACTIVE;
import static com.hclc.isolationlevels.page247writeskew.WriteSkewAssignmentState.CANCELLED;
import static javax.persistence.EnumType.STRING;

@Entity
public class WriteSkewDoctorVersionedShiftAssignment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private WriteSkewDoctor doctor;

    @ManyToOne
    private WriteSkewVersionedShift shift;

    @Enumerated(STRING)
    private WriteSkewAssignmentState state;

    public WriteSkewDoctorVersionedShiftAssignment() {
    }

    public WriteSkewDoctorVersionedShiftAssignment(WriteSkewDoctor doctor, WriteSkewVersionedShift shift) {
        this.doctor = doctor;
        this.shift = shift;
        this.state = ACTIVE;
    }


    public boolean isActive() {
        return state == ACTIVE;
    }

    public WriteSkewDoctor getDoctor() {
        return doctor;
    }

    public void cancel() {
        state = CANCELLED;
    }
}
