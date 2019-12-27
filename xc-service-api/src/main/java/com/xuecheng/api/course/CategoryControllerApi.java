package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: huiyishe
 * @Date: 2019-12-26 10:50
 */
@Api(value = "CMS 课程分类接口",description = "CMS 课程分类接口,提供课程分类的查询接口")
public interface CategoryControllerApi {

    @ApiOperation("查询课程分类")
    public CategoryNode findCategoryList();
}
