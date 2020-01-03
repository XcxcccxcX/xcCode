package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: huiyishe
 * @Date: 2020-01-01 16:52
 */
@FeignClient(value = "XC-SERVICE-MANAGE-CMS")
public interface CmsPageClient {

    //根据页面id查询页面信息 远程调用Cms 请求数据
    @GetMapping("/cms/page/get/{id}")//标识远程调用的http 方法类型
    public CmsPage findById(@PathVariable("id") String id);

    //添加页面 用于课程预览
    @PostMapping("/cms/page/save")
    CmsPageResult saveCmsPage(@RequestBody CmsPage cmsPage);

}
