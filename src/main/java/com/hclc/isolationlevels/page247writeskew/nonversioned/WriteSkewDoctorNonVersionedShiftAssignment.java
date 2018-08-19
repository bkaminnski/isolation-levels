package com.hclc.isolationlevels.page247writeskew.nonversioned;

import com.hclc.isolationlevels.page247writeskew.WriteSkewAssignmentState;
import com.hclc.isolationlevels.page247writeskew.WriteSkewDoctor;

import javax.persistence.*;

import static com.hclc.isolationlevels.page247writeskew.WriteSkewAssignmentState.ACTIVE;
import static com.hclc.isolationlevels.page247writeskew.WriteSkewAssignmentState.CANCELLED;
import static javax.persistence.EnumType.STRING;

@Entity
public class WriteSkewDoctorNonVersionedShiftAssignment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private WriteSkewDoctor doctor;

    @ManyToOne
    private WriteSkewNonVersionedShift shift;

    @Enumerated(STRING)
    private WriteSkewAssignmentState state;

    public WriteSkewDoctorNonVersionedShiftAssignment() {
    }

    public WriteSkewDoctorNonVersionedShiftAssignment(WriteSkewDoctor doctor, WriteSkewNonVersionedShift shift) {
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
