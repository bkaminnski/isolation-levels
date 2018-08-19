package com.hclc.isolationlevels.page247writeskew.nonversioned;

import com.hclc.isolationlevels.page247writeskew.WriteSkewDoctor;
import com.hclc.isolationlevels.page247writeskew.WriteSkewShift;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.EAGER;

@Entity
public class WriteSkewNonVersionedShift extends WriteSkewShift {

    @OneToMany(fetch = EAGER, mappedBy = "shift")
    private List<WriteSkewDoctorNonVersionedShiftAssignment> doctors = new ArrayList<>();

    public WriteSkewNonVersionedShift() {
    }

    public WriteSkewNonVersionedShift(Date startDay) {
        super(startDay);
    }

    public void addDoctor(WriteSkewDoctor doctor, WriteSkewDoctorNonVersionedShiftAssignmentRepository assignmentRepository) {
        WriteSkewDoctorNonVersionedShiftAssignment assignment = new WriteSkewDoctorNonVersionedShiftAssignment(doctor, this);
        assignmentRepository.save(assignment);
    }

    public void removeDoctor(WriteSkewDoctor doctor, WriteSkewDoctorNonVersionedShiftAssignmentRepository assignmentRepository) {
        protectInvariants(doctor);
        doctors.stream()
                .filter(WriteSkewDoctorNonVersionedShiftAssignment::isActive)
                .filter(a -> a.getDoctor().equals(doctor))
                .forEach(a -> {
                    a.cancel();
                    assignmentRepository.save(a);
                });
    }

    public Set<WriteSkewDoctor> getDoctors() {
        return doctors.stream()
                .filter(WriteSkewDoctorNonVersionedShiftAssignment::isActive)
                .map(WriteSkewDoctorNonVersionedShiftAssignment::getDoctor)
                .collect(Collectors.toSet());
    }

    private void protectInvariants(WriteSkewDoctor doctor) {
        Set<WriteSkewDoctor> doctors = getDoctors();
        doctors.remove(doctor);
        if (doctors.size() < MIN_DOCTORS_ON_SHIFT) {
            throw new IllegalStateException("Number of doctors on a shift must not be below " + MIN_DOCTORS_ON_SHIFT);
        }
    }
}
