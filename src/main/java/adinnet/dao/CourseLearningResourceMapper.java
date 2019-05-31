package adinnet.dao;

import com.adinnet.repository.CourseEvaluate;

import com.adinnet.repository.CourseLearningResource;
import com.adinnet.repository.CourseOutline;
import com.adinnet.repository.dto.CourseEvaluateDto;
import com.adinnet.repository.dto.CourseLearningResourceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Created by wangx on 2018/9/20. Repositor<CourseOutline, Integer>,JpaSpecificationExecutor<CourseOutline>
 */
public interface CourseLearningResourceMapper  extends Repositor<CourseLearningResource, Integer>, JpaSpecificationExecutor<CourseLearningResource> {


    @Query("from CourseLearningResource c  where c.isDeleted =1 and c.courseId =:courseId")
    public List<CourseLearningResource> findAll(@Param("courseId") Integer courseId);

    @Query("from CourseLearningResource c  where c.isDeleted =1 and c.attachId =:attachId")
    public List<CourseLearningResource> findByAttachId(@Param("attachId") String attachId);

    @Query("update CourseLearningResource c set c.isDeleted=0 where c.id =:id")
    @Modifying
    public Integer delById(@Param(value = "id") Integer id);

    @Query("update CourseLearningResource c set c.courseId =:courseId ,c.name =:name ,c.pdfPath =:pdfPath  where c.id =:id")
    @Modifying
    public Integer updateById(@Param(value = "courseId") Integer courseId, @Param(value = "name") String name, @Param(value = "pdfPath") String pdfPath, @Param(value = "id") Integer id);

    @Query(value=" select a.* ,b.title as courseName from tb_course_learning_resource a  right  join tb_course b on a.course_id = b.id  where (a.name like CONCAT('%',:name,'%')  or :name is null or :name= '')and 1=1 and a.is_deleted =1 order by ?#{#pageable}",nativeQuery = true)
    public Page<List<Map<String, Object>>>  queryAllResource(@Param("name") String name, Pageable pageable);

    @Query("from CourseLearningResource c where  c.isDeleted=1 and c.id =:id")
    public CourseLearningResource findByResourceId(@Param(value = "id") Integer id);

    @Query("update CourseLearningResource c set c.courseId =:courseId  where c.id =:id")
    @Modifying
    public Integer updateById2(@Param(value = "courseId") Integer courseId, @Param(value = "id") Integer id);


}
