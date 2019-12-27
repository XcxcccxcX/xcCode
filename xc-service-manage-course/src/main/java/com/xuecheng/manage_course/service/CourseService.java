package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author: huiyishe
 * @Date: 2019-12-26 13:48
 */
@Service
public class CourseService {
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    TeachplanRepository teachplanRepository;
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;

    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.selectList(courseId);
    };

    //addTeachplan  添加课程计划
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null ||
                StringUtils.isEmpty(teachplan.getCourseid())||
                StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //获得课程id
        String couseId = teachplan.getCourseid();
        //获取parentid
        String parentId = teachplan.getParentid();
        if (StringUtils.isEmpty(parentId)){
            parentId = this.getTeachPlanRoot(couseId);
        }
        Optional<Teachplan> optionalTeachplan = teachplanRepository.findById(parentId);
        Teachplan teachplanNode = optionalTeachplan.get();
        String grade = teachplanNode.getGrade();

        Teachplan teachplanNew = new Teachplan();
        //将 传来的 teachplan  copy 到新的teachplanNew 对象中
        BeanUtils.copyProperties(teachplan,teachplanNew);
        teachplanNew.setParentid(parentId);
        teachplanNew.setCourseid(couseId);
        if (grade.equals("1")){
            teachplanNew.setGrade("2");//级别
        }else {
            teachplanNew.setGrade("3");//级别
        }
        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询课程的根节点  查询不到则添加
    private String getTeachPlanRoot(String courseId){
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(courseId);
        if (!optionalCourseBase.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
            return  null;
        }
        CourseBase courseBase = optionalCourseBase.get();
        List<Teachplan> teachplans = teachplanRepository.findByCourseidAndParentid(courseId,"0");
        if (teachplans == null || teachplans.size()<=0){
            //查询不到
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setCourseid(courseId);
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        return teachplans.get(0).getId();
    }

    //查询我的课程列表
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest){
        PageHelper.startPage(page,size);
        Page<CourseInfo> courseInfos = courseMapper.findMyCourseList();
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(courseInfos.getTotal());
        queryResult.setList(courseInfos.getResult());
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }
}
