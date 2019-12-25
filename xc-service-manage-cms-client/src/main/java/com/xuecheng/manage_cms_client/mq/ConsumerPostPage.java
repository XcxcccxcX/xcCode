package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听mq 接收页面发布消息
 *
 * @author: huiyishe
 * @Date: 2019-12-25 17:21
 */
@Component
public class ConsumerPostPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) {
        //解析消息
        Map map = JSON.parseObject(msg, Map.class);
        //获取 消息中的pageId
        String pageId = (String) map.get("pageId");
        CmsPage cmsPage = pageService.findCmsPageByPageId(pageId);
        if (cmsPage == null){
            LOGGER.error("recevie postpage message,cmsPage is null, pageId:{}",pageId);
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //开始调用service 方法  将页面从GridFS中下载到服务器
        pageService.savePageToServerPath(pageId);
    }
}
