package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: huiyishe
 * @Date: 2019-12-26 13:51
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    CourseService courseService;

    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody  Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult findCourseList(@PathVariable("page") int page,@PathVariable("size") int size, CourseListRequest courseListRequest) {
        return courseService.findCourseList(page,size,courseListRequest);
    }

    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult courseBaseAdd(@RequestBody CourseBase courseBase) {
        return courseService.courseBaseAdd(courseBase);
    }

    @Override
    @GetMapping("/coursebase/get/{courseBaseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseBaseId") String courseBaseId) {
        return courseService.getCourseBaseById(courseBaseId);
    }

    @Override
    @PostMapping("/coursebase/update/{courseId}")
    public ResponseResult updateCourseBaseById(@PathVariable("courseId") String courseid, @RequestBody CourseBase courseBase) {
        return courseService.updateCourseBaseById(courseid,courseBase);
    }

    @Override
    @GetMapping("/coursemarket/get/{courseid}")
    public CourseMarket getCourseMarketById(@PathVariable("courseid") String courseId) {
        return courseService.getCourseMarketById(courseId);
    }

    @Override
    @PostMapping("/coursemarket/update/{courseId}")
    public ResponseResult updateCourseMarket(@PathVariable("courseId") String courseid, @RequestBody CourseMarket courseMarket) {
        return courseService.updateCourseMarket(courseid,courseMarket);
    }

    @Override
    @PostMapping("/coursePic/add")
    public ResponseResult saveCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        return courseService.addCoursePic(courseId,pic);
    }

    @Override
    @GetMapping("/coursePic/list/{courseId}")
    public CoursePic findPicByCourseId(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePicByCourseId(courseId);
    }
}
