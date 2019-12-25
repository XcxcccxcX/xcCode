package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

/**
 * @author: huiyishe
 * @Date: 2019-12-25 16:37
 */
@Service
public class PageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    public void savePageToServerPath(String pageId){

        //根据pageId 查询到页面信息
        CmsPage cmsPage = this.findCmsPageByPageId(pageId);
        //获取cmsPage 对象中的 htmlFileId
        String htmlFileId  = cmsPage.getHtmlFileId();
        //根据htmlFileId 从GridFS 查询到html文件内容
        InputStream inputStream = this.findFileById(htmlFileId);
        if (inputStream==null){
           LOGGER.error("findFileById inputStream is null, htmlFileId:{}",htmlFileId);
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
            return;
        }
        //根据站点id 得到站点的物理路径
        CmsSite cmsSite = this.findCmsSiteBySiteId(cmsPage.getSiteId());
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        //页面的物理路径
        String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        File f  = new File(pagePath);
        if (!f.exists()){
            f.mkdir();
        }
        //将html 文件保存到服务器物理路径上
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(pagePath);
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //

    //根据htmlFileId 从GridFS 查询到html文件内容
    public InputStream findFileById(String htmlFileId){
        //文件对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        //打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //定义
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //根据pageId 查询到页面信息
    public CmsPage findCmsPageByPageId(String pageId){
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById(pageId);
        if (optionalCmsPage.isPresent()){
            return optionalCmsPage.get();
        }
        return null;
    }

    //根据站点id  查询到站点信息
    public CmsSite findCmsSiteBySiteId(String siteId){
        Optional<CmsSite> optionalCmsSite = cmsSiteRepository.findById(siteId);
        if (optionalCmsSite.isPresent()){
            return optionalCmsSite.get();
        }
        return null;
    }
}
