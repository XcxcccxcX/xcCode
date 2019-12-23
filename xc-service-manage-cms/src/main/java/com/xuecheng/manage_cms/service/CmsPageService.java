package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import javax.persistence.GeneratedValue;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;

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
        if (cmsPage==null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
            CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndPageWebPathAndSiteId(cmsPage.getPageName(),cmsPage.getPageWebPath(),cmsPage.getSiteId());
            //异常捕获
            if (cmsPage1!=null){
                ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
            }
                cmsPage.setPageId(null);
                cmsPageRepository.save(cmsPage);
                return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
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
            cmsById.setDataUrl(cmsPage.getDataUrl());
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

    //页面静态化方法

    /**
     *  静态化程序获取页面的DataUrl
     *  静态化程序远程请求DataUrl获取数据模型
     *  静态化程序获取页面的模板信息
     *  执行页面静态化
     */
    public String getPageHtml(String pageId){
        //静态化程序远程请求DataUrl获取数据模型
        Map modelByPageId = getModelByPageId(pageId);
        if (modelByPageId==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //静态化程序获取页面的模板信息
        String template = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //执行静态化
        String html = generateHtml(template, modelByPageId);
        return html;
    }



    //执行静态化
    private String generateHtml(String templateContent,Map model){
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        //配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板内容
        try {
            Template template = configuration.getTemplate("template");
            //调用api 静态化
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //静态化程序获取页面的模板信息
    private String getTemplateByPageId(String pageId){
        //取出页面的信息
        CmsPage cmsPage = this.findById(pageId);
        if (cmsPage==null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取模板id
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)){
            //模板id为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询模板信息
        Optional<CmsTemplate> byId = cmsTemplateRepository.findById(templateId);
        if (byId.isPresent()){
            CmsTemplate cmsTemplate = byId.get();
            //获取GirdFS 模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
        //根据文件id 查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));

        //打开一个下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建Gridresource对象,获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        //从流中取数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //静态化程序远程请求DataUrl获取数据模型
    private Map getModelByPageId(String pageId){
        //取出页面的信息
        CmsPage cmsPage = this.findById(pageId);
        if (cmsPage==null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)){
            //页面dataUrl 为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //通过restTemplate请求url 获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }
}
