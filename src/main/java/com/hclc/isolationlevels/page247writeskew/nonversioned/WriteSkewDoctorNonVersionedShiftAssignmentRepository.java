package com.hclc.isolationlevels.page247writeskew.nonversioned;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriteSkewDoctorNonVersionedShiftAssignmentRepository extends JpaRepository<WriteSkewDoctorNonVersionedShiftAssignment, Long> {

}
