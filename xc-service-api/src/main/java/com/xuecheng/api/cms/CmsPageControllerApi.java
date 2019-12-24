package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xxx Cotter on 2019/12/11.
 * @author xcx
 */
@Api(value = "CMS页面管理接口",description = "CMS页面管理接口,提供界面接口的 增,删,改,查")
public interface CmsPageControllerApi {
    //页面查询
    /**
    * 页面查询  / 条件查询
     * @param page 页码
     * @param size 条数
     * @param queryPageRequest 请求条件
     * @return 返回信息 状态 ...
    * */
    @ApiOperation("分页查询列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "page",value = "页码",required = true,paramType = "path",dataType = "int"),
                        @ApiImplicitParam(name = "size",value = "每页记录数",required = true,paramType = "path",dataType = "int")
    })
     QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);


    @ApiOperation("添加页面")
    CmsPageResult addCmsPage(CmsPage cmsPage);

    @ApiOperation("根据id 查询页面信息")
    CmsPage findById(String id);

    @ApiOperation("edit 修改页面信息")
    CmsPageResult edit(String id,CmsPage cmsPage);

    @ApiOperation("del 删除页面信息")
    ResponseResult delete(String id);
}
