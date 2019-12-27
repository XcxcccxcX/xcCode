package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: huiyishe
 * @Date: 2019-12-26 11:44
 */
@Mapper
public interface CategoryMapper {

    //课程分类查询
    public CategoryNode findCategory();
}
