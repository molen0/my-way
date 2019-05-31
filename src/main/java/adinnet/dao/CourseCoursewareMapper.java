package adinnet.dao;

import com.adinnet.repository.CourseCourseware;
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
 * Created by wangx on 2018/9/21.
 */
public interface CourseCoursewareMapper  extends Repositor<CourseCourseware, Integer>, JpaSpecificationExecutor<CourseCourseware> {

    @Query("from CourseCourseware c  where c.isDeleted =1 and c.courseId =:courseId order by c.id desc")
    public List<CourseCourseware> findByCourseId(@Param("courseId") Integer courseId);

    @Query("update CourseCourseware c set c.courseId =:courseId  ,c.pdfPath =:pdfPath ,c.name =:name where c.id =:id")
    @Modifying
    public Integer updateById(@Param(value = "courseId") Integer courseId, @Param(value = "pdfPath") String pdfPath, @Param(value = "name") String name, @Param(value = "id") Integer id);

    @Query("update CourseCourseware c set c.isDeleted=0 where c.id =:id")
    @Modifying
    public Integer delById(@Param(value = "id") Integer id);

    @Query(value=" select a.* ,b.title as courseName from tb_course_courseware a right join tb_course b on a.course_id = b.id  where (b.title like CONCAT('%',:courseName,'%')  or :courseName is null or :courseName = '' )and 1=1 and a.is_deleted =1 order by ?#{#pageable}",nativeQuery = true)
    public Page<List<Map<String, Object>>> queryAllCourseware(@Param("courseName") String courseName, Pageable pageable);

    @Query("update CourseCourseware c set c.courseId =:courseId  where c.id =:id")
    @Modifying
    public Integer updateById2(@Param(value = "courseId") Integer courseId, @Param(value = "id") Integer id);

    @Query(value=" SELECT GROUP_CONCAT(s.`name`) `name`FROM " +
            "    tb_course_specialist cs LEFT JOIN tb_specialist s ON s.id = cs.specialist_id " +
            "    WHERE s.is_delete = 1 AND cs.course_id =:courseId ",nativeQuery = true)
    public String  querySpecNameByCourseId(@Param("courseId") Integer courseId);
}
