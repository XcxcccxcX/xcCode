package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author: huiyishe
 * @Date: 2019-12-26 13:48
 */
@Service
public class CourseService {

    private static  final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    TeachplanRepository teachplanRepository;
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseMarketRepository courseMarketRepository;
    @Autowired
    CoursePicRepository coursePicRepository;
    @Autowired
    CmsPageClient cmsPageClient;

    @Value("${course-publish.dataUrlPre}")
    private  String publish_dataUrlPre;
    @Value("${course-publish.siteId}")
    private  String publish_siteId;
    @Value("${course-publish.templateId}")
    private  String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private  String publish_previewUrl;
    @Value("${course-publish.pageWebPath}")
    private  String publish_pageWebPath;
    @Value("${course-publish.pagePhysicalPath}")
    private  String publish_pagePhysicalPath;


    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.selectList(courseId);
    }

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
            //级别
            teachplanNew.setGrade("2");
        }else {
            //级别
            teachplanNew.setGrade("3");
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

    public ResponseResult courseBaseAdd(CourseBase courseBase) {
        if (courseBase==null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        if (courseBase.getName() == "" || StringUtils.isEmpty(courseBase.getName())
                ||courseBase.getMt() == "" || StringUtils.isEmpty(courseBase.getMt())||
                courseBase.getSt() == "" || StringUtils.isEmpty(courseBase.getSt()) ||
                courseBase.getGrade() == "" || StringUtils.isEmpty(courseBase.getGrade()) ||
                courseBase.getStudymodel() == "" || StringUtils.isEmpty(courseBase.getStudymodel()) ||
                courseBase.getTeachmode() == "" || StringUtils.isEmpty(courseBase.getStudymodel())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        if (courseBase.getId() == "" || StringUtils.isEmpty(courseBase.getId())){
            CourseBase courseBaseAdd = new CourseBase();
            String uuid = String.valueOf(UUID.randomUUID());
            uuid = uuid.replace("-", "");
            BeanUtils.copyProperties(courseBase,courseBaseAdd);
            courseBaseAdd.setId(uuid);
            courseBaseAdd.setStatus("202002");
            courseBaseRepository.save(courseBaseAdd);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return  null;
    }

    /**
     * 获取课程根据id courseBaseId
     */
    public CourseBase getCourseBaseById(String courseBaseId) {
        if (courseBaseId == "" || StringUtils.isEmpty(courseBaseId)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(courseBaseId);
        if (!optionalCourseBase.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CourseBase courseBase = optionalCourseBase.get();
        return courseBase;
    }

    /**
     * 修改课程基本信息
     */
    @Transactional
    public ResponseResult updateCourseBaseById(String courseid, CourseBase courseBase) {
        if (courseid == "" || StringUtils.isEmpty(courseid) || courseBase == null){
            LOGGER.error("修改课程基本信息 非法参数 courseId:{},courseBase:{}",courseid,courseBase.toString());
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        if (courseBase.getName() == "" || StringUtils.isEmpty(courseBase.getName())
                ||courseBase.getMt() == "" || StringUtils.isEmpty(courseBase.getMt())||
                courseBase.getSt() == "" || StringUtils.isEmpty(courseBase.getSt()) ||
                courseBase.getGrade() == "" || StringUtils.isEmpty(courseBase.getGrade()) ||
                courseBase.getStudymodel() == "" || StringUtils.isEmpty(courseBase.getStudymodel()) ||
                courseBase.getTeachmode() == "" || StringUtils.isEmpty(courseBase.getStudymodel())){
            LOGGER.error("修改课程基本信息 非法参数 验证参数");
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<CourseBase> courseBaseRepositoryById = courseBaseRepository.findById(courseid);
        if (!courseBaseRepositoryById.isPresent()){
            LOGGER.error("根据id查询课程信息 不存在 courseId:{}",courseid);
            ExceptionCast.cast(CommonCode.SEARCH_RESULTS_DO_NOT_EXIT);
        }
        CourseBase courseBase1 = courseBaseRepositoryById.get();
        BeanUtils.copyProperties(courseBase,courseBase1);
        courseBase1.setStatus("202002");
        courseBaseRepository.save(courseBase1);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获取课程营销信息
     * @param courseId 课程id
     * @return
     */
    public CourseMarket getCourseMarketById(String courseId) {
        if (courseId == "" || StringUtils.isEmpty(courseId)){
            LOGGER.error("获取课程营销信息 参数异常 courseId:{}",courseId);
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<CourseMarket> courseMarketRepositoryById = courseMarketRepository.findById(courseId);
        if (!courseMarketRepositoryById.isPresent()){
            LOGGER.error("根据课程id 查询营销信息result 异常 courseId:{}",courseId);
            return null;
        }
        CourseMarket courseMarket = courseMarketRepositoryById.get();
        return courseMarket;
    }

    /**
     * 修改或添加 课程营销信息
     * @return
     */
    @Transactional
    public ResponseResult updateCourseMarket(String id,CourseMarket courseMarket){
        if (StringUtils.isEmpty(id) || id.equals("") || courseMarket.getCharge().equals("") || StringUtils.isEmpty(courseMarket.getCharge())
                || StringUtils.isEmpty(courseMarket.getValid()) || courseMarket.getCharge().equals("")){
            LOGGER.error("修改或添加 课程营销信息 参数异常");
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<CourseMarket> byId = courseMarketRepository.findById(id);
        if (!byId.isPresent()){
            CourseMarket courseMarket1 = new CourseMarket();
            BeanUtils.copyProperties(courseMarket,courseMarket1);
            courseMarketRepository.save(courseMarket1);
        }else {
            CourseMarket courseMarket1 = byId.get();
            BeanUtils.copyProperties(courseMarket,courseMarket1);
            courseMarketRepository.save(courseMarket1);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public ResponseResult addCoursePic(String courseId, String pic) {
        if (StringUtils.isEmpty(courseId) || courseId == "" ||
        StringUtils.isEmpty(pic) || pic == ""){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        CoursePic coursePic = null;
        Optional<CoursePic> optionalCoursePic = coursePicRepository.findById(courseId);
        if (optionalCoursePic.isPresent()){
            coursePic = optionalCoursePic.get();
        }
        if (coursePic == null){
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 查询课程图片
     * @param courseId
     * @return
     */
    public CoursePic findCoursePicByCourseId(String courseId) {
        Optional<CoursePic> optionalCoursePic = coursePicRepository.findById(courseId);
        if (optionalCoursePic.isPresent()){
            CoursePic coursePic = optionalCoursePic.get();
            return coursePic;
        }
        return null;
    }

    /**
     * 课程视图信息
     * @param id
     * @return
     */
    public CourseView courseViewById(String id) {
        if (StringUtils.isEmpty(id)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        CourseView courseView = new CourseView();
        //课程基本信息
        CourseBase courseBase = this.getCourseBaseById(id);
        if (courseBase!=null){
            courseView.setCourseBase(courseBase);
        }
        //营销信息
        CourseMarket courseMarket = this.getCourseMarketById(id);
        if (courseMarket!=null){
            courseView.setCourseMarket(courseMarket);
        }
        //课程计划信息
        TeachplanNode teachplanNode = this.findTeachplanList(id);
        if (teachplanNode!=null){
            courseView.setTeachplanNode(teachplanNode);
        }
        return courseView;
    }
    public CourseBase findByBaseId(String courseId){
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(courseId);
        if (optionalCourseBase.isPresent()){
            CourseBase courseBase = optionalCourseBase.get();
            return courseBase;
        }
        ExceptionCast.cast(CommonCode.SEARCH_RESULTS_DO_NOT_EXIT);
        return null;
    };


    public CoursePublishResult preview(String id) {
        CmsPage cmsPage = new CmsPage();
        CourseBase courseBase = this.findByBaseId(id);
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setDataUrl(publish_dataUrlPre+id);
        cmsPage.setPageName(id+".html");
        cmsPage.setPagePhysicalPath(publish_pagePhysicalPath);
        cmsPage.setPageWebPath(publish_pageWebPath);

        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if (!cmsPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        CmsPage cmsPage1  = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        String previewUrl = publish_previewUrl+pageId;
        return new CoursePublishResult(CommonCode.SUCCESS,previewUrl);
    }
}
