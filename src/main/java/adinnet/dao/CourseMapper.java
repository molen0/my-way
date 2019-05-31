package adinnet.dao;

import com.adinnet.repository.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by RuanXiang on 2018/9/19.
 */
public interface CourseMapper extends Repositor<Course, Integer>, JpaSpecificationExecutor<Course> {
    /**
     * 查询列表
     * @param property
     * @param courseType
     * @param semesterId
     * @return
     */
    @Query("from Course c where c.courseAttr = :property and c.courseType = :courseType and c.semesterId = :semesterId ")
    public List<Course> queryList(@Param(value = "property") Integer property, @Param(value = "courseType") Integer courseType, @Param(value = "semesterId") Integer semesterId);

    @Query("update Course c set c.studyNum=:num where c.id =:id")
    @Modifying
    public void updateCourseNum(@Param("num") Integer num, @Param("id") Integer id);

    @Query(value = "SELECT a.* FROM tb_course a LEFT JOIN tb_examin e ON e.course_id = a.id  WHERE  e.course_id is NULL and a.status=1",nativeQuery = true)
    public List<Course> queryByExam();
    /**
     * 查询课程
     * @param id
     * @return
     */
    @Query("from Course c where c.id = :id")
    public Course queryById(@Param(value = "id") Integer id);

    @Query(value="select * from tb_course c",nativeQuery = true)
    public List<Course> queryListAlls();

    @Query("from Course c where c.title = :title")
    public Course queryBytitle(@Param(value = "title") String title);


    @Query(value="select * from tb_course c where c.title like CONCAT('%',:title,'%')  order by c.update_time desc limit 1",nativeQuery = true)
    public Course queryBytitle1(@Param(value = "title") String title);


    @Query("from Course c where  c.title like CONCAT('%',:title,'%')")
    public List<Course> queryBytitles(@Param(value = "title") String title);



    /**
     * 删除课程
     * @param id
     * @return
     */
    @Query("update Course set status = 2 where id = :id")
    @Modifying
    public void updateById(@Param(value = "id") Integer id);

    /**
     * 查询学期课程
     * @param semesterId
     * @return
     */
    @Query("from Course c where c.semesterId = :semesterId and c.status<>2")
    public List<Course> queryBySemesterId(@Param(value = "semesterId") Integer semesterId);

    /**
     * 查询在用课程列表
     * @param courseType
     * @param semesterId
     * @return
     */
    @Query(value="select * from tb_course c where c.status=1 and c.course_type = :courseType and c.semester_id = :semesterId order by course_attr asc,indexs asc limit :counts",nativeQuery = true)
    public List<Course> queryListByParamAsc(@Param(value = "courseType") Integer courseType, @Param(value = "semesterId") Integer semesterId, @Param(value = "counts") Integer counts);

    /**
     * 查询在用课程列表
     * @param courseType
     * @param semesterId
     * @return
     */
    @Query(value="select * from tb_course c where c.status=1 and c.course_type = :courseType and c.semester_id = :semesterId order by course_attr desc,indexs asc limit :counts",nativeQuery = true)
    public List<Course> queryListByParamDesc(@Param(value = "courseType") Integer courseType, @Param(value = "semesterId") Integer semesterId, @Param(value = "counts") Integer counts);

    /**
     * 查询所有未添加考试答案资源的课程
     * @return
     */
    @Query(value="select * from tb_course c where c.status=1 and(c.id not in (SELECT b.course_id from tb_exam_answer_resource b WHERE b.is_deleted =1)) or (c.id = :courseId  )",nativeQuery = true)
    public List<Course> queryListAllExists(@Param(value = "courseId") Integer courseId);
    @Query(value="select * from tb_course c where c.status=1 and c.id not in (SELECT b.course_id from tb_exam_answer_resource b WHERE b.is_deleted =1) ",nativeQuery = true)
    public List<Course> queryListAllExists2();
    @Query(value="select * from tb_course c where c.status=1 ",nativeQuery = true)
    public List<Course> queryListAll();

    /**
     * 查询在用课程列表
     * @param courseType
     * @param semesterId
     * @return
     */
    @Query(value="select * from tb_course c where c.status=1 and c.course_type = :courseType and c.semester_id = :semesterId and c.course_attr = :courseAttr order by indexs asc limit :counts",nativeQuery = true)
    public List<Course> queryListByParam(@Param(value = "courseType") Integer courseType, @Param(value = "semesterId") Integer semesterId, @Param(value = "counts") Integer counts, @Param(value = "courseAttr") Integer courseAttr);

    /**
     * 更新课程授课讲师
     * @param id
     * @return
     */
    @Query("update Course set specialistNames = :specialistNames where id = :id")
    @Modifying
    public void updateSpecialistNames(@Param(value = "specialistNames") String specialistNames, @Param(value = "id") Integer id);
}
