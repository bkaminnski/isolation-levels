package com.hclc.isolationlevels.page245lostupdatescompareandset;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompareAndSetVersionedPageRepository extends JpaRepository<CompareAndSetVersionedPage, Long> {

    CompareAndSetVersionedPage findByName(String name);
}
