package com.xuecheng.test.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: huiyishe
 * @Date: 2019-12-31 14:42
 */
@Service
public class uploadFile {
    @Value("${xc.endpoint}")
    private String endpoint;
    @Value("${xc.accessKeyId}")
    private String accessKeyId;
    @Value("${xc.accessKeySecret}")
    private String accessKeySecret;
    @Value("${xc.bucketName}")
    private String bucketName;

    public  String uploadFile(String objectKey, MultipartFile multipartFile){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 创建存储空间。
        ossClient.createBucket(bucketName);
        ObjectMetadata meta = new ObjectMetadata();
        String oldFileName = multipartFile.getOriginalFilename();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("/YYYY-MM-dd/HH-mm-ss-");
        String date = simpleDateFormat.format(new Date());
        String name = bucketName+date+oldFileName;
        System.out.println(name);
        meta.addUserMetadata("filename",name);
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,name,new ByteArrayInputStream(multipartFile.getBytes()),meta);
            ossClient.putObject(putObjectRequest);
            Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
            String url = ossClient.generatePresignedUrl(bucketName,name,expiration).toString();
            System.out.println(url);
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    return null;
    }
}
