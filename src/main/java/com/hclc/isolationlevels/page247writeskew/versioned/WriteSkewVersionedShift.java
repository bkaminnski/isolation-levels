package com.hclc.isolationlevels.page247writeskew.versioned;

import com.hclc.isolationlevels.page247writeskew.WriteSkewDoctor;
import com.hclc.isolationlevels.page247writeskew.WriteSkewShift;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.EAGER;

@Entity
public class WriteSkewVersionedShift extends WriteSkewShift {

    @OneToMany(fetch = EAGER, mappedBy = "shift")
    private List<WriteSkewDoctorVersionedShiftAssignment> doctors = new ArrayList<>();

    @Version
    private int version;

    public WriteSkewVersionedShift() {
    }

    public WriteSkewVersionedShift(Date startDay) {
        super(startDay);
    }

    public void addDoctor(WriteSkewDoctor doctor, WriteSkewDoctorVersionedShiftAssignmentRepository assignmentRepository) {
        WriteSkewDoctorVersionedShiftAssignment assignment = new WriteSkewDoctorVersionedShiftAssignment(doctor, this);
        assignmentRepository.save(assignment);
    }

    public void removeDoctor(WriteSkewDoctor doctor, WriteSkewDoctorVersionedShiftAssignmentRepository assignmentRepository) {
        protectInvariants(doctor);
        doctors.stream()
                .filter(WriteSkewDoctorVersionedShiftAssignment::isActive)
                .filter(a -> a.getDoctor().equals(doctor))
                .forEach(a -> {
                    a.cancel();
                    assignmentRepository.save(a);
                });
    }

    public Set<WriteSkewDoctor> getDoctors() {
        return doctors.stream()
                .filter(WriteSkewDoctorVersionedShiftAssignment::isActive)
                .map(WriteSkewDoctorVersionedShiftAssignment::getDoctor)
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
