package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 自定义异常类型
 * Created by Enzo Cotter on 2019/12/22.
 */
public class CustomException extends RuntimeException {
    /**
     * 错误代码
     */
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }
    /**
     * 获取错误代码
     */
    public ResultCode getResultCode(){
        return resultCode;
    }
}
