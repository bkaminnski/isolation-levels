package com.hclc.isolationlevels.page245lostupdatescompareandset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.INITIAL_CONTENT;
import static com.hclc.isolationlevels.page245lostupdatescompareandset.CompareAndSetPage.PAGE_NAME;

@Component
public class CompareAndSetVersionedScenariosSetup {

    @Autowired
    private CompareAndSetVersionedPageRepository pageRepository;

    @Transactional
    public void reset() {
        pageRepository.deleteAll();
        CompareAndSetVersionedPage page = new CompareAndSetVersionedPage();
        page.setName(PAGE_NAME);
        page.setContent(INITIAL_CONTENT);
        pageRepository.save(page);
    }
}
