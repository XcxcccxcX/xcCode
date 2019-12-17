package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Enzo Cotter on 2019/12/11.
 */
@Data
public class QueryPageRequest {
    //接收页面查询的查询条件
    @ApiModelProperty("站点id")
    private String siteId;

    @ApiModelProperty("页面id")
    private String pageId;

    @ApiModelProperty("页面名称")
    private String pageName;

    @ApiModelProperty("别名")
    private String pageAliase;

    @ApiModelProperty("模板id")
    private String templateId;

    //TODO...
}
