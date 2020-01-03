package com.xuecheng.manage_cms.controller;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPageService;
import com.xuecheng.manage_cms.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Enzo Cotter on 2019/12/12.
 * @author xcx
 */
@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    CmsPageService cmsPageService;


    @GetMapping("/cms/preview/{pageId}")
    public void getPageHtml(@PathVariable("pageId") String pageId) {
        //执行静态化
        String html = cmsPageService.getPageHtml(pageId);
        try {
            //通过response 对象将内容输出
            OutputStream outputStream = response.getOutputStream();
            response.setHeader("Content-type","text/html;charset=utf-8");
            outputStream.write(html.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
