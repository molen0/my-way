package adinnet.service.Impl;

import com.adinnet.dao.CourseEvaluateMapper;
import com.adinnet.dao.CourseMapper;
import com.adinnet.dao.DoctorMapper;
import com.adinnet.ex.BuzEx;
import com.adinnet.repository.Course;
import com.adinnet.repository.CourseEvaluate;
import com.adinnet.repository.Doctor;
import com.adinnet.repository.Page1;
import com.adinnet.repository.dto.CourseEvaluateDto;
import com.adinnet.response.code.CCode;
import com.adinnet.service.CourseEvaluateService;
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
public class CourseEvaluateServiceImpl implements CourseEvaluateService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseEvaluateMapper courseEvaluateMapper;
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(CourseEvaluate courseEvaluate) {
        courseEvaluate.setCreateTime(new Date());
        courseEvaluate.setUpdateTime(new Date());
        courseEvaluateMapper.save(courseEvaluate);
    }

    @Override
    public void update(CourseEvaluate courseEvaluate) {
        courseEvaluate.setCreateTime(new Date());
        courseEvaluate.setUpdateTime(new Date());
        courseEvaluateMapper.save(courseEvaluate);
    }

    @Override
    public void delete(CourseEvaluate courseEvaluate) {
        courseEvaluateMapper.delete(courseEvaluate);
    }

    @Override
    @Transactional
    public void del(Integer id) {
        courseEvaluateMapper.deleteById(id);
    }


    @Override
    public Page<CourseEvaluate> pageList(CourseEvaluate courseEvaluate){
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable=new PageRequest(courseEvaluate.page(), courseEvaluate.getLimit(),sort);  //分页信息
        Specification<CourseEvaluate> spec = new Specification<CourseEvaluate>() {        //查询条件构造

            @Override
            public Predicate toPredicate(Root<CourseEvaluate> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                   if( !courseEvaluate.getCourseName().equals("") ){
                       //Course course = courseMapper.queryBytitle1(courseEvaluate.getCourseName().trim());
                       List<Course>courseList=courseMapper.queryBytitles(courseEvaluate.getCourseName().trim());
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

                if( !courseEvaluate.getDoctorName().equals("")){
                    //Doctor doctor = doctorMapper.queryByName1(courseEvaluate.getDoctorName().trim());
                    List<Doctor>doctorList=doctorMapper.queryByNames(courseEvaluate.getDoctorName().trim());
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

                if(null != courseEvaluate.getCourseType()){
                    Path<String> name = root.get("courseType");
                    predicates.add(cb.equal(name, courseEvaluate.getCourseType()));
                }

                if( !courseEvaluate.getImpression().equals("")){
                    Path<String> name = root.get("impression");
                    predicates.add(cb.like(name, "%"+courseEvaluate.getImpression().trim()+"%"));
                }

                if( !courseEvaluate.getExpectCourse().equals("")){
                    Path<String> name = root.get("expectCourse");
                    predicates.add(cb.like(name, "%"+courseEvaluate.getExpectCourse().trim()+"%"));
                }

                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        Page<CourseEvaluate> plist = courseEvaluateMapper.findAll(spec,pageable);
        List<CourseEvaluate> list = plist.getContent();
        for(CourseEvaluate c:list){
            //课程
            Course course = courseMapper.queryById(c.getCourseId());
            if(course!=null){
                c.setCourseName(course.getTitle());
            }

            //医生
            Doctor doctor = doctorMapper.queryById(c.getDoctorId());
            if(doctor!=null){
            c.setDoctorName(doctor.getName());}
        }
        return plist;
    }



    public Page<CourseEvaluate> pageList1(CourseEvaluate courseEvaluate){
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable=new PageRequest(courseEvaluate.page(), courseEvaluate.getLimit(),sort);  //分页信息
        Specification<CourseEvaluate> spec = new Specification<CourseEvaluate>() {        //查询条件构造

            @Override
            public Predicate toPredicate(Root<CourseEvaluate> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
              /*  if(null != courseEvaluate.getContent()){
                    Path<String> name = root.get("username");
                    predicates.add(cb.like(name, "%"+courseEvaluate.getContent()+"%"));
                }*/
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return courseEvaluateMapper.findAll(spec,pageable);
    }

/*    public Page<List<Map<String, Object>>> pageList2(CourseEvaluateDto courseEvaluateDto){
        Sort sort = new Sort(Sort.Direction.ASC, "create_time");
        Pageable pageable=new PageRequest(courseEvaluateDto.page(), courseEvaluateDto.getLimit(),sort);  //分页信息
        Specification<CourseEvaluateDto> spec = new Specification<CourseEvaluateDto>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<CourseEvaluateDto> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(null != courseEvaluateDto.getCourseName()){
                    Path<String> name = root.get("content");
                    predicates.add(cb.like(name, "%"+courseEvaluateDto.getCourseName()+"%"));
                }
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return courseEvaluateMapper.queryAllEvate(spec, pageable);
    }*/


    @Override
    public Page1<List<Map<String,Object>>> pageList1(CourseEvaluateDto courseEvaluateDto) {
        Page1  page = new Page1<>();
        String hql = " select \n" +
                " cv.*, \n" +
                " c.title, \n" +
                " d.name as docName  \n" +
                "  from  \n" +
                "  tb_course_evaluate cv  \n" +
                "  left join tb_course c on c.id = cv.course_id  \n" +
                "  left join tb_doctor d on d.id = cv.doctor_id where cv.id is not null";
        String countHql = " select count(1) " +
                "  from  \n" +
                "  tb_course_evaluate cv  \n" +
                "  left join tb_course c on c.id = cv.course_id  \n" +
                "  left join tb_doctor d on d.id = cv.doctor_id  where cv.id is not null";
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
            if(null != courseEvaluateDto.getImpression() && !"".equals(courseEvaluateDto.getImpression())){
                str.append("  and cv.impression like concat('%','"+courseEvaluateDto.getImpression()+"','%') ");
                str1.append("  and  cv.impression like concat('%','"+courseEvaluateDto.getImpression()+"','%') ");
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
        page.setSortName("cv.update_time");
        // page.setPageNum(examin.getOffset());
        // page.setPageSize(examin.getLimit());
        return courseEvaluateMapper.findNatPage2(page);
    }






    @Override
    public void saveCourseEvaluate(String body) {
        JSONObject jo = JSONObject.parseObject(body);
        log.info("saveCourseEvaluate  :"+jo.toString());
        // JSONObject jsonObject = JSON.parseObject(body);
        Integer courseId = jo.getInteger("courseId");
        Integer doctorId = jo.getInteger("doctorId");
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
        List<CourseEvaluate>courseEvaluateList=courseEvaluateMapper.queryByCourseIdAnddocId(courseId, doctorId,courseType);
        if(courseEvaluateList.size()>0){
            log.error("this doctor has benn Evaluate the course");
            throw new BuzEx(CCode.COURSE_ALREADY_ENVALATE);
        }

        CourseEvaluate cour=new CourseEvaluate();
        cour.setUpdateTime(new Date());
        cour.setCreateTime(new Date());
        cour.setCourseId(courseId);
        cour.setDoctorId(doctorId);
        cour.setCourseType(courseType);
        //对视频的印象
        JSONArray impressionJa = jo.getJSONArray("impressionJa");
        if (impressionJa != null && impressionJa.size() > 0) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            for (int i = 0; i < impressionJa.size(); i++) {
                JSONObject j = impressionJa.getJSONObject(i);
                if(sb.length() > 0){
                    sb.append( "," ).append(j.getString("impression"));
                }else{
                    sb.append( j.getString("impression") );
                }
                cour.setImpression(sb.toString());

                if(sb1.length() > 0){
                    sb1.append( "," ).append(j.getString("id"));
                }else{
                    sb1.append( j.getString("id") );
                }
                cour.setImpressionId(sb1.toString());
            }
        }
        //期待的课程
        JSONArray courseJa = jo.getJSONArray("courseJa");
        if (courseJa != null && courseJa.size() > 0) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            for (int i = 0; i < courseJa.size(); i++) {
                JSONObject j = courseJa.getJSONObject(i);
                if(sb.length() > 0){
                    sb.append( "," ).append(j.getString("expectCourse"));
                }else{
                    sb.append( j.getString("expectCourse") );
                }
                cour.setExpectCourse(sb.toString());

                if(sb1.length() > 0){
                    sb1.append( "," ).append(j.getString("id"));
                }else{
                    sb1.append( j.getString("id") );
                }
                cour.setExpectCourseId(sb1.toString());
            }

        }
        courseEvaluateMapper.save(cour);
    }

    @Override
    public JSONObject queryAllEva(String body){
        JSONObject jo = JSONObject.parseObject(body);
        log.info("queryAllEva  :"+jo.toString());
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

        JSONObject jor=new JSONObject();
        List<Map<String, Object>> evaluateList = courseEvaluateMapper.queryByCourseId(courseId,courseType);
   /*     Map<String, Object> item = new HashMap<>();
        if (evaluateList != null && evaluateList.size() > 0) {
            for (int i = 0; i < evaluateList.size(); i++) {
            Doctor doc=doctorMapper.getOne(evaluateList.get(i).getDoctorId());
                item.put("docPhoto",doc.getPhoto());
                item.put("expression",evaluateList.get(i).getImpression());
                item.put("course",evaluateList.get(i).getExpectCourse());
                item.put("creatTime",evaluateList.get(i).getCreateTime());
                jor.putAll(item);
            }
            jou.addAll(jor);
        }*/
        List<CourseEvaluate> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
     for(Map<String, Object> map:evaluateList){
         CourseEvaluate courseEvaluate = new CourseEvaluate();
         courseEvaluate.setCourseId((Integer) map.get("course_id"));
         courseEvaluate.setDoctorId((Integer) map.get("doctor_id"));
         courseEvaluate.setCreateTime((Date) map.get("create_time"));
         courseEvaluate.setUpdateTime((Date) map.get("update_time"));
         courseEvaluate.setImpression((String) map.get("impression"));
         courseEvaluate.setCourseType(courseType);
         courseEvaluate.setId((Integer) map.get("id"));
         courseEvaluate.setName((String) map.get("name"));
         courseEvaluate.setPhoto((String) map.get("photo"));
         courseEvaluate.setExpectCourse((String) map.get("expect_course"));
         courseEvaluate.setDatestr(sdf.format(courseEvaluate.getCreateTime()));
         list.add(courseEvaluate);
      }
        jor.put("evaluateList",list);
        return jor;
    }

    @Override
    public CourseEvaluate getOne(Integer id) {
        return courseEvaluateMapper.queryById(id);
    }
}
