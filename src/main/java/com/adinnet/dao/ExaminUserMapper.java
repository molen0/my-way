package com.adinnet.dao;

import com.adinnet.repository.CourseEvaluate;
import com.adinnet.repository.ExaminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 用户考试记录
 * @create 2018-09-29 16:18
 **/
public interface ExaminUserMapper extends Repositor<ExaminUser, Integer>,JpaSpecificationExecutor<ExaminUser> {

//    @Query(value=" SELECT teu.* ,tc.course_attr,tc.title,tc.course_type,td.name,tuc.credit_url \n" +
//            "   FROM tb_examin_user teu " +
//            "   LEFT JOIN tb_examin te ON teu.examin_id = te.id " +
//            "   LEFT JOIN tb_user_credit tuc ON tuc.examin_user_id = teu.id " +
//            "   LEFT JOIN tb_doctor td ON teu.user_id = td.id " +
//            "   LEFT JOIN tb_course tc ON te.course_id = tc.id where te.is_delete=1 ",nativeQuery = true)
//    public Page<List<Map<String, Object>>> queryPage(Specification<ExaminUser> spec, Pageable pageable);

    @Query(value = "select teu.* from tb_examin_user teu LEFT JOIN\n" +
            " tb_examin te ON te.id=teu.examin_id\n" +
            " LEFT JOIN\n" +
            " tb_course tc ON tc.id=te.course_id where teu.user_id = :userId and teu.semester_id = :semesterId and teu.is_delete=1 and tc.course_type=1",nativeQuery = true)
    public List<ExaminUser> examinUserList(@Param(value = "userId") Integer userId, @Param(value = "semesterId") Integer semesterId);

    @Query(value = "SELECT COUNT(1) FROM (\n" +
            "SELECT sum(teu.credit) tol FROM tb_examin_user teu \n" +
            "LEFT JOIN tb_doctor td ON td.id=teu.user_id \n" +
            "LEFT JOIN tb_examin te ON te.id=teu.examin_id \n" +
            "LEFT JOIN tb_course tc ON tc.id=te.course_id \n" +
            "WHERE td.area = :area and teu.semester_id = :semesterId and td.property = :property and teu.is_delete=1 and tc.course_type =1 GROUP BY teu.user_id) tem\n" +
            "WHERE tem.tol >=:crateX AND tem.tol <= :crateD",nativeQuery = true)
    public Integer totleCrateList(@Param(value = "area") String area, @Param(value = "crateD") Double crateD, @Param(value = "crateX") Double crateX, @Param(value = "semesterId") Integer semesterId, @Param(value = "property") Integer property);

    @Query(value = "SELECT COUNT(1) FROM (\n" +
            "SELECT td.name FROM tb_examin_user teu \n" +
            "LEFT JOIN tb_doctor td ON td.id=teu.user_id \n" +
            "LEFT JOIN tb_examin te ON te.id=teu.examin_id \n" +
            "LEFT JOIN tb_course tc ON tc.id=te.course_id \n" +
            "WHERE td.area = :area and teu.semester_id = :semesterId and td.property = :property and teu.is_delete=1 and tc.course_type =1 GROUP BY teu.user_id HAVING COUNT(teu.user_id) > :counts) s",nativeQuery = true)
    public Integer totleExaminList(@Param(value = "area") String area, @Param(value = "counts") Integer count, @Param(value = "semesterId") Integer semesterId, @Param(value = "property") Integer property);

    @Query(value = "SELECT COUNT(1) FROM (\n" +
            "SELECT sr.course_id FROM tb_course_study_rate sr \n" +
            "LEFT JOIN tb_course c on sr.course_id=c.id \n" +
            "LEFT JOIN tb_doctor d ON d.id=sr.doctor_id \n" +
            "WHERE d.area = :area and c.semester_id = :semesterId and d.property = :property and c.course_type =1 GROUP BY sr.doctor_id HAVING COUNT(sr.doctor_id) > :counts) s",nativeQuery = true)
    public Integer totleLearnList(@Param(value = "area") String area, @Param(value = "semesterId") Integer semesterId, @Param(value = "property") Integer property, @Param(value = "counts") Integer count);
}
