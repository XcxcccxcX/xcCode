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
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/***
 * @author  xxx
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSearch {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    RestClient restClient;

    /**
     * 搜索全部记录
     */
    @Test
    public void testSearchAll() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //z指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        //matchAllQuery 搜索全部
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //设置源字段过滤 param1 结果集包括哪些字段  param2 结果集不包括哪些
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        //向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索, 向es 发起http 请求
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            //搜索结果
            SearchHits hits = searchResponse.getHits();
            //总记录数
            long totalHits = hits.getTotalHits();
            SearchHit[] hits1 = hits.getHits();
            for (SearchHit documentFields : hits1) {
                //文档主键
                String id = documentFields.getId();
                Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                String name = (String) sourceAsMap.get("name");
                String studymodel = (String) sourceAsMap.get("studymodel");
                Double price = (Double) sourceAsMap.get("price");
                Date timestamp = dateFormat.parse( (String)sourceAsMap.get("timestamp"));
                //不存在 过滤了
                String description = (String)sourceAsMap.get("description");
                System.out.println(name);
                System.out.println(studymodel);
                System.out.println(price);
                System.out.println(timestamp);
                System.out.println(description);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分页搜索
     */
    @Test
    public void testSearchByPage() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //z指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        //matchAllQuery 搜索全部
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置分页参数
        int page = 2;
        //每页记录数
        int size = 1;
        //计算出记录起始下标
        int form = (page - 1)*size;
        searchSourceBuilder.from(form); //起始下标 从0开始
        searchSourceBuilder.size(size); //每页显示条数
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //设置源字段过滤 param1 结果集包括哪些字段  param2 结果集不包括哪些
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        //向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索, 向es 发起http 请求
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            //搜索结果
            SearchHits hits = searchResponse.getHits();
            //总记录数
            long totalHits = hits.getTotalHits();
            SearchHit[] hits1 = hits.getHits();
            for (SearchHit documentFields : hits1) {
                //文档主键
                String id = documentFields.getId();
                Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                String name = (String) sourceAsMap.get("name");
                String studymodel = (String) sourceAsMap.get("studymodel");
                Double price = (Double) sourceAsMap.get("price");
                Date timestamp = dateFormat.parse( (String)sourceAsMap.get("timestamp"));
                //不存在 过滤了
                String description = (String)sourceAsMap.get("description");
                System.out.println(name);
                System.out.println(studymodel);
                System.out.println(price);
                System.out.println(timestamp);
                System.out.println(description);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Term Query
     */
    @Test
    public void testTermQuery() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //z指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        //matchAllQuery 搜索全部
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置分页参数
        int page = 2;
        //每页记录数
        int size = 1;
        //计算出记录起始下标
        int form = (page - 1)*size;
        searchSourceBuilder.from(form); //起始下标 从0开始
        searchSourceBuilder.size(size); //每页显示条数
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //设置源字段过滤 param1 结果集包括哪些字段  param2 结果集不包括哪些
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        //向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索, 向es 发起http 请求
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            //搜索结果
            SearchHits hits = searchResponse.getHits();
            //总记录数
            long totalHits = hits.getTotalHits();
            SearchHit[] hits1 = hits.getHits();
            for (SearchHit documentFields : hits1) {
                //文档主键
                String id = documentFields.getId();
                Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                String name = (String) sourceAsMap.get("name");
                String studymodel = (String) sourceAsMap.get("studymodel");
                Double price = (Double) sourceAsMap.get("price");
                Date timestamp = dateFormat.parse( (String)sourceAsMap.get("timestamp"));
                //不存在 过滤了
                String description = (String)sourceAsMap.get("description");
                System.out.println(name);
                System.out.println(studymodel);
                System.out.println(price);
                System.out.println(timestamp);
                System.out.println(description);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

