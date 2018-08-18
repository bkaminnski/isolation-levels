package com.hclc.isolationlevels.page245lostupdatescompareandset.scenario2_areads_breads_aupdates_bupdates_acommits_bcommits;

import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetVersionedPage;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetVersionedPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.PAGE_NAME;

@Component
public class CompareAndSetScenario2Versioned extends CompareAndSetScenario2 {

    @Autowired
    private CompareAndSetVersionedPageRepository pageRepository;

    @Override
    @Transactional
    CompareAndSetPage read() {
        return pageRepository.findByName(PAGE_NAME);
    }

    @Override
    void update(CompareAndSetPage page, String newContent) {
        CompareAndSetVersionedPage versionedPage = (CompareAndSetVersionedPage) page;
        versionedPage.setContent(newContent);
        pageRepository.saveAndFlush(versionedPage);
    }
}
