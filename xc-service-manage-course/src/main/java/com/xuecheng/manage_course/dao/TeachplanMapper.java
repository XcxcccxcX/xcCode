package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: huiyishe
 * @Date: 2019-12-26 11:44
 */
@Mapper
public interface TeachplanMapper {

    //课程计划查询
    public TeachplanNode selectList(String courseId);
}
