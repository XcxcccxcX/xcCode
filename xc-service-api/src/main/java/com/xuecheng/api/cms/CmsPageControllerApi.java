package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * Created by xxx Cotter on 2019/12/11.
 * @author xcx
 */
public interface CmsPageControllerApi {
    //页面查询
    /**
    * 页面查询
     * @param page 页码
     * @param size 条数
     * @param queryPageRequest 请求条件
     * @return 返回信息 状态 ...
    * */
     QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
}
