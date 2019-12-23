package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author: huiyishe
 * @Date: 2019-12-23 10:13
 */

@RequestMapping("/freemarker")
@Controller //使用controller   RestController 返回的是json 数据
public class FreemarkerController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/banner")
    public String index_banner(Map<String,Object> map){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f",Map.class);
         Map body = forEntity.getBody();
        map.putAll(body);
        return "index_banner";
    }

    @RequestMapping("test1")
    public String test1(Map<String, Object> map){
        map.put("name","xxx");

        Student student1 = new Student();
        student1.setName("小明");
        student1.setAge(18);
        student1.setMoney(1000.86f);
        student1.setBirthday(new Date());
        Student student2 = new Student();
        student2.setName("小红");
        student2.setAge(19);
        student2.setMoney(200.1f);
        student2.setBirthday(new Date());

        //朋友列表
        List<Student> friends = new ArrayList<Student>();
        friends.add(student1);
        //第二名同学设置 朋友列表
        student2.setFriends(friends);
        //第二名同学设置 最好朋友
        student2.setBestFriend(student1);

        //学生列表
        List<Student> stus = new ArrayList();
        stus.add(student1);
        stus.add(student2);

        //将学生列表放在 数据模型中
        map.put("stus",stus);

        //准备map数据
        HashMap<String,Student> stuMap = new HashMap<String,Student>();
        stuMap.put("stu1",student1);
        stuMap.put("stu2",student2);

        //向数据模型中放数据
        map.put("stu1",student1);

        //向数据模型中放map 数据
        map.put("stuMap",stuMap);

        //对应  .ftl
        return "test01";
    }
}
