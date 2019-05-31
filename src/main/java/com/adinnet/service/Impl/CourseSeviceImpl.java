package com.adinnet.service.Impl;

import com.adinnet.dao.*;
import com.adinnet.ex.BuzEx;
import com.adinnet.repository.*;
import com.adinnet.repository.vo.CourseVo;
import com.adinnet.response.code.CCode;
import com.adinnet.service.CourseService;
import com.adinnet.service.DoctorService;
import com.adinnet.service.SemesterService;
import com.adinnet.service.SpecialistService;
import com.adinnet.utils.qiniu.QiniuUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by RuanXiang on 2018/9/19.
 */
@Service
public class CourseSeviceImpl implements CourseService{
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private DoctorService doctorService;


    @Autowired
    private SemesterMapper semesterMapper;

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private CourseOutlineMapper courseOutlineMapper;

    @Autowired
    private CourseSpecialistMapper courseSpecialistMapper;

    @Autowired
    private ExperienceShareMapper experienceShareMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(Course course) {
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());
        courseMapper.save(course);
    }

    @Override
    public void update(Course course) {
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());
        courseMapper.save(course);
    }

    @Override
    public void delete(Course courseEvaluate) {
        courseMapper.delete(courseEvaluate);
    }

    @Override
    public Course queryById(Integer id) {
        return courseMapper.queryById(id);
    }

    public Page<Course> pageList(Course course){
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable=new PageRequest(course.page(), course.getLimit(),sort);  //分页信息
        Specification<Course> spec = new Specification<Course>() {        //查询条件构造

            @Override
            public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //query.where(cb.and(cb.equal(root.get("status").as(Integer.class),1))).getRestriction();
                predicates.add(cb.notEqual(root.get("status"), 2));
                if(StringUtils.isNotBlank(course.getTitle())){
                    Path<String> name = root.get("title");
                    predicates.add(cb.like(name, "%"+course.getTitle()+"%"));
                }
                if(StringUtils.isNotBlank(course.getSpecialist())){
                    Path<String> name = root.get("specialistNames");
                    predicates.add(cb.like(name, "%"+course.getSpecialist()+"%"));
                }
                if(null != course.getCourseAttr()){
                    Path<String> name = root.get("courseAttr");
                    predicates.add(cb.equal(name, course.getCourseAttr()));
                }
                if(null != course.getCourseType()){
                    Path<String> name = root.get("courseType");
                    predicates.add(cb.equal(name, course.getCourseType()));
                }
                if(null != course.getSemesterId()){
                    Path<String> name = root.get("semesterId");
                    predicates.add(cb.equal(name, course.getSemesterId()));
                }
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        Page<Course> plist = courseMapper.findAll(spec,pageable);
        List<Course> list = plist.getContent();
        for(Course c:list){
            //视频大纲数
            List<CourseOutline> courseOutlines = courseOutlineMapper.queryByCourseId(c.getId());
            c.setChapterCount(courseOutlines==null?0:courseOutlines.size());
            //学期名称
            Semester semester = semesterMapper.queryById(c.getSemesterId());
            c.setSemesterName(semester.getName());
            List<Specialist> specialistList = specialistService.querySpecialistByCourse(c.getId());
            String teachers = "";
            for(int i=0;i<specialistList.size();i++){
                if(i==0){
                    teachers = specialistList.get(i).getName();
                }else{
                    teachers = teachers +" | " + specialistList.get(i).getName();
                }
            }
            c.setSpecialist(teachers);
        }
//        Iterator<Course> it = plist.iterator();
//        while(it.hasNext()){
//            Course c = it.next();
//            //视频大纲数
//            List<CourseOutline> courseOutlines = courseOutlineMapper.queryByCourseId(c.getId());
//            c.setChapterCount(courseOutlines==null?0:courseOutlines.size());
//            //学期名称
//            Semester semester = semesterMapper.queryById(c.getSemesterId());
//            c.setSemesterName(semester.getName());
//            List<Specialist> specialistList = specialistService.querySpecialistByCourse(c.getId());
//            String teachers = "";
//            for(int i=0;i<specialistList.size();i++){
//                if(i==0){
//                    teachers = specialistList.get(i).getName();
//                }else{
//                    teachers = teachers +" | " + specialistList.get(i).getName();
//                }
//            }
//            c.setSpecialist(teachers);
//            if(StringUtils.isNotBlank(course.getSpecialist())){
//                System.out.println("*******"+teachers.indexOf(course.getSpecialist()));
//                if(teachers.indexOf(course.getSpecialist())==-1){
//                    it.remove();
//                }
//            }
//        }
        return plist;
    }

    @Override
    public List<Course> selectAllList() {
        Specification<Course> spec = new Specification<Course>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                Path<Integer> status = root.get("status");
                predicates.add(cb.equal(status, 1));
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return courseMapper.findAll(spec);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer updateCourseNum(Integer id) {
        Course course = courseMapper.getOne(id);
        Integer num = course.getStudyNum();
        num +=1;
        courseMapper.updateCourseNum(num,id);
        return num;
    }

    @Override
    public JSONObject queryListByParam(Integer doctorId,Integer coursetype) {
        Doctor doctor = doctorService.queryById(doctorId);
        //查询本学期
        Semester semester = semesterMapper.queryThisSemester();
        //通过医生属性查询对应课程 0:乡村医生 1：全科医生
        List<Course> list = new ArrayList<Course>();
//        if(doctor.getProperty()==0){
//            list = courseMapper.queryListByParamAsc(coursetype,semester.getId(),20);
//        }else{
//            list = courseMapper.queryListByParamDesc(coursetype,semester.getId(),20);
//        }
        list = courseMapper.queryListByParam(coursetype,semester.getId(),10,doctor.getProperty());
        List<CourseVo> listVo = getList(list);
        JSONObject jsonObject = new JSONObject();
        //学期名称
        jsonObject.put("semesterName",semester.getName());
        //课程
        jsonObject.put("list",listVo);
        return jsonObject;
    }

    @Override
    public JSONObject queryIntroduceById(Integer courseId) {
        //查询本学期
        Course course = courseMapper.getOne(courseId);
        //通过课程查询讲课专家
        List<Specialist> specialistList = specialistService.querySpecialistByCourse(course.getId());
        JSONObject jsonObject = new JSONObject();
        //学期名称
        jsonObject.put("courseIntroduce",course.getIntroduce());
        //课程
        jsonObject.put("specialist",specialistList);
        return jsonObject;
    }
    @Override
    public List<Course> queryByExam(){
        return courseMapper.queryByExam();
    }

    @Override
    public void saveCourse(Course course, MultipartFile file,MultipartFile imageFile) throws IOException, HttpException {
        if(file != null){
            String path = QiniuUtils.upload(file, "courseImage");
            course.setPhoto(path);
        }
        if(imageFile != null){
            String image = QiniuUtils.upload(imageFile, "courseImage");
            course.setImage(image);
        }
        //随机听课人数
        int max=2000;
        int min=1000;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        course.setStudyNum(s);
        //本学期
//        Semester semester = semesterMapper.queryThisSemester();
//        course.setSemesterId(semester.getId());
        course.setStatus(1);
        course.setUserId(1);
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());
        courseMapper.save(course);
        saveCourseSpecialist(course.getId(),course.getSpecialistCodes());
    }

    @Override
    @Transactional
    public void updateCourse(Course course, MultipartFile file,MultipartFile imageFile) throws IOException, HttpException {
        Integer courseId = course.getId();
        Course course1 = courseMapper.queryById(courseId);
        if(file.getOriginalFilename() !=null &&!"".equals(file.getOriginalFilename())){
            String path = QiniuUtils.upload(file, "courseImage");
            course.setPhoto(path);
        }else{
            course.setPhoto(course1.getPhoto());
        }
        if(imageFile.getOriginalFilename() !=null &&!"".equals(imageFile.getOriginalFilename())){
            String image = QiniuUtils.upload(imageFile, "courseImage");
            course.setImage(image);
        }else{
            course.setImage(course1.getImage());
        }
        course.setSemesterId(course.getSemesterId());
        course.setUserId(course1.getUserId());
        course.setStudyNum(course1.getStudyNum());
        course.setCreateTime(course1.getCreateTime());
        //保存专家 删除再添加
        courseSpecialistMapper.deleteByCourseId(courseId);
        String specialistCodes = course.getSpecialistCodes();
        saveCourseSpecialist(courseId,specialistCodes);
        course.setUpdateTime(new Date());
        courseMapper.saveAndFlush(course);
    }

    @Override
    @Transactional
    public void del(Integer id) {
        courseMapper.updateById(id);
    }

    public void saveCourseSpecialist(Integer courseId,String specialistCodes){
        String specialistCode = specialistCodes.substring(0,specialistCodes.length()-1);
        String[] strArr = specialistCode.trim().split(";");
        for (String str : strArr) {
            //专家id
            Integer sId = Integer.parseInt(str);
            CourseSpecialist courseSpecialist = new CourseSpecialist();
            courseSpecialist.setSpecialistId(sId);
            courseSpecialist.setCourseId(courseId);
            courseSpecialistMapper.save(courseSpecialist);
        }
    }

    @Override
    public Course getOne(Integer id) {
        return courseMapper.getOne(id);
    }

    @Override
    public JSONObject queryIndexInfo(Integer doctorId) {
        JSONObject jsonObject = new JSONObject();
        Doctor doctor = doctorService.queryById(doctorId);
        //查询本学期
        Semester semester = semesterMapper.queryThisSemester();
        Integer semesterId = null;
        if(null != semester){
            semesterId = semester.getId();
            //必学课程
            List<Course> courseList = new ArrayList<Course>();
            //知识学习
            List<Course> studyList = new ArrayList<Course>();
            //乡村医生
//            if(doctor.getProperty()==0){
//                courseList = courseMapper.queryListByParamAsc(1,semesterId,6);
//                studyList = courseMapper.queryListByParamAsc(2,semesterId,6);
//            }else{
//                courseList = courseMapper.queryListByParamDesc(1,semesterId,6);
//                studyList = courseMapper.queryListByParamDesc(2,semesterId,6);
//            }
            courseList = courseMapper.queryListByParam(1,semesterId,6,doctor.getProperty());
            studyList = courseMapper.queryListByParam(2,semesterId,6,doctor.getProperty());
            //通过医生属性查询对应课程 0:乡村医生 1：全科医生
            List<CourseVo> courseListVo = getList(courseList);
            List<CourseVo> studyListVo = getList(studyList);
            //学期名称
            jsonObject.put("semesterName",semester.getName());
            //必学课程
            jsonObject.put("courseList",courseListVo);
            //知识学习
            jsonObject.put("studyListVo",studyListVo);
        }else{
            throw new BuzEx(CCode.C_Semester_NULL);
        }
        //经验分享
        List<ExperienceShare> experienceList =  experienceShareMapper.findSix();
        jsonObject.put("experienceList",experienceList);
        return jsonObject;
    }

    @Override
    public List<Course> queryListAllExists(Integer courseId) {
        if (null !=courseId  ) {
            return courseMapper.queryListAllExists(courseId);
        }else{
            return courseMapper.queryListAllExists2();
        }
    }

    public List<CourseVo> getList(List<Course> list){
        List<CourseVo> listVo = new ArrayList<CourseVo>();
        for(Course course:list){
            List<Specialist> specialistList = specialistService.querySpecialistByCourse(course.getId());
            String teachers = "";
            CourseVo courseVo = new CourseVo();
            for(Specialist specialist:specialistList){
                teachers = teachers +" " + specialist.getName();
            }
            //视频大纲数
            List<CourseOutline> courseOutlines = courseOutlineMapper.queryByCourseId(course.getId());
            courseVo.setTeachers(teachers);
            courseVo.setId(course.getId());
            courseVo.setChapterCount(courseOutlines==null?0:courseOutlines.size());
            courseVo.setPhoto(course.getPhoto());
            courseVo.setTitle(course.getTitle());
            listVo.add(courseVo);
        }
        return listVo;
    }
    @Override
    @Transactional
    public void doSpecialist(){
        List<Course> list = courseMapper.queryListAlls();
        for(Course course:list){
            List<Specialist> specialistList = specialistService.querySpecialistByCourse(course.getId());
            String teachers = "";
            for(int i=0;i<specialistList.size();i++){
                if(i==0){
                    teachers = specialistList.get(i).getName();
                }else{
                    teachers = teachers +";" + specialistList.get(i).getName();
                }
            }
            courseMapper.updateSpecialistNames(teachers,course.getId());
        }
    }
}
