package com.adinnet.dao;

import com.adinnet.repository.CourseOutline;
import com.adinnet.repository.Doctor;
import com.adinnet.repository.Examin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/18.
 */
public interface CourseOutlineMapper extends  Repositor<CourseOutline, Integer>,JpaSpecificationExecutor<CourseOutline>{

    /**
     * 查询课程大纲
     * @param courseId 课程id
     * @return
     */

    @Query(value="select * from tb_course_outline co where co.course_id = :courseId order by co.sort asc",nativeQuery = true)
    /*@Query("from CourseOutline co where co.courseId = :courseId order by co.sort asc")*/
    public List<CourseOutline> queryByCourseId(@Param(value = "courseId") Integer courseId);

    @Query(value="select co.*,c.title from tb_course_outline co left join tb_course c on co.course_id = c.id",nativeQuery = true)
    public Page<CourseOutline> findCourseOutlinePage1(Specification<CourseOutline> spec, Pageable pageable);

    @Query(value="select co.*,c.title from tb_course_outline co left join tb_course c on co.course_id = c.id",countQuery = "select count(1) from tb_course_outline co left join tb_course c on co.course_id = c.id",nativeQuery = true)
    public Page<List<Map<String, CourseOutline>>> findCourseOutlinePage(Specification<CourseOutline> spec, Pageable pageable);

    @Query("from CourseOutline c where c.id = :id")
    public CourseOutline queryById(@Param(value = "id") Integer id);


    @Query("update CourseOutline set status = 2 where id = :id")
    @Modifying
    public void updateStatusById(@Param(value = "id") Integer id);
}
