package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author: huiyishe
 * @Date: 2019-12-23 15:50
 */

@Service
public class CmsConfigService {
    @Autowired
    CmsConfigRepository cmsConfigRepository;

    public CmsConfig getConfigById(String id){
        Optional<CmsConfig> optionalCmsConfig = cmsConfigRepository.findById(id);
        if (optionalCmsConfig.isPresent()){
            CmsConfig cmsConfig = optionalCmsConfig.get();
            return cmsConfig;
        }
        return null;
    }
}
