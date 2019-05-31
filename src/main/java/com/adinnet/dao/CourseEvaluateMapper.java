package com.adinnet.dao;

import com.adinnet.repository.CourseEvaluate;
import com.adinnet.repository.CourseOutline;
import com.adinnet.repository.dto.CourseEvaluateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2018/9/18.
 */
    //public interface CourseEvaluateMapper extends JpaRepository<CourseEvaluate, Integer>, JpaSpecificationExecutor<CourseEvaluate> {
    public interface CourseEvaluateMapper extends Repositor<CourseEvaluate, Integer>,JpaSpecificationExecutor<CourseEvaluate>{

/**
     * 查询所有评价 courseId doctorId
     * @param courseId 课程id
     * @return
     */

    @Query("from CourseEvaluate co where co.courseId = :courseId and co.doctorId = :doctorId and co.courseType = :courseType")
    public List<CourseEvaluate> queryByCourseIdAnddocId(@Param(value = "courseId") Integer courseId, @Param(value = "doctorId") Integer doctorId, @Param(value = "courseType") Integer courseType);

    /**
     * 查询所有评价
     * @return
     */
    //todo
    @Query(value = " SELECT\n" +
            "\t e.*, d.photo AS photo, " +
            "\t d.name AS name \n" +
            " FROM\n" +
            "\t tb_course_evaluate e\n" +
            " LEFT JOIN tb_doctor d ON d.id = e.doctor_id where e.course_id = :courseId and e.course_type = :courseType order by e.create_time desc \n" +
            " ",nativeQuery = true)
    public List<Map<String, Object>> queryByCourseId(@Param(value = "courseId") Integer courseId, @Param(value = "courseType") Integer courseType);



    @Query(value="  select cv.*, d.`name` as \"docName\",c.title from tb_course_evaluate cv left join tb_course c on c.id = cv.course_id left join tb_doctor d on d.id = cv.doctor_id",nativeQuery = true)
    public Page<List<Map<String, Object>>> queryAllEvate2(Specification<CourseEvaluate> spec, org.springframework.data.domain.Pageable pageable);


    @Query(value="select" +
            " cv.*," +
            " c.title," +
            "  d.name as docName " +
            " from " +
            " tb_course_evaluate cv " +
            " left join tb_course c on c.id = cv.course_id " +
            " left join tb_doctor d on d.id = cv.doctor_id " +
            " ",nativeQuery = true)
    public Page<List<Map<String, Object>>> queryAllEvate(@Param(value = "spec") Specification<CourseEvaluateDto> spec, org.springframework.data.domain.Pageable pageable);




    @Query("from CourseEvaluate co where co.id = :Id ")
    public CourseEvaluate queryById(@Param(value = "Id") Integer Id);

    //统计：视频印象
    @Query(value = "select count(1) from tb_course_evaluate where impression_id like concat('%',:tagId,'%')",nativeQuery = true)
    public BigInteger findNumsByTagId1(@Param(value = "tagId") Integer tagId);

    //统计：期待课程
    @Query(value = "select count(1) from tb_course_evaluate where expect_course_id like concat('%',:tagId,'%')",nativeQuery = true)
    public BigInteger findNumsByTagId2(@Param(value = "tagId") Integer tagId);
}
