package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsSiteControllerApi;
import com.xuecheng.api.cms.CmsSysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.CmsSiteService;
import com.xuecheng.manage_cms.service.CmsSysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Enzo Cotter on 2019/12/12.
 * @author xcx
 */
@RestController
@RequestMapping("/sys")
public class CmsSysDictionaryController implements CmsSysDictionaryControllerApi {

    @Autowired
    CmsSysDictionaryService cmsSysDictionaryService;

    @Override
    @GetMapping("/dictionary/get/{dType}")
    public SysDictionary getByType(@PathVariable("dType") String dType) {
        return cmsSysDictionaryService.getByType(dType);
    }
}
