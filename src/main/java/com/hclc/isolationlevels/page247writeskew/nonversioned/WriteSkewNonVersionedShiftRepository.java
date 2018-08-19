package com.hclc.isolationlevels.page247writeskew.nonversioned;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface WriteSkewNonVersionedShiftRepository extends JpaRepository<WriteSkewNonVersionedShift, Long> {

    WriteSkewNonVersionedShift findByStartDay(Date startDay);
}
