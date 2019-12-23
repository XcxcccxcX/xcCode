package com.xuecheng.test.freemarker;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author: huiyishe
 * @Date: 2019-12-23 11:51
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerTest {

    //测试静态化, 基于ftl文件生成html文件
    @Test
    public void testGenerateHtml() throws IOException, TemplateException {
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //定义模板
        //得到classPath路径
        String classPath = this.getClass().getResource("/").getPath();
        //定义模板路径
        configuration.setDirectoryForTemplateLoading(new File(classPath+"/templates/"));
        //获取模板文件内容
        Template template = configuration.getTemplate("test01.ftl");
        //定义数据模型
        Map map = getMap();
        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template,map);

        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("F:/test01.html"));
        //输出文件
        IOUtils.copy(inputStream,fileOutputStream);
        inputStream.close();
        fileOutputStream.close();
    }


    /**
     * 基于模板文件的内容(字符串)生成html文件
     */
    @Test
    public void testGenerateHtmlByString() throws IOException, TemplateException {
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //定义模板
        //模板内容
       String templateString = ""+
       "<html>\n"+
       "   <head></head>\n"+
       "   <body>\n"+
       "   名称: ${name}\n"+
       "   </body>\n"+
       "</html>";

        //使用一个模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateString);
        //再配置中设置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板内容
        Template template = configuration.getTemplate("template","utf-8");

        //定义数据模型
        Map map = getMap();
        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template,map);

        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("F:/test01.html"));
        //输出文件
        IOUtils.copy(inputStream,fileOutputStream);
        inputStream.close();
        fileOutputStream.close();
    }

    //获取数据模型
    public Map getMap(){
        Map map = new HashMap<>();
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

        return  map;
    }
}
