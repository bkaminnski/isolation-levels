package com.hclc.isolationlevels.page247writeskew.versioned;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriteSkewDoctorVersionedShiftAssignmentRepository extends JpaRepository<WriteSkewDoctorVersionedShiftAssignment, Long> {

}
