package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: huiyishe
 * @Date: 2019-12-31 16:41
 */
@Api(value = "上传文件接口 ",description = "文件上传 图片上传 接口")
public interface FileSystemControllerApi {

    @ApiOperation("上传文件接口")
    UploadFileResult upload(MultipartFile multipartFile,
                            String filetag,
                            String businesskey,
                            String metadata);
}
