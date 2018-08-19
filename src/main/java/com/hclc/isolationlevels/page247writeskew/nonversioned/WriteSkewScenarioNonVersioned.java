package com.hclc.isolationlevels.page247writeskew.nonversioned;

import com.hclc.isolationlevels.page247writeskew.WriteSkewDoctor;
import com.hclc.isolationlevels.page247writeskew.WriteSkewDoctorRepository;
import com.hclc.isolationlevels.page247writeskew.WriteSkewScenario;
import com.hclc.isolationlevels.page247writeskew.WriteSkewShift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.hclc.isolationlevels.page247writeskew.WriteSkewShift.START_DAY;

@Component
public class WriteSkewScenarioNonVersioned extends WriteSkewScenario {

    @Autowired
    private WriteSkewDoctorRepository doctorRepository;

    @Autowired
    private WriteSkewNonVersionedShiftRepository shiftRepository;

    @Autowired
    private WriteSkewDoctorNonVersionedShiftAssignmentRepository assignmentRepository;

    protected WriteSkewShift read() {
        return shiftRepository.findByStartDay(START_DAY);
    }

    protected void removeDoctor(WriteSkewShift shift, String name) {
        WriteSkewDoctor doctor = doctorRepository.findByName(name);
        WriteSkewNonVersionedShift nonVersionedShift = (WriteSkewNonVersionedShift) shift;
        nonVersionedShift.removeDoctor(doctor, assignmentRepository);
        shiftRepository.save(nonVersionedShift);
    }
}
