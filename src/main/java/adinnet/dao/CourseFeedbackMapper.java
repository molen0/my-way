package adinnet.dao;

import com.adinnet.repository.CourseFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2018/9/18.
 */
//public interface CourseFeedbackMapper extends JpaRepository<CourseFeedback, Integer>, JpaSpecificationExecutor<CourseFeedback> {
    public interface CourseFeedbackMapper extends Repositor<CourseFeedback, Integer>,JpaSpecificationExecutor<CourseFeedback>{



    /**
     * 查询所有反馈
     * @return
     */
    //todo
    @Query(value = " SELECT \n" +
            " \t f.*, d.photo AS photo,\n" +
            " \t d.name AS name,\n" +
            " (select GROUP_CONCAT(t.path SEPARATOR \",\")  from tb_attach  t where t.attach_id=f.attach_id) as path ,\n" +
           // " (select fd.reply from tb_course_feedback fd where  fd.reply_id=f.id)  as replyContent, \n" +
            "  \"健康云医生客服\" as title \n" +
            " FROM \n" +
            " \t tb_course_feedback f \n" +
            " LEFT JOIN tb_doctor d ON d.id = f.doctor_id where f.course_id = :courseId and f.course_type = :courseType  order by f.create_time desc " +
            " ",nativeQuery = true)
    public List<Map<String, Object>> queryFeedByCourseId(@Param(value = "courseId") Integer courseId, @Param(value = "courseType") Integer courseType);




    @Query(value="select\n" +
            " co.*, d.name,\n" +
            " c.title,\n" +
            "  o.name as \"outName\"\n" +
            " from \n" +
            " tb_course_feedback co \n" +
            " left join tb_course c on c.id = co.course_id \n" +
            " left join tb_doctor d on d.id = co.doctor_id \n" +
            " left join tb_course_outline o  on c.id=o.course_id ",nativeQuery = true)
    public Page<List<Map<String, Object>>> queryAllFeedBack(Specification<CourseFeedback> spec, org.springframework.data.domain.Pageable pageable);



    @Query("update CourseFeedback csr  set csr.replyUserId =:replyUserId,csr.reply =:reply,csr.updateTime=now() where csr.id=:id")
    @Modifying
    public void updateCourseFeedback(@Param("replyUserId") Integer replyUserId, @Param("reply") String reply, @Param("id") Integer id);
}
