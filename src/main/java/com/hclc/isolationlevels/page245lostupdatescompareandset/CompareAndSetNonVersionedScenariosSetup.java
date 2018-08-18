package com.hclc.isolationlevels.page245lostupdatescompareandset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.INITIAL_CONTENT;
import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.PAGE_NAME;

@Component
public class CompareAndSetNonVersionedScenariosSetup {

    @Autowired
    private CompareAndSetNonVersionedPageRepository pageRepository;

    @Transactional
    public void reset() {
        pageRepository.deleteAll();
        CompareAndSetNonVersionedPage page = new CompareAndSetNonVersionedPage();
        page.setName(PAGE_NAME);
        page.setContent(INITIAL_CONTENT);
        pageRepository.save(page);
    }
}
