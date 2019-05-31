package adinnet.dao;

import com.adinnet.repository.ReportDoctor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/10/31.
 */
public interface ReportDoctorMapper extends Repositor<ReportDoctor, Integer>, JpaSpecificationExecutor<ReportDoctor> {



    @Query(value = "SELECT IFNULL(SUM(eu.credit), 0) " +
            "    FROM tb_examin_user eu JOIN tb_examin e ON eu.examin_id = e.id JOIN " +
            " tb_course c ON e.course_id = c.id WHERE  c.course_type = 1 and eu.user_id = :doctorId ",nativeQuery = true)
    public String findByDoctorId(@Param("doctorId") Integer doctorId);
}
