package adinnet.service;

import com.adinnet.repository.CourseStudyRate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2018/9/18.
 */
public interface CourseStudyRateService {

    /**
     * 修改课程大纲学习进度
     * @param courseId 课程id
     * @param doctorId 医生id
     * @param outlineId 课程大纲id
     * @param rate 进度
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String updateStudyRate(Integer courseId, Integer doctorId, Integer outlineId, String rate);
}
