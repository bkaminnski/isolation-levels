package com.hclc.isolationlevels.page237repeatableread;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatableReadAccountRepository extends JpaRepository<RepeatableReadAccount, Long> {

    RepeatableReadAccount findByName(String name);
}
