package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by xxx Cotter on 2019/12/11.
 * @author xcx
 */
@Api(value = "CMS 配置管理接口",description = "CMS 配置管理接口,提供数据模型的管理,查询接口")
public interface CmsConfigControllerApi {
    /**
     * 根据id 查询CMS配置信息
     * */
    @ApiOperation("根据id 查询CMS配置信息")
    public CmsConfig getModel(String id);
}
