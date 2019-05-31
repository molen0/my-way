package adinnet.dao;

import com.adinnet.repository.CourseStudyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Administrator on 2018/9/18.
 */
public interface CourseStudyRateMapper extends JpaRepository<CourseStudyRate, Integer>, JpaSpecificationExecutor<CourseStudyRate> {

    /**
     * 根据大纲id和用户id查询学习进度
     * @param outlineId 大纲id
     * @param doctorId 医生id
     * @return
     */
    @Query("from CourseStudyRate csr where csr.outlineId =:outlineId and doctorId=:doctorId")
    public CourseStudyRate queryByUserOutline(@Param(value = "outlineId") Integer outlineId, @Param(value = "doctorId") Integer doctorId);

    /**
     * 执行修改大纲学习进度
     * @param rate 进度
     * @param time 次数
     */
    @Query("update CourseStudyRate csr  set csr.rate =:rate,csr.time=:time,csr.nums =:nums,csr.updateTime=now() where csr.id=:id")
    @Modifying
    public void updateCourseStudyRate(@Param("rate") String rate, @Param("time") String time, @Param("nums") Integer nums, @Param("id") Integer id);
}
