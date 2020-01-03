package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: huiyishe
 * @Date: 2019-12-26 10:50
 */
@Api(value = "CMS 课程管理接口",description = "CMS 课程管理接口,提供课程的管理,查询接口")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询我的课程列表")
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程")
    public ResponseResult courseBaseAdd(CourseBase courseBase);

    @ApiOperation("根据id查询课程")
    public CourseBase getCourseBaseById(String courseBaseId);

    @ApiOperation("修改课程基本信息")
    public ResponseResult updateCourseBaseById(String courseBaseId,CourseBase courseBase);

    @ApiOperation("获取课程营销信息")
    public CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("更新/添加 课程营销信息")
    public ResponseResult updateCourseMarket(String courseId,CourseMarket courseMarket);

    @ApiOperation("课程 保存图片地址")
    ResponseResult saveCoursePic(String courseId,String pic);

    @ApiOperation("查询 课程图片地址")
    CoursePic findPicByCourseId(String courseId);

    @ApiOperation("课程视图查询")
    CourseView courseView(String id);

    @ApiOperation("课程预览")
    CoursePublishResult preview(String id);
}
