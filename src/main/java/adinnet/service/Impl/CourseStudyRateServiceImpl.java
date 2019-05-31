package adinnet.service.Impl;

import com.adinnet.dao.CourseOutlineMapper;
import com.adinnet.dao.CourseStudyRateMapper;
import com.adinnet.dao.DoctorMapper;
import com.adinnet.ex.BuzEx;
import com.adinnet.ex.Ex;
import com.adinnet.repository.CourseOutline;
import com.adinnet.repository.CourseStudyRate;
import com.adinnet.repository.Doctor;
import com.adinnet.response.code.CCode;
import com.adinnet.service.CourseStudyRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 2018/9/18.
 */
@Service
@Slf4j
public class CourseStudyRateServiceImpl implements CourseStudyRateService {

    @Autowired
    private CourseStudyRateMapper courseStudyRateMapper;

    @Autowired
    private CourseOutlineMapper courseOutlineMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Override
    public synchronized String updateStudyRate(Integer courseId, Integer doctorId, Integer outlineId, String time) {
        String code = "200";
        Doctor doctor = doctorMapper.queryById(doctorId);
        if(doctor !=null){
            CourseStudyRate courseStudyRate = courseStudyRateMapper.queryByUserOutline(outlineId, doctorId);
            CourseOutline courseOutline = courseOutlineMapper.queryById(outlineId);
            if(courseStudyRate !=null){//执行更新
                doUpdateStudyRate(time,courseOutline.getVideoTime(),courseStudyRate.getNums(),courseStudyRate);
            }else{//执行保存
                doSaveStudyRate(courseId,doctorId,outlineId,time,courseOutline.getVideoTime());
            }
        }else{
            code = CCode.C_DOCTOR_IS_NULL.getMessage();
        }
        return code;
    }

    /**
     * 执行保存大纲学习进度
     * @param courseId 课程id
     * @param doctorId 医生id
     * @param outlineId 大纲id
     * @param time 观看时长
     */
    public void doSaveStudyRate(Integer courseId, Integer doctorId, Integer outlineId, String time,String videoTime) {
        Integer nums = 0;
        CourseStudyRate courseStudyRate = new CourseStudyRate();
        courseStudyRate.setCourseId(courseId);
        courseStudyRate.setDoctorId(doctorId);
        courseStudyRate.setOutlineId(outlineId);
        double readTime = Double.parseDouble(time);
        double allTime = Double.parseDouble(videoTime);
        String rate = readTime<allTime?String.valueOf((int)Math.floor(readTime/allTime*100)):"100";
        if(readTime>allTime){
            log.info("=========观看时长大于总时长===========观看时长readTime："+readTime+",总时长allTime:"+allTime);
        }
        if("100".equals(rate)){
            nums=1;
        }
        courseStudyRate.setRate(rate);
        courseStudyRate.setTime(time);
        courseStudyRate.setNums(nums);
        courseStudyRate.setCreateTime(new Date());
        courseStudyRate.setUpdateTime(new Date());
        String uniqueKey = courseId+","+outlineId+","+doctorId;
        courseStudyRate.setUniqueKey(uniqueKey);
        log.info("---------此处建立唯一索引："+uniqueKey+"----------");
        log.info("-----------执行添加学习进度时间："+System.currentTimeMillis()+"。大纲id："+outlineId+";医生id："+doctorId);
        try{
            courseStudyRateMapper.save(courseStudyRate);
        }catch (Exception e){
            log.info("-------------------      数据已经存在，无法插入！     ---------------------");
            return;
        }
        log.info("-----------添加成功了：大纲id："+outlineId+";医生id："+doctorId);
    }

    /**
     * 执行修改大纲学习进度
     * @param time 观看时长
     */
    public void doUpdateStudyRate(String time,String videoTime,Integer nums,CourseStudyRate courseStudyRate) {
        double readTime = Double.parseDouble(time);
        double allTime = Double.parseDouble(videoTime);
        String rate = readTime<allTime?String.valueOf((int)Math.floor(readTime/allTime*100)):"100";
        String localRate = courseStudyRate.getRate();
        /****************     time和rate，保留最大   ************/
        rate = Integer.parseInt(localRate) > Integer.parseInt(rate) ? localRate:rate;
        String localTime = courseStudyRate.getTime();
        time = Integer.parseInt(time)>Integer.parseInt(localTime)?time:localTime;
        /****************     end   ************/
        if(readTime>allTime){
            log.info("=========观看时长大于总时长===========观看时长readTime："+readTime+",总时长allTime:"+allTime);
        }
        if("100".equals(rate)){
            nums=1;
        }
        courseStudyRateMapper.updateCourseStudyRate(rate,time,nums,courseStudyRate.getId());
    }
}
