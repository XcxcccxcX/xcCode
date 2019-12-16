package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * Created by Enzo Cotter on 2019/12/12.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DaoRepository {
    @Autowired
    CmsPageRepository cmsPageRepository;

    @Test
    public void findAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void findAllPage(){
        int page =0 ;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    public void testUpdate(){
        Optional<CmsPage> cmsPageRepositoryById = cmsPageRepository.findById("5d621d030484d86994f579a9");
        if (cmsPageRepositoryById.isPresent()){
            CmsPage cmsPage = cmsPageRepositoryById.get();
            cmsPage.setPageName("2");
            cmsPageRepository.save(cmsPage);
        }
    }
}
