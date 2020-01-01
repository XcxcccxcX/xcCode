package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbno {

    @Autowired
    RestTemplate restTemplate;
    @Test
    public void testRibbon() {
        //确定要获取的服务名称
        String serviceId = "XC-SERVICE-MANAGE-CMS";
        //Ribbon客户端 从eurekaServer中获取服务列表
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity("http://"+serviceId+"/cms/page/get/5a754adf6abb500ad05688d9", Map.class);
        Map map = responseEntity.getBody();
        System.out.println(map);
    }
}
