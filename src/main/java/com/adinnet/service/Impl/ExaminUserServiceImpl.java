package com.adinnet.service.Impl;

import com.adinnet.dao.*;
import com.adinnet.repository.*;
import com.adinnet.repository.vo.AnswerVo;
import com.adinnet.repository.vo.ExaminUserVo;
import com.adinnet.repository.vo.ExaminVo;
import com.adinnet.service.CreaditBookService;
import com.adinnet.service.ExaminTopicService;
import com.adinnet.service.ExaminUserService;
import com.adinnet.service.SemesterService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 用户考试记录
 * @create 2018-09-29 16:20
 **/
@Service
public class ExaminUserServiceImpl implements ExaminUserService{

    @Autowired
    private ExaminUserMapper examinUserMapper;

    @Autowired
    private ExaminTopicService examinTopicService;

    @Autowired
    private ExaminUserRecordMapper examinUserRecordMapper;

    @Autowired
    private SemesterService semesterService;
    @Autowired
    private ExaminMapper examinMapper;
    @Autowired
    private CreaditBookService creaditBookService;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private ExaminUserService examinUserService;
    @Override
    public List<ExaminUser> selectUserId(Integer courseId, String userId) {
        Integer examinId =  examinTopicService.getExaminId(courseId);
        ExaminUser examinUser = new ExaminUser();
        examinUser.setIsDelete(1);
        Doctor doctor = new Doctor();
        doctor.setId(Integer.parseInt(userId));
        examinUser.setDoctor(doctor);
        Examin examin = new Examin();
        examin.setId(examinId);
        examinUser.setExamin(examin);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("examinId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<ExaminUser> ex = Example.of(examinUser, matcher);
        return examinUserMapper.findAll(ex);
    }
    @Override
    public List<ExaminUser> selectUser(Integer courseId, String userId) {
        Integer examinId =  examinTopicService.getExaminId(courseId);
        ExaminUser examinUser = new ExaminUser();
        Doctor doctor = new Doctor();
        doctor.setId(Integer.parseInt(userId));
        examinUser.setDoctor(doctor);
        Examin examin = new Examin();
        examin.setId(examinId);
        examinUser.setExamin(examin);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("examinId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<ExaminUser> ex = Example.of(examinUser, matcher);
        return examinUserMapper.findAll(ex);
    }
    @Override
    @Transactional
    public ExaminUser save(Integer examinId, String userId) {
        ExaminUser examinUser = new ExaminUser();
        examinUser.setIsDelete(1);
        Doctor doctor = new Doctor();
        doctor.setId(Integer.parseInt(userId));
        examinUser.setDoctor(doctor);
        Examin examin = new Examin();
        examin.setId(examinId);
        examinUser.setExamin(examin);
      //  examinUser.setUserId(userId);
        examinUser.setCredit("0.0");
        examinUser.setError(0);
        examinUser.setRight(0);
        //examinUser.setExaminId(examinId);
        examinUser.setCreateTime(new Date());
        examinUser.setUpdateTime(new Date());
       return examinUserMapper.save(examinUser);
    }

    @Override
    public ExaminVo selectExamin(Integer courseId) {
        Integer examinId =  examinTopicService.getExaminId(courseId);
        ExaminVo examinVo = new ExaminVo();
        examinVo.setId(examinId);
        examinVo.setCourseId(courseId);
        List<ExaminTopic> examinTopicList = examinTopicService.listExaminTopic(examinId);
        for(ExaminTopic examinTopic : examinTopicList){
            List<ExaminTopicAnswer> examinTopicAnswerList =examinTopicService.listByAnswer(examinTopic.getId());
           if(examinTopicAnswerList.size()>0){
               examinTopic.setAnswerList(examinTopicAnswerList);
           }
            examinTopic.setTopicAnswer("");
            examinTopic.setRemark("");
        }
        Semester semester = semesterService.queryByCourseId(courseId);
        examinVo.setDate(semester.getMinutes()*(examinTopicList.size()));
        examinVo.setExaminList(examinTopicList);
        return examinVo;
    }

    @Override
    @Transactional
    public Map saveExaminUserVo(ExaminUserVo examinUserVo,JSONArray jsonArray) {
        ExaminUser examinUser = save(examinUserVo.getId(), examinUserVo.getUserId());
        Examin examin = examinMapper.getOne(examinUserVo.getId());
        Semester semester = semesterService.queryByCourseId(examin.getCourse().getId());
        List<ExaminTopic> examinTopicList = examinTopicService.listExaminTopic(examinUserVo.getId());
        List<AnswerVo> answerList = examinUserVo.getAnswerList();
        Integer count = 0;//正确数量
        for (int i=0;i<jsonArray.size();i++) {
            JSONObject job = jsonArray.getJSONObject(i);
            Integer id = job.getInteger("id");
            String  order = job.getString("order");
            ExaminUserRecord examinUserRecord = new ExaminUserRecord();
            examinUserRecord.setCreateTime(new Date());
            examinUserRecord.setUpdateTime(new Date());
            examinUserRecord.setIsDelete(1);
            examinUserRecord.setExaminId(examinUserVo.getId());
            examinUserRecord.setExaminUserId(examinUser.getDoctor().getId().toString());
            ExaminTopic examinTopic1 = new ExaminTopic();
            examinTopic1.setId(id);
            examinUserRecord.setExaminTopic(examinTopic1);
            examinUserRecord.setUserAnswer(order);
            examinUserRecord.setIsFlag(0);
            examinUserRecord.setRecordId(examinUser.getId());
            for (ExaminTopic examinTopic : examinTopicList) {
                if (examinTopic.getId().intValue() == id.intValue() && examinTopic.getTopicAnswer().equals(order)) {
                    count++;
                    examinUserRecord.setIsFlag(1);
                }
                examinUserRecordMapper.save(examinUserRecord);
            }
        }
        updateExaminUser(semester.getId(),examinUser.getId(),count,examinTopicList.size()-count,examin.getCourse().getId());

        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf1 =  new SimpleDateFormat("yyyy-MM-dd");
        Map map = new HashedMap();
        try {
            Date date = sdf1.parse(semester.getTime());
            map.put("date",sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map checkDate(Integer courseId,String userId) {
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf1 =  new SimpleDateFormat("yyyy-MM-dd");
        Semester semester = semesterService.queryByCourseId(courseId);
        Map map = new HashedMap();
        try {
            Date date2 = sdf1.parse(semester.getCourseEndTime());
            String dateStr = sdf.format(date2);
            String dateStr1 = sdf.format(new Date());
            Date date = sdf.parse(dateStr);
            Date date1 = sdf.parse(dateStr1);
            map.put("date",dateStr);
            if(date1.before(date)){
                map.put("flag",0);
            }else {
                map.put("flag",1);
                List<ExaminUser> list = selectUserId(courseId,userId);
                List<ExaminUser> examinUserList = examinUserMapper.examinUserList(Integer.parseInt(userId),semester.getId());
                    int totle = 0;
                    Double add = 0.0;
                    for(ExaminUser examinUser : examinUserList){
                       if("1".equals(examinUser.getExamin().getCourse().getCourseType().toString())){
                           totle = totle + 1;
                           add = add + Double.parseDouble(examinUser.getCredit()) ;
                       }
                    }
                  if(add>=0.5){
                      map.put("totleCredit",add);
                  }
                if(list.size()>0){
                    ExaminUser examinUser = list.get(0);
                    map.put("right",examinUser.getRight());
                    map.put("error",examinUser.getError());
                    map.put("credit",examinUser.getCredit());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    @Transactional
    public void updateExaminUser(Integer semesterId, Integer id, Integer right, Integer error,Integer courseId) {
        String hql = "UPDATE  tb_examin_user SET semester_id = ?,rights = ?,error = ?,credit = ?,update_time = now() WHERE id = ?";
        double credit = right.intValue()>=3?0.5:0;
        examinUserMapper.updateBean(hql,semesterId,right,error,credit,id);
        ExaminUser examinUser = examinUserMapper.getOne(id);
        List<ExaminUser> examinUsers = examinUserService.selectUser(courseId,examinUser.getDoctor().getId().toString());
       for(ExaminUser examUser : examinUsers){
           if(!examUser.getId().toString().equals(id.toString())  ){
               String hql1 ="UPDATE  tb_examin_user SET is_delete = 0 WHERE id = ?";
               if(credit > Double.parseDouble(examUser.getCredit())){
                   examinUserMapper.updateBean(hql1,examUser.getId());
               }else{
                   if(examUser.getRight()<= right){
                       examinUserMapper.updateBean(hql1,examUser.getId());
                   }else{
                       examinUserMapper.updateBean(hql1,id);
                   }
               }
           }
       }
    }

    @Override
    public ExaminUser wrongset(Integer courseId, String userId) {
        List<ExaminUser> examinUserList = selectUserId(courseId,userId);
        ExaminUser examinUser = null;
        if(examinUserList.size()>0){
             examinUser = examinUserList.get(0);
            List<ExaminTopic> examinTopicList1 = examinTopicService.listExaminTopic(examinUser.getExamin().getId());
            List<ExaminUserRecord> examinTopics = new ArrayList<>();
            for(ExaminTopic examinTopic : examinTopicList1){
               List<ExaminUserRecord> examinTopicList = selectRecordId(examinUser.getId(),examinTopic.getId());
                ExaminUserRecord examinUserRecord = new ExaminUserRecord();
                if(examinTopicList.size()>0){
                    examinUserRecord = examinTopicList.get(0);
                }
                examinUserRecord.setExaminTopic(examinTopic);
                List<ExaminTopicAnswer> examinTopicAnswerList =examinTopicService.listByAnswer(examinTopic.getId());
                if(examinTopicAnswerList.size()>0){
                    examinUserRecord.getExaminTopic().setAnswerList(examinTopicAnswerList);
                }
                examinTopics.add(examinUserRecord);
            }
            examinUser.setExaminList(examinTopics);
        }
        return examinUser;
    }

    @Override
    public List<ExaminUserRecord> selectExaminUserRecord(Integer examinId,String userId) {
        ExaminUserRecord examinUserRecord = new ExaminUserRecord();
        examinUserRecord.setExaminId(examinId);
        examinUserRecord.setExaminUserId(userId);
        examinUserRecord.setIsDelete(1);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("examinUserId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("examinId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<ExaminUserRecord> ex = Example.of(examinUserRecord);
        return examinUserRecordMapper.findAll(ex);
    }

    @Override
    public List<ExaminUserRecord> selectRecordId(Integer recordId,Integer examinTopicId) {
        ExaminUserRecord examinUserRecord = new ExaminUserRecord();
        examinUserRecord.setRecordId(recordId);
        examinUserRecord.setIsDelete(1);
        ExaminTopic examinTopic  = new ExaminTopic();
        examinTopic.setId(examinTopicId);
        examinUserRecord.setExaminTopic(examinTopic);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("examinTopic.id", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("recordId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<ExaminUserRecord> ex = Example.of(examinUserRecord);
        return examinUserRecordMapper.findAll(ex);
    }

    @Override
    public PageBean<ExaminUser> pageList(ExaminUser examinUser) {
        PageBean<ExaminUser> page = new PageBean<>();
        String hql = " SELECT teu.* ,ts.id,tc.course_attr,tc.title,tc.course_type,td.name,tuc.credit_url \n" +
            "   FROM tb_examin_user teu " +
           "   LEFT JOIN tb_examin te ON teu.examin_id = te.id " +
            "   LEFT JOIN tb_user_credit tuc ON tuc.examin_user_id = teu.id " +
            "   LEFT JOIN tb_doctor td ON teu.user_id = td.id " +
            "   LEFT JOIN tb_course tc ON te.course_id = tc.id  "+
                "   LEFT JOIN tb_semester ts ON teu.semester_id = ts.id where te.is_delete=1 and teu.is_delete=1 and tc.course_type=1";
        String countHql = "SELECT count(1)  from (SELECT  ts.id" +
                "   FROM tb_examin_user teu " +
                "   LEFT JOIN tb_examin te ON teu.examin_id = te.id " +
                "   LEFT JOIN tb_user_credit tuc ON tuc.examin_user_id = teu.id " +
                "   LEFT JOIN tb_doctor td ON teu.user_id = td.id " +
                "   LEFT JOIN tb_course tc ON te.course_id = tc.id  "+
        "   LEFT JOIN tb_semester ts ON teu.semester_id = ts.id where te.is_delete=1 and teu.is_delete=1 and tc.course_type=1";
        StringBuffer str = new StringBuffer(hql);
        StringBuffer str1 = new StringBuffer(countHql);
        if(null != examinUser){
            if(null != examinUser.getDoctorName() && !"".equals(examinUser.getDoctorName())){
                str.append("  and td.name like concat('%','"+examinUser.getDoctorName()+"','%') ");
                str1.append("  and  td.name like concat('%','"+examinUser.getDoctorName()+"','%') ");
            }
            if(null != examinUser.getDoctorCompany() && !"".equals(examinUser.getDoctorCompany())){
                str.append("  and td.company like concat('%','"+examinUser.getDoctorCompany()+"','%') ");
                str1.append("  and  td.company like concat('%','"+examinUser.getDoctorCompany()+"','%') ");
            }
            if(null != examinUser.getSemesterName() && !"".equals(examinUser.getSemesterName())){
                str.append("  and ts.name like concat('%','"+examinUser.getSemesterName()+"','%') ");
                str1.append("  and  ts.name like concat('%','"+examinUser.getSemesterName()+"','%') ");
            }
        }
        str.append("  group by  teu.user_id,teu.semester_id");
        str1.append(" group by  teu.user_id ,teu.semester_id ) temp ");
        page.setHql(str.toString());
        page.setCountHql(str1.toString());
        page.setSortName("teu.update_time");
        page.setPageNum(examinUser.getOffset());
        page.setPageSize(examinUser.getLimit());
        PageBean<ExaminUser> page1 = new PageBean<>();
        page1 = examinUserMapper.findNatPage(page);
        List<ExaminUser> list = page1.getDatas();
        for(ExaminUser examinUser1 : list){
            examinUser1.setSemesterCredit(examinAllCredit(examinUser1.getDoctor().getId(),examinUser1.getSemester().getId()));
        }
        return  page1;
    }

    @Override
    public List<ExaminUser> examinUserList(Integer userId, Integer semesterId) {
        return examinUserMapper.examinUserList(userId,semesterId);
    }
    public String  examinAllCredit(Integer userId, Integer semesterId) {
        List<ExaminUser> list = examinUserMapper.examinUserList(userId,semesterId);
        Double totle = 0.0;
        for(ExaminUser examinUser : list){
            totle += Double.parseDouble(examinUser.getCredit());
        }
        return totle.toString().substring(0,3);
    }

    @Override
    public Map selectCradit(Integer courseId, Integer userId) {
        Course course = courseMapper.getOne(courseId);
        List<ExaminUser> list = examinUserService.examinUserList(userId,course.getSemesterId());
        Map<String,Object> map = new HashedMap();
        try{
            int totle = 0;
            Double add = 0.0;
            for(ExaminUser examinUser : list){
                if("1".equals(examinUser.getExamin().getCourse().getCourseType().toString())){
                    totle = totle + 1;
                    add = add + Double.parseDouble(examinUser.getCredit()) ;
                }
            }
            if(add >=0.5) {
                SimpleDateFormat sdf =  new SimpleDateFormat("yyyy/MM/dd");
                SimpleDateFormat sdf1 =  new SimpleDateFormat("yyyy-MM-dd");
                Semester semester = semesterService.queryByCourseId(courseId);
                Date date2 = sdf1.parse(semester.getTime());
                String dateStr = sdf.format(date2);
                String dateStr1 = sdf.format(new Date());
                Date date = sdf.parse(dateStr);
                Date date1 = sdf.parse(dateStr1);
                map.put("date",dateStr);
                if(date1.before(date)){
                    map.put("flag","0");
                }else {
                    map.put("flag", "1");
                }
                Integer type = 0;
                if("1".equals(list.get(0).getDoctor().getProperty().toString())){
                    type =1;
                }
                CreaditBook creaditBook = creaditBookService.getBysemesterId(type);
                if (null != creaditBook) {
                    List<String> list1 = new ArrayList<>();
                    if (null != creaditBook.getChapter1() && !"".equals(creaditBook.getChapter1())) {
                        list1.add(creaditBook.getChapter1());
                    }
                    if (null != creaditBook.getChapter2() && !"".equals(creaditBook.getChapter2())) {
                        list1.add(creaditBook.getChapter2());
                    }
                    List<String> list2 = new ArrayList<>();
                    if (null != creaditBook.getHospital1() && !"".equals(creaditBook.getHospital1())) {
                        list2.add(creaditBook.getHospital1());
                    }
                    if (null != creaditBook.getHospital2() && !"".equals(creaditBook.getHospital2())) {
                        list2.add(creaditBook.getHospital2());
                    }
                    Map map1 = new HashedMap();
                    map1.put("headlines", creaditBook.getHeadlines());
                    map1.put("semesterTitle", creaditBook.getSemesterTitle());
                    map1.put("doctorName", list.get(0).getDoctor().getName());
                    map1.put("proveCon", creaditBook.getProveCon());
                    map1.put("doctorCredits", (add + "").substring(0, 3));
                    map1.put("HospitalList", list2);
                    map1.put("chapterList", list1);
                    map1.put("issueDate", creaditBook.getIssueDate());
                    map1.put("creaditName", creaditBook.getCreaditName());
                    map.put("result", map1);
                }
        }else{
            map.put("flag","-1");
        }
        map.put("courseType",course.getCourseType());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }

}
