package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author: huiyishe
 * @Date: 2019-12-31 16:56
 */
@Service
public class FileSystemService {
    private static Logger LOGGER = LoggerFactory.getLogger(FileSystem.class);

    @Value("${xuecheng.oss.endpoint}")
    private String endpoint;
    @Value("${xuecheng.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${xuecheng.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${xuecheng.oss.bucketName}")
    private String bucketName;

    @Autowired
    private FileSystemRepository fileSystemRepository;
    /**
     * 上传文件
     * @param multipartFile
     * @param filetag
     * @param businesskey
     * @param metadata
     * @return
     */
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata) {
        if (multipartFile == null){
            ExceptionCast.cast(CommonCode.UPLOAD_FILE_DO_NOT_EXIT);
            LOGGER.error("上传文件不存在 time:{}",new Date());
        }
        //上传文件
        String url = uplodaFile(multipartFile);
        if(StringUtils.isEmpty(url)){
            ExceptionCast.cast(CommonCode.UPLOAD_FILE_ERROR);
            LOGGER.error("上传文件失败 文件name:{}",multipartFile.getOriginalFilename());
        }
        
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(url);
        fileSystem.setFilePath(url);
        fileSystem.setFiletag(filetag);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileType(multipartFile.getContentType());
        if (StringUtils.isNotEmpty(metadata)){
            try {
                Map map = JSON.parseObject(metadata,Map.class);
                fileSystem.setMetadata(map);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
     }

        private String uplodaFile(MultipartFile multipartFile){

            String url = null;
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
                 url = ossClient.generatePresignedUrl(bucketName,name,expiration).toString();
                System.out.println(url);
            } catch (IOException e) {
                e.printStackTrace();
                ExceptionCast.cast(CommonCode.UPLOAD_FILE_ERROR);
            }finally {
                // 关闭OSSClient。
                ossClient.shutdown();
            }
            return url;
        }

}
