package com.hclc.isolationlevels.page247writeskew;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriteSkewDoctorRepository extends JpaRepository<WriteSkewDoctor, Long> {

    WriteSkewDoctor findByName(String name);
}
