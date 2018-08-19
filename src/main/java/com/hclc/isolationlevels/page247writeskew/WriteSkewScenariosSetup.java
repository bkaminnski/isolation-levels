package com.hclc.isolationlevels.page247writeskew;

import com.hclc.isolationlevels.page247writeskew.nonversioned.WriteSkewDoctorNonVersionedShiftAssignmentRepository;
import com.hclc.isolationlevels.page247writeskew.nonversioned.WriteSkewNonVersionedShift;
import com.hclc.isolationlevels.page247writeskew.nonversioned.WriteSkewNonVersionedShiftRepository;
import com.hclc.isolationlevels.page247writeskew.versioned.WriteSkewDoctorVersionedShiftAssignmentRepository;
import com.hclc.isolationlevels.page247writeskew.versioned.WriteSkewVersionedShift;
import com.hclc.isolationlevels.page247writeskew.versioned.WriteSkewVersionedShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class WriteSkewScenariosSetup {

    @Autowired
    private WriteSkewDoctorRepository doctorRepository;

    @Autowired
    private WriteSkewNonVersionedShiftRepository nonVersionedShiftRepository;

    @Autowired
    private WriteSkewDoctorNonVersionedShiftAssignmentRepository nonVersionedAssignmentRepository;

    @Autowired
    private WriteSkewVersionedShiftRepository versionedShiftRepository;

    @Autowired
    private WriteSkewDoctorVersionedShiftAssignmentRepository versionedAssignmentRepository;

    @Transactional
    public void reset() {
        nonVersionedAssignmentRepository.deleteAll();
        versionedAssignmentRepository.deleteAll();
        nonVersionedShiftRepository.deleteAll();
        versionedShiftRepository.deleteAll();
        doctorRepository.deleteAll();
        WriteSkewDoctor alice = new WriteSkewDoctor(WriteSkewShift.ALICE);
        doctorRepository.save(alice);
        WriteSkewDoctor bob = new WriteSkewDoctor(WriteSkewShift.BOB);
        doctorRepository.save(bob);
        WriteSkewDoctor carol = new WriteSkewDoctor(WriteSkewShift.CAROL);
        doctorRepository.save(carol);
        WriteSkewNonVersionedShift nonVersionedShift = new WriteSkewNonVersionedShift(WriteSkewShift.START_DAY);
        nonVersionedShift.addDoctor(alice, nonVersionedAssignmentRepository);
        nonVersionedShift.addDoctor(bob, nonVersionedAssignmentRepository);
        nonVersionedShift.addDoctor(carol, nonVersionedAssignmentRepository);
        nonVersionedShiftRepository.save(nonVersionedShift);
        WriteSkewVersionedShift versionedShift = new WriteSkewVersionedShift(WriteSkewShift.START_DAY);
        versionedShift.addDoctor(alice, versionedAssignmentRepository);
        versionedShift.addDoctor(bob, versionedAssignmentRepository);
        versionedShift.addDoctor(carol, versionedAssignmentRepository);
        versionedShiftRepository.save(versionedShift);
    }
}
