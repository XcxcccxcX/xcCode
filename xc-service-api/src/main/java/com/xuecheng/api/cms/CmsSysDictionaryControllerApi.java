package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by xxx Cotter on 2019/12/11.
 * @author xcx
 */
@Api(value = "CMS SysDictionary 字典接口",description = "CMS SysDictionary 字典接口 查询")
public interface CmsSysDictionaryControllerApi {
    /**
     * 根据type  查询
     * @return 返回信息 list ...
     * */
    @ApiOperation("SysDictionary 接口查询")
    SysDictionary getByType(String type);
}
