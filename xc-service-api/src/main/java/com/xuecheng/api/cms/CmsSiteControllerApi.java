package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by xxx Cotter on 2019/12/11.
 * @author xcx
 */
@Api(value = "CMS Site 站点查询管理接口",description = "CMS Site 站点查询")
public interface CmsSiteControllerApi {
    /**
     * 查询站点
     * @return 返回信息 状态 ...
     * */
    @ApiOperation("查询站点")
    QueryResponseResult findSiteList();
}
