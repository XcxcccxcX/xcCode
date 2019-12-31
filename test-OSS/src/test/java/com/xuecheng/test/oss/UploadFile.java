package com.xuecheng.test.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.*;

/**
 * @author: huiyishe
 * @Date: 2019-12-31 10:41
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:config/oss.properties")
public class UploadFile {

    @Value("${endpoint}")
    private String endpoint;
    @Value("${accessKeyId}")
    private String accessKeyId;
    @Value("${accessKeySecret}")
    private String accessKeySecret;
    @Value("${bucketName}")
    private String bucketName;
    @Test
    public void tetsOss(){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 创建存储空间。
        ossClient.createBucket(bucketName);
        try {
            File filename = new File("d:/gamedb.sql");
            ObjectMetadata meta = new ObjectMetadata();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }
}
