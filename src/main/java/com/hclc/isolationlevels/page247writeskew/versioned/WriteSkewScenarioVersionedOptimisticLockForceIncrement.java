package com.hclc.isolationlevels.page247writeskew.versioned;

import com.hclc.isolationlevels.page247writeskew.WriteSkewDoctor;
import com.hclc.isolationlevels.page247writeskew.WriteSkewDoctorRepository;
import com.hclc.isolationlevels.page247writeskew.WriteSkewScenario;
import com.hclc.isolationlevels.page247writeskew.WriteSkewShift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import static com.hclc.isolationlevels.page247writeskew.WriteSkewShift.START_DAY;

@Component
public class WriteSkewScenarioVersionedOptimisticLockForceIncrement extends WriteSkewScenario {

    @Autowired
    private WriteSkewDoctorRepository doctorRepository;

    @Autowired
    private WriteSkewVersionedShiftRepository shiftRepository;

    @Autowired
    private WriteSkewDoctorVersionedShiftAssignmentRepository assignmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    protected WriteSkewShift read() {
        WriteSkewVersionedShift shift = shiftRepository.findByStartDay(START_DAY);
        entityManager.lock(shift, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        return shift;
    }

    protected void removeDoctor(WriteSkewShift shift, String name) {
        WriteSkewDoctor doctor = doctorRepository.findByName(name);
        WriteSkewVersionedShift versionedShift = (WriteSkewVersionedShift) shift;
        versionedShift.removeDoctor(doctor, assignmentRepository);
        shiftRepository.save(versionedShift);
    }
}
