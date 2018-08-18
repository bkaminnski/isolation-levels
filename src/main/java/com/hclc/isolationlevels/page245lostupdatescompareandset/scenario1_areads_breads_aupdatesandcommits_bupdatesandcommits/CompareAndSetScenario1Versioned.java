package com.hclc.isolationlevels.page245lostupdatescompareandset.scenario1_areads_breads_aupdatesandcommits_bupdatesandcommits;

import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetVersionedPage;
import com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetVersionedPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.PAGE_NAME;

@Component
public class CompareAndSetScenario1Versioned extends CompareAndSetScenario1 {

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
