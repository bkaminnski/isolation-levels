package com.hclc.isolationlevels.page247writeskew.versioned;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface WriteSkewVersionedShiftRepository extends JpaRepository<WriteSkewVersionedShift, Long> {

    WriteSkewVersionedShift findByStartDay(Date startDay);
}
