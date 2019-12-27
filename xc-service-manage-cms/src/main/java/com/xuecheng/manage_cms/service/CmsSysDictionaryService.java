package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsSysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Enzo Cotter on 2019/12/16.
 */
@Service
public class CmsSysDictionaryService {
    @Autowired
    CmsSysDictionaryRepository cmsSysDictionaryRepository;

    public SysDictionary getByType(String dType) {
        if (dType==""|| StringUtils.isEmpty(dType)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        SysDictionary sysDictionary = cmsSysDictionaryRepository.findByDType(dType);
        System.out.println(sysDictionary);
        return sysDictionary;
    }
    //查询站点

}
