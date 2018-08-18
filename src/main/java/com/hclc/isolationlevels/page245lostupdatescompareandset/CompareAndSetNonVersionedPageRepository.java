package com.hclc.isolationlevels.page245lostupdatescompareandset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompareAndSetNonVersionedPageRepository extends JpaRepository<CompareAndSetNonVersionedPage, Long> {

    CompareAndSetNonVersionedPage findByName(String name);

    @Modifying(flushAutomatically = true)
    @Query("update CompareAndSetNonVersionedPage p set p.content = :contentToSet where p.id = :id and p.content = :contentToCompare")
    void updateContentByIdAndContent(@Param("contentToSet") String contentToSet, @Param("id") Long id, @Param("contentToCompare") String contentToCompare);

    @Modifying(flushAutomatically = true)
    @Query("update CompareAndSetNonVersionedPage p set p.content = :contentToSet where p.id = :id")
    void updateContentById(@Param("contentToSet") String contentToSet, @Param("id") Long id);
}
