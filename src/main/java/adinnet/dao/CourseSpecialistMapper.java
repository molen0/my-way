package adinnet.dao;

import com.adinnet.repository.CourseSpecialist;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by adinnet on 2018/10/9.
 */
public interface CourseSpecialistMapper extends Repositor<CourseSpecialist, Integer>, JpaSpecificationExecutor<CourseSpecialist> {
    /**
     * 删除专家
     * @param courseId
     * @return
     */
    @Query(value="delete from tb_course_specialist where course_id = :courseId",nativeQuery = true)
    @Modifying
    public Integer deleteByCourseId(@Param(value = "courseId") Integer courseId);
}
