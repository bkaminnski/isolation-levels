package com.hclc.isolationlevels.page245lostupdatescompareandset.scenario2_areads_breads_aupdates_bupdates_acommits_bcommits;

import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetNonVersionedPageRepository;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.PAGE_NAME;

@Component
public class CompareAndSetScenario2NonVersionedCompareOnContent extends CompareAndSetScenario2 {

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
