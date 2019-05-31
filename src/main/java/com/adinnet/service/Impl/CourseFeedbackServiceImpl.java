package com.adinnet.service.Impl;

import com.adinnet.common.status.AttchType;
import com.adinnet.common.status.IsJudge;
import com.adinnet.dao.AttachMapper;
import com.adinnet.dao.CourseFeedbackMapper;
import com.adinnet.dao.CourseMapper;
import com.adinnet.dao.DoctorMapper;
import com.adinnet.ex.BuzEx;
import com.adinnet.repository.*;
import com.adinnet.repository.dto.CourseEvaluateDto;
import com.adinnet.response.code.CCode;
import com.adinnet.service.CourseFeedbackService;
import com.adinnet.utils.ShiroUtil;
import com.adinnet.utils.UuidUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2018/9/18.
 */
@Service
@Slf4j
public class CourseFeedbackServiceImpl implements CourseFeedbackService {

    @Autowired
    private CourseFeedbackMapper courseFeedbackMapper;
    @Autowired
    private AttachMapper attachMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(CourseFeedback courseFeedback) {
        courseFeedback.setCreateTime(new Date());
        courseFeedback.setUpdateTime(new Date());
        courseFeedbackMapper.save(courseFeedback);
    }


    @Override
    public CourseFeedback getOne(Integer id) {
        return courseFeedbackMapper.getOne(id);
    }
    @Override
    public void update(CourseFeedback courseFeedback) {
        courseFeedback.setCreateTime(new Date());
        courseFeedback.setUpdateTime(new Date());
        courseFeedbackMapper.save(courseFeedback);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateCourseFeedback(CourseFeedback courseFeedback) {
        UserInfo admin = ShiroUtil.getUserInfo();
        courseFeedback.setReplyUserId(admin.getUid());
        courseFeedback.setUpdateTime(new Date());
        courseFeedback.setReply(courseFeedback.getReply());
        courseFeedbackMapper.updateCourseFeedback(admin.getUid(),courseFeedback.getReply(),courseFeedback.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveFeedback(CourseFeedback courseFeedback) {
        UserInfo admin = ShiroUtil.getUserInfo();
        courseFeedback.setCreateTime(new Date());
        courseFeedback.setUpdateTime(new Date());

        Integer  courseId=courseFeedback.getCourseId();
        Course course=courseMapper.queryById(courseId);
        if (course == null ) {
            log.error("course is null");
            throw new BuzEx(CCode.C_PARAM_IS_NULL);
        }
        Integer courseType=course.getCourseType();

        courseFeedback.setCourseId(courseFeedback.getCourseId());
        courseFeedback.setCourseType(courseType);
        courseFeedback.setReply(courseFeedback.getReply());
        courseFeedback.setReplyUserId(admin.getUid());
        courseFeedbackMapper.save(courseFeedback);
    }

    @Override
    public void delete(CourseFeedback courseFeedback) {
        courseFeedbackMapper.delete(courseFeedback);
    }


    @Override
    @Transactional
    public void del(Integer id) {
        courseFeedbackMapper.deleteById(id);
    }

    public Page<CourseFeedback> pageList3(CourseFeedback courseFeedback){
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable=new PageRequest(courseFeedback.page(), courseFeedback.getLimit(),sort);  //分页信息
        Specification<CourseFeedback> spec = new Specification<CourseFeedback>() {        //查询条件构造

            @Override
            public Predicate toPredicate(Root<CourseFeedback> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(null != courseFeedback.getContent()){
                    Path<String> name = root.get("username");
                    predicates.add(cb.like(name, "%"+courseFeedback.getContent()+"%"));
                }
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return courseFeedbackMapper.findAll(spec,pageable);
    }



    public Page<CourseFeedback> pageList(CourseFeedback courseFeedback){
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable=new PageRequest(courseFeedback.page(), courseFeedback.getLimit(),sort);  //分页信息
        Specification<CourseFeedback> spec = new Specification<CourseFeedback>() {        //查询条件构造

            @Override
            public Predicate toPredicate(Root<CourseFeedback> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();

           /*     if( !courseFeedback.getCourseName().equals("")){
                    Course course = courseMapper.queryBytitle1(courseFeedback.getCourseName().trim());
                    // Path<String> name = root.get("courseId");
                    // predicates.add(cb.like(name, "%"+course.getId()+"%"));
                    if(course!=null){
                        Path<Integer> age = root.get("courseId");
                        predicates.add(cb.equal(age, course.getId()));
                    }

                }*/

                if( !courseFeedback.getCourseName().equals("") ){
                   // Course course = courseMapper.queryBytitle1(courseFeedback.getCourseName().trim());
                    List<Course>courseList=courseMapper.queryBytitles(courseFeedback.getCourseName().trim());
                    List<Integer> itemList1 = new ArrayList<>();
                    if(courseList!=null && courseList.size()>0){
                        for(int i=0;i<courseList.size();i++){
                            Integer courseId=courseList.get(i).getId();
                            itemList1.add(courseId);
                        }
                    }
                    if(courseList.size()>0){
                        // Path<Integer> age = root.get("courseId");
                        Expression<Integer> exp = root.<Integer>get("courseId");
                        //predicates.add(cb.equal(age, course.getId()));
                        predicates.add(exp.in(itemList1));
                    }else{
                        Expression<Integer> exp = root.<Integer>get("courseId");
                        predicates.add(exp.in(-1));
                    }

                }

                if( !courseFeedback.getDoctorName().equals("")){
                    //Doctor doctor = doctorMapper.queryByName1(courseEvaluate.getDoctorName().trim());
                    List<Doctor>doctorList=doctorMapper.queryByNames(courseFeedback.getCourseName().trim());
                    List<Integer> itemList1 = new ArrayList<>();
                    if(doctorList!=null && doctorList.size()>0){
                        for(int i=0;i<doctorList.size();i++){
                            Integer courseId=doctorList.get(i).getId();
                            itemList1.add(courseId);
                        }
                    }
                    if(doctorList.size()>0){
                        // Path<Integer> age = root.get("doctorId");
                        Expression<Integer> exp = root.<Integer>get("doctorId");
                        // predicates.add(cb.equal(age, doctor.getId()));
                        predicates.add(exp.in(itemList1));
                    }else{
                        Expression<Integer> exp = root.<Integer>get("doctorId");
                        predicates.add(exp.in(-1));
                    }

                }



                if(null != courseFeedback.getCourseType()){
                    Path<String> name = root.get("courseType");
                    predicates.add(cb.equal(name, courseFeedback.getCourseType()));
                }

           /*     if( !courseFeedback.getDoctorName().equals("")){
                    Doctor doctor = doctorMapper.queryByName1(courseFeedback.getDoctorName().trim());
                    // Path<String> name = root.get("doctorId");
                    // predicates.add(cb.like(name, "%"+doctor.getId()+"%"));
                    if(doctor!=null){
                        Path<Integer> age = root.get("doctorId");
                        predicates.add(cb.equal(age, doctor.getId()));
                    }

                }*/

                if( !courseFeedback.getContent().equals("")) {
                    Path<String> name = root.get("content");
                    predicates.add(cb.like(name, "%"+courseFeedback.getContent().trim()+"%"));
                }

                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        Page<CourseFeedback> plist = courseFeedbackMapper.findAll(spec,pageable);
        List<CourseFeedback> list = plist.getContent();
        for(CourseFeedback c:list){
            //课程
            Course course = courseMapper.queryById(c.getCourseId());
            if(course!=null){
                c.setCourseName(course.getTitle());
            }

            //医生
            Doctor doctor = doctorMapper.queryById(c.getDoctorId());
            if(doctor!=null){
                c.setDoctorName(doctor.getName());
            }

            List<Attach>attachList=attachMapper.findByAttachId(c.getAttachId());
            if(attachList!=null && attachList.size()>0){
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<attachList.size();i++){

                if(sb.length() > 0){
                    sb.append( ";" ).append(attachList.get(i).getPath());
                }else{
                    sb.append(attachList.get(i).getPath());
                  }
                    c.setPath(sb.toString());
                }

            }
        }
        return plist;
    }


    @Override
    public Page<List<Map<String, Object>>> pageList2( CourseFeedback courseFeedback){
        Sort sort = new Sort(Sort.Direction.ASC, "create_time");
        Pageable pageable=new PageRequest(courseFeedback.page(), courseFeedback.getLimit(),sort);  //分页信息
        Specification<CourseFeedback> spec = new Specification<CourseFeedback>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<CourseFeedback> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return courseFeedbackMapper.queryAllFeedBack(spec, pageable);
    }

    @Override
    public Page1<List<Map<String,Object>>> pageList1(CourseEvaluateDto courseEvaluateDto) {
        Page1  page = new Page1<>();
        String hql = " select\n" +
                "  co.*, d.name,\n" +
                "  c.title\n" +
                "   " +
                "     from \n" +
                "     tb_course_feedback co \n" +
                "     left join tb_course c on c.id = co.course_id \n" +
                "     left join tb_doctor d on d.id = co.doctor_id  where co.id is not null ";
        String countHql = " select count(1) " +
                "           from \n" +
                "           tb_course_feedback co \n" +
                "             left join tb_course c on c.id = co.course_id \n" +
                "             left join tb_doctor d on d.id = co.doctor_id   where co.id is not null ";
        StringBuffer str = new StringBuffer(hql);
        StringBuffer str1 = new StringBuffer(countHql);
        if(null != courseEvaluateDto){
            if(null != courseEvaluateDto.getCourseName() && !"".equals(courseEvaluateDto.getCourseName())){
                str.append("  and c.title like concat('%','"+courseEvaluateDto.getCourseName()+"','%') ");
                str1.append("  and  c.title like concat('%','"+courseEvaluateDto.getCourseName()+"','%') ");
            }
            if(null != courseEvaluateDto.getDoctorName() && !"".equals(courseEvaluateDto.getDoctorName())){
                str.append("  and d.name like concat('%','"+courseEvaluateDto.getDoctorName()+"','%') ");
                str1.append("  and  d.name like concat('%','"+courseEvaluateDto.getDoctorName()+"','%') ");
            }
            if(null != courseEvaluateDto.getContent() && !"".equals(courseEvaluateDto.getContent())){
                str.append("  and co.content like concat('%','"+courseEvaluateDto.getContent()+"','%') ");
                str1.append("  and  co.content like concat('%','"+courseEvaluateDto.getContent()+"','%') ");
            }
     /*     if(null != examin.getCcourseAttr()){
                str.append("  and tc.course_attr = "+examin.getCcourseAttr());
                str1.append("  and tc.course_attr = "+examin.getCcourseAttr());
            }
            if(null != examin.getCcourseType()){
                str.append("  and tc.course_type = "+examin.getCcourseType());
                str1.append(" and  tc.course_type = "+examin.getCcourseType());
            }*/
        }
        page.setHql(str.toString());
        page.setCountHql(str1.toString());
        page.setSortName("co.update_time");
        // page.setPageNum(examin.getOffset());
        // page.setPageSize(examin.getLimit());
        return courseFeedbackMapper.findNatPage2(page);
    }



    @Override
    public void saveFeedBack(String body) {
        JSONObject jo = JSONObject.parseObject(body);
        log.info("saveFeedBack  :"+jo.toString());
        // JSONObject jsonObject = JSON.parseObject(body);
        Integer courseId = jo.getInteger("courseId");
        Integer doctorId = jo.getInteger("doctorId");
       // Integer outlineId = jo.getInteger("outlineId");
        String content=jo.getString("content");

        if (courseId == null || doctorId == null) {
            log.error("courseId or doctorId is null");
            throw new BuzEx(CCode.C_PARAM_IS_NULL);
        }

        Course course=courseMapper.queryById(courseId);
        if (course == null ) {
            log.error("course is null");
            throw new BuzEx(CCode.C_PARAM_IS_NULL);
        }
        Integer courseType=course.getCourseType();

        JSONArray imageJa = jo.getJSONArray("imageJa");
        String random= UuidUtil.createRandomNo();//生成随机数
        CourseFeedback cour=new CourseFeedback();
        cour.setCreateTime(new Date());
        cour.setUpdateTime(new Date());
        cour.setContent(content);
        cour.setCourseId(courseId);
        //cour.setOutlineId(outlineId);
        cour.setCourseType(courseType);
        cour.setDoctorId(doctorId);
        cour.setAttachId(random);
        courseFeedbackMapper.save(cour);

        if (imageJa != null && imageJa.size() > 0) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < imageJa.size(); i++) {
                JSONObject j = imageJa.getJSONObject(i);
                Attach attach=new Attach();
                attach.setCreateTime(new Date());
                attach.setUpdateTime(new Date());
                attach.setAttachId(random);
                attach.setType(AttchType._1.getCode());
                attach.setFileName(j.getString("fileName"));
                attach.setFileType(IsJudge.NO.getCode());//图片
                attach.setPath(j.getString("imageUrl"));
                attachMapper.save(attach);

            }

        }

    }

    @Override
    public JSONObject queryAllFeed(String body){
        JSONObject jo = JSONObject.parseObject(body);
        JSONArray jr=new JSONArray();
        log.info("queryAllFeed  :"+jo.toString());
        Integer courseId = jo.getInteger("courseId");
         if (courseId == null) {
        log.error("courseId  is null");
        throw new BuzEx(CCode.C_PARAM_IS_NULL);
       }

        Course course=courseMapper.queryById(courseId);
        if (course == null ) {
            log.error("course is null");
            throw new BuzEx(CCode.C_PARAM_IS_NULL);
        }
        Integer courseType=course.getCourseType();

        List<Map<String, Object>> feedBackList = courseFeedbackMapper.queryFeedByCourseId(courseId,courseType);
 /*       if (feedBackList != null && feedBackList.size() > 0) {
            //for(Map<String, Object> map:feedBackList){
            for (int i = 0; i < feedBackList.size(); i++) {
                JSONArray jr1=new JSONArray();
                String attachId = (String)feedBackList.get(i).get("attach_id");
                List<Attach>attachList=attachMapper.findByAttachId(attachId);
                // List<Map<String, Object>>attachList1=attachMapper.findByAttachId1(attachId);
                //jor.put("attachList1", attachList);
                feedBackList.get(0).put("attachList1", attachList);

            }
        }*/
         SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
        List<CourseFeedback> list = new ArrayList<>();
        for(Map<String, Object> map:feedBackList){
            CourseFeedback feedBack = new CourseFeedback();
            Integer doctorId = (Integer) map.get("doctor_id");
            Doctor doctor = doctorMapper.queryById(doctorId);

            feedBack.setCourseId((Integer) map.get("course_id"));
            feedBack.setCourseType(courseType);
            feedBack.setDoctorId(doctorId);
            feedBack.setCreateTime((Date) map.get("create_time"));
            feedBack.setUpdateTime((Date) map.get("update_time"));
            feedBack.setContent((String) map.get("content"));
            feedBack.setId((Integer) map.get("id"));
            feedBack.setName((String) map.get("name"));
            feedBack.setPhoto((String) map.get("photo"));
            feedBack.setReplyUserId((Integer) map.get("reply_user_id"));
            feedBack.setReply((String) map.get("reply"));
            feedBack.setAttachId((String) map.get("attach_id"));
            feedBack.setPath((String) map.get("path"));

            //feedBack.setReply((String) map.get("replyContent"));
            feedBack.setTitle((String) map.get("title"));
            feedBack.setDatestr(sdf.format(feedBack.getCreateTime()));
            //只查询存在用户的反馈
            if(null != doctor){
                list.add(feedBack);
            }
        }
        jo.put("feedBackList",list);
        return jo;
    }
}
