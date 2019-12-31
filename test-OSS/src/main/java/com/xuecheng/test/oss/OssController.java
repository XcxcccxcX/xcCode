package com.xuecheng.test.oss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: huiyishe
 * @Date: 2019-12-31 14:49
 */
@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    uploadFile uploadFile;
    @PostMapping
    public void fileUpload(MultipartFile multipartFile,String fileName){
        uploadFile.uploadFile(fileName,multipartFile);
    }
}
