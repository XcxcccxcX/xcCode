package com.xuecheng.search;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/***
 * @author  xxx
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    RestClient restClient;

    @Test
    public void createIndex(){
        //创建索引对象 设置索引名称
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        //设置索引参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards",1)
                                                      .put("number_of_replicas",0));
        //指定映射
        createIndexRequest.mapping("doc","{\n" +
                "    \"properties\": {\n" +
                "        \"description\": {\n" +
                "            \"type\": \"text\",\n" +
                "              \"analyzer\":\"ik_max_word\",\n" +
                "            \"search_analyzer\":\"ik_smart\"\n" +
                "            \n" +
                "        },\n" +
                "        \"name\": {\n" +
                "            \"type\": \"text\",\n" +
                "            \"analyzer\":\"ik_max_word\",\n" +
                "            \"search_analyzer\":\"ik_smart\"\n" +
                "        },\n" +
                "        \"studymodel\": {\n" +
                "            \"type\": \"text\"\n" +
                "        },\n" +
                "         \"pic\": {\n" +
                "            \"type\": \"text\",\n" +
                "            \"index\":false\n" +
                "        }\n" +
                "    }\n" +
                "}", XContentType.JSON);
        //操作索引的客户端
        IndicesClient indices = restHighLevelClient.indices();
        //执行创建索引库
        try {
            CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            System.out.println(acknowledged);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //Test
    //删除索引库
    @Test
    public void testDelIndex(){
        //删除索引对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
        //操作索引的客户端
        IndicesClient indicesClient  = restHighLevelClient.indices();
        try {
            //删除索引
            DeleteIndexResponse delete = indicesClient.delete(deleteIndexRequest);
            //得到响应
            boolean acknowledged = delete.isAcknowledged();
            System.out.println(acknowledged);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加文档
     */
    @Test
    public void testAddDoc(){
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("name","spring cloud实战");
        jsonMap.put("description","Bootstrap是由Twitter推出的一个前台页面开发框架，在行业之中使用较为广泛。此开发框架包含了大量的CSS、JS程序代码，可以帮助开发者（尤其是不擅长页面开发的程序人员）轻松的实现一个不受浏览器限制的精美界面效果");
        jsonMap.put("studymodel","201001");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        jsonMap.put("timestamp",dateFormat.format(new Date()));
        jsonMap.put("price",5.6f);

        //索引请求对象
        IndexRequest indexRequest = new IndexRequest("xc_course","doc");
        //指定索引文档内容
        indexRequest.source(jsonMap);
        //索引响应对象
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest);
            //获取响应内容
            DocWriteResponse.Result result = indexResponse.getResult();
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询文档
     */
    @Test
    public void testGetDoc(){
        //查询的请求对象
        GetRequest getRequest = new GetRequest("xc_course","doc","oqY-iG8B2ommnsPhB1ui");
        try {
            GetResponse getResponse = restHighLevelClient.get(getRequest);
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            System.out.println(sourceAsMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
