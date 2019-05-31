package com.adinnet.service.Impl;

import com.adinnet.dao.*;
import com.adinnet.ex.BuzEx;
import com.adinnet.http.HttpUtils;
import com.adinnet.repository.*;
import com.adinnet.repository.vo.CourseVo;
import com.adinnet.response.code.CCode;
import com.adinnet.service.CourseOutlineService;
import com.adinnet.service.SpecialistService;
import com.adinnet.utils.FileSizeUtils;
import com.adinnet.utils.ReturnUtil;
import com.adinnet.utils.qiniu.QiniuUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.*;

/**
 * @author W。
 * @Description: 课程大纲
 * @create 2018-09-18 11:16
 **/
@Service
public class CourseOutlineServiceImpl implements CourseOutlineService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseOutlineMapper courseOutlineMapper;

    @Autowired
    private CourseStudyRateMapper courseStudyRateMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private SpecialistService specialistService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Map queryByCourseId(Integer courseId,Integer doctorId) {
        Map map = new HashMap<>();
        Doctor doctor = doctorMapper.queryById(doctorId);
        if(doctor !=null){
            Course course = courseMapper.queryById(courseId);
            List<CourseOutline> list = courseOutlineMapper.queryByCourseId(courseId);
            if(doctorId != null){
                for(CourseOutline courseOutline:list){
                    //根据大纲id和用户id查询学习进度
                    CourseStudyRate courseStudyRate = courseStudyRateMapper.queryByUserOutline(courseOutline.getId(),doctorId);
                    if(courseStudyRate !=null){
                        courseOutline.setRate(courseStudyRate.getRate());
                        courseOutline.setNums(courseStudyRate.getNums());
                        courseOutline.setTime(courseStudyRate.getTime());
                    }else{
                        courseOutline.setRate("0");
                        courseOutline.setTime("0");
                        courseOutline.setNums(0);
                    }
                }
            }
            Integer num = course.getStudyNum();
            num +=1;
            courseMapper.updateCourseNum(num,courseId);
            map.put("num",num);
            map.put("course",course);
            map.put("list",list);
        }else{
            //CCode.C_DOCTOR_IS_NULL
            map.put("code","4055");
        }
        return map;
    }

    @Override
    public Page<CourseOutline> pageList( CourseOutline courseOutline){
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        Pageable pageable=new PageRequest(courseOutline.page(), courseOutline.getLimit(),sort);  //分页信息
        Specification<CourseOutline> spec = new Specification<CourseOutline>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<CourseOutline> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return courseOutlineMapper.findCourseOutlinePage1(spec,pageable);
    }

    @Override
    public CourseOutline getOne(Integer id) {
        return courseOutlineMapper.queryById(id);
    }

    @Override
    public ModelMap saveCourseOutline(CourseOutline courseOutline, MultipartFile file) throws IOException, HttpException {
        if(file != null){
            int flag =judgeFile(file);
            if(flag == 1){
                return ReturnUtil.Error("视频不能大于100M", null, null);
            }else if(flag == 2){
                return ReturnUtil.Error("请上传视频文件", null, null);
            }
            String videoPath = QiniuUtils.upload(file, "courseVideo");
            // 根据响应获取文件大小
            String result = HttpUtils.httpGet(videoPath + "?avinfo", "utf-8");
            JSONObject streams = JSONObject.parseObject(result);
            JSONArray jsonArray = streams.getJSONArray("streams");
            String duration = jsonArray.getJSONObject(0).getString("duration");
            double temp =  Math.floor(Double.parseDouble(duration));
            String videoTime = String.valueOf((int)temp);
            courseOutline.setVideoPath(videoPath);
            courseOutline.setVideoTime(videoTime);
        }
        courseOutline.setStatus(1);
        courseOutline.setCreateTime(new Date());
        courseOutline.setUpdateTime(new Date());
        Course course = new Course();
        course.setId(courseOutline.getCourseId());
        courseOutline.setCourse(course);
        courseOutlineMapper.save(courseOutline);
        return ReturnUtil.Success("操作成功", null, "/api/courseOutline/index");
    }

    @Override
    public ModelMap updateCourseOutline(CourseOutline courseOutline, MultipartFile file) throws IOException, HttpException {
        if(null != file && file.getOriginalFilename() !=null &&!"".equals(file.getOriginalFilename())){
            int flag =judgeFile(file);
            if(flag == 1){
                return ReturnUtil.Error("视频不能大于100M", null, null);
            }else if(flag == 2){
                return ReturnUtil.Error("请上传视频文件", null, null);
            }
            String videoPath = QiniuUtils.upload(file, "courseVideo");
            // 根据响应获取文件大小
            String result = HttpUtils.httpGet(videoPath + "?avinfo", "utf-8");
            JSONObject streams = JSONObject.parseObject(result);
            JSONArray jsonArray = streams.getJSONArray("streams");
            String duration = jsonArray.getJSONObject(0).getString("duration");
            double temp =  Math.floor(Double.parseDouble(duration));
            String videoTime = String.valueOf((int)temp);
            courseOutline.setVideoPath(videoPath);
            courseOutline.setVideoTime(videoTime);
        }else{
            CourseOutline original = courseOutlineMapper.queryById(courseOutline.getId());
            courseOutline.setVideoTime(original.getVideoTime());
            courseOutline.setVideoPath(original.getVideoPath());
        }
        courseOutline.setUpdateTime(new Date());
        Course course = new Course();
        course.setId(courseOutline.getCourseId());
        courseOutline.setCourse(course);
        courseOutlineMapper.saveAndFlush(courseOutline);
        return ReturnUtil.Success("操作成功", null, "/api/courseOutline/index");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void del(Integer id) {
        courseOutlineMapper.updateStatusById(id);
        //courseOutlineMapper.deleteById(id);
    }

    @Override
    public  Page<List<Map<String, CourseOutline>>> pageList1(CourseOutline courseOutline) {
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        Pageable pageable=new PageRequest(courseOutline.page(), courseOutline.getLimit(),sort);  //分页信息
        Specification<CourseOutline> spec = new Specification<CourseOutline>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<CourseOutline> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(null != courseOutline.getName()){
                    Path<String> name = root.get("content");
                    predicates.add(cb.like(name, "%"+courseOutline.getName()+"%"));
                }
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        Page<List<Map<String, CourseOutline>>> page = courseOutlineMapper.findCourseOutlinePage(spec, pageable);
        return page;
    }

    public  Page<List<Map<String, CourseOutline>>> pageList2(CourseOutline courseOutline) {
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        Pageable pageable=new PageRequest(courseOutline.page(), courseOutline.getLimit(),sort);  //分页信息
        Specification<CourseOutline> spec = new Specification<CourseOutline>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<CourseOutline> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(null != courseOutline.getName()){
                    Path<String> name = root.get("content");
                    predicates.add(cb.like(name, "%"+courseOutline.getName()+"%"));
                }
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        Page<List<Map<String, CourseOutline>>> page = courseOutlineMapper.findCourseOutlinePage(spec, pageable);
        return page;
    }

    @Override
    public PageBean<CourseOutline> pageList3(CourseOutline courseOutline) {
        PageBean<CourseOutline> page = new PageBean<>();
        String hql = "select co.*,c.title,tem.specialistName " +
                "from tb_course_outline co " +
                "left join tb_course c on co.course_id = c.id  " +
                "left join (select group_concat(ts.`name`) `specialistName`,tcs.course_id from tb_course_specialist tcs " +
                "left join tb_specialist ts on ts.id = tcs.specialist_id group by tcs.course_id)tem on tem.course_id=co.course_id " +
                "where co.status=1";
        String countHql = "select count(1) " +
                "from tb_course_outline co " +
                "left join tb_course c on co.course_id = c.id " +
                "left join (select group_concat(ts.`name`) `specialistName`,tcs.course_id from tb_course_specialist tcs " +
                "left join tb_specialist ts on ts.id = tcs.specialist_id group by tcs.course_id)tem on tem.course_id=co.course_id " +
                "where co.status=1";
        StringBuffer str = new StringBuffer(hql);
        StringBuffer str1 = new StringBuffer(countHql);
        if(null != courseOutline){
            if(null != courseOutline.getName() && !"".equals(courseOutline.getName())){
                str.append("  and co.name like concat('%','"+courseOutline.getName()+"','%') ");
                str1.append("  and  co.name like concat('%','"+courseOutline.getName()+"','%') ");
            }
        }
        if(null != courseOutline){
            if(null != courseOutline.getCourseAttr() && !"".equals(courseOutline.getCourseAttr())){
                str.append("  and c.course_attr = "+courseOutline.getCourseAttr()+" ");
                str1.append("  and c.course_attr = "+courseOutline.getCourseAttr()+" ");
            }
        }
        if(null != courseOutline){
            if(null != courseOutline.getSpecialistName() && !"".equals(courseOutline.getSpecialistName())){
                str.append("  and tem.specialistName like concat('%','"+courseOutline.getSpecialistName()+"','%') ");
                str1.append("  and  tem.specialistName like concat('%','"+courseOutline.getSpecialistName()+"','%') ");
            }
        }
        page.setHql(str.toString());
        page.setCountHql(str1.toString());
        page.setSortName("co.update_time desc");
        page.setPageNum(courseOutline.getOffset());
        page.setPageSize(courseOutline.getLimit());
        page = courseOutlineMapper.findNatPage(page);
        List<CourseOutline>  courseOutlines = page.getDatas();
        for(CourseOutline courseOut: courseOutlines){
            courseOut.setSpecialistName( specialistService.querySpecialistName(courseOut.getCourseId())
            );
        }
        return page;
    }

    @Override
    public PageBean<CourseOutline> pageList4(CourseOutline courseOutline) {
        PageBean<CourseOutline> page = new PageBean<>();
        String hql = "select co.*,c.* " +
                "from tb_course_outline co " +
                "left join tb_course c on co.course_id = c.id where 1=1";
        String countHql = "select count(1) " +
                "from tb_course_outline co " +
                "left join tb_course c on co.course_id = c.id where 1=1";
        StringBuffer str = new StringBuffer(hql);
        StringBuffer str1 = new StringBuffer(countHql);
        if(null != courseOutline){
            if(null != courseOutline.getName() && !"".equals(courseOutline.getName())){
                str.append("  and co.name like concat('%','"+courseOutline.getName()+"','%') ");
                str1.append("  and  co.name like concat('%','"+courseOutline.getName()+"','%') ");
            }

            str.append(" group by co.course_id");
            str1.append(" group by co.course_id");
        }
        page.setHql(str.toString());
        page.setCountHql(str1.toString());
        page.setSortName("co.update_time");
        return courseOutlineMapper.findNatPage(page);
    }

    @Override
    public boolean startExam(Integer courseId, Integer doctorId) {
        Doctor doctor = doctorMapper.queryById(doctorId);
        if(doctor !=null){
            List<CourseOutline> list = courseOutlineMapper.queryByCourseId(courseId);
            for(CourseOutline courseOutline:list){
                CourseStudyRate courseStudyRate = courseStudyRateMapper.queryByUserOutline(courseOutline.getId(), doctorId);
                if(courseStudyRate != null){
                    if(!"100".equals(courseStudyRate.getRate())){
                        return false;
                    }
                }else{
                    return false;
                }
            }
        }else{
            return false;
        }
        return true;
    }

    public int judgeFile(MultipartFile file){
        int flag = 0;
        String type =  file.getContentType();
        if(!type.contains("video")){
            flag = 2;
            //return ReturnUtil.Error("请上传视频文件", null, null);
        }
        double size = FileSizeUtils.getFileSize(file);
        if(size>1024*100){
            flag = 1;
            //return ReturnUtil.Error("视频不能大于100M", null, null);
        }
        return flag;
    }
}
