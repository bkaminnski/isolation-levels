package com.hclc.isolationlevels.page245lostupdatescompareandset.scenario1_areads_breads_aupdatesandcommits_bupdatesandcommits;

import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetNonVersionedPageRepository;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.PAGE_NAME;

@Component
public class CompareAndSetScenario1NonVersionedCompareOnContent extends CompareAndSetScenario1 {

    @Autowired
    private CompareAndSetNonVersionedPageRepository pageRepository;

    @Override
    @Transactional
    CompareAndSetPage read() {
        return pageRepository.findByName(PAGE_NAME);
    }

    @Override
    void update(CompareAndSetPage page, String newContent) {
        pageRepository.updateContentByIdAndContent(newContent, page.getId(), page.getContent());
    }
}
