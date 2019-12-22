package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.GeneratedValue;
import java.util.List;
import java.util.Optional;

/**
 * Created by Enzo Cotter on 2019/12/16.
 */
@Service
public class CmsPageService {
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        //判断查询条件
        if (queryPageRequest==null){
            queryPageRequest = new QueryPageRequest();
        }
        if (page <= 0){
            page = 1;
        }
        if (size <= 0){
            size = 10;
        }
        page = page - 1;
        Pageable pageable = PageRequest.of(page,size);
        //条件对象
        CmsPage cmsPage = new CmsPage();
        //定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //判断传来的参数不为空  传入条件对象中
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        Example example = Example.of(cmsPage,exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        if (all != null) {
            QueryResult queryResult = new QueryResult();
            queryResult.setList(all.getContent());
            queryResult.setTotal(all.getTotalElements());
            QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
            return queryResponseResult;
        }
        return null;
    }

    //添加Cms 页面
    public CmsPageResult addCmsPage(CmsPage cmsPage) {
        if (cmsPage!=null){
            CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndPageWebPathAndSiteId(cmsPage.getPageName(),cmsPage.getPageWebPath(),cmsPage.getSiteId());
            if (cmsPage1==null){
                cmsPage.setPageId(null);
                cmsPageRepository.save(cmsPage);
                return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
            }
            return new CmsPageResult(CommonCode.FAIL,null);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    //根据id 查询页面
    public CmsPage findById(String id){
        if (StringUtils.isNotEmpty(id)||id != null) {
         Optional<CmsPage> cmsPage1 =  cmsPageRepository.findById(id);
         if (cmsPage1.isPresent()){
             CmsPage cmsPage = cmsPage1.get();
             return  cmsPage;
         }
         return null;
        }
        return null;
    }

    //修改页面
    public CmsPageResult editCmsPage(String id,CmsPage cmsPage){
        CmsPage cmsById = this.findById(id);
        if (cmsById!=null){
            cmsById.setTemplateId(cmsPage.getTemplateId());
            cmsById.setSiteId(cmsPage.getSiteId());
            cmsById.setPageAliase(cmsPage.getPageAliase());
            cmsById.setPageName(cmsPage.getPageName());
            cmsById.setPageWebPath(cmsPage.getPageWebPath());
            cmsById.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            CmsPage cmsPage1 = cmsPageRepository.save(cmsById);
            if (cmsPage1!=null){
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, cmsById);
                return cmsPageResult;
            }
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    //删除页面
    public ResponseResult deleteById(String id) {
        Optional<CmsPage> cmsPageById = cmsPageRepository.findById(id);
        if (cmsPageById.isPresent()){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}
