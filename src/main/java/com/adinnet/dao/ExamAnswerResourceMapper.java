package com.adinnet.dao;

import com.adinnet.repository.CourseOutline;
import com.adinnet.repository.ExamAnswerResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/9.
 */
public interface ExamAnswerResourceMapper extends Repositor<ExamAnswerResource, Integer>, JpaSpecificationExecutor<ExamAnswerResource> {

    @Query(value = " select a.* ,b.title as courseName from tb_exam_answer_resource a right join tb_course b on a.course_id = b.id  where (b.title like CONCAT('%',:courseName,'%')  or :courseName is null or :courseName= '' )and 1=1 and a.is_deleted =1 order by ?#{#pageable}", nativeQuery = true)
    public Page<List<Map<String, Object>>> queryAllResource(@Param("courseName") String courseName, Pageable pageable);

    @Query("from ExamAnswerResource e where e.id = :id")
    public ExamAnswerResource queryById(@Param(value = "id") Integer id);

    @Query("from ExamAnswerResource e where e.courseId = :courseId and e.isDeleted =1")
    public List<ExamAnswerResource> findByCourseId(@Param(value = "courseId") Integer courseId);
    @Query("update ExamAnswerResource c set c.courseId =:courseId ,c.videoPath =:videoPath ,c.videoTime =:videoTime ,c.pdfPath =:pdfPath,c.pdfName =:pdfName  where c.id =:id")
    @Modifying
    public Integer updateById(@Param(value = "courseId") Integer courseId, @Param(value = "videoPath") String videoPath, @Param(value = "videoTime") String videoTime, @Param(value = "pdfPath") String pdfPath, @Param(value = "pdfName") String pdfName, @Param(value = "id") Integer id);

    @Query("update ExamAnswerResource c set c.courseId =:courseId ,c.videoPath =:videoPath ,c.videoTime =:videoTime   where c.id =:id")
    @Modifying
    public Integer updateById2(@Param(value = "courseId") Integer courseId, @Param(value = "videoPath") String videoPath, @Param(value = "videoTime") String videoTime, @Param(value = "id") Integer id);


    @Query("update ExamAnswerResource  c set c.isDeleted=0 where c.id =:id")
    @Modifying
    public Integer delById(@Param(value = "id") Integer id);
}
