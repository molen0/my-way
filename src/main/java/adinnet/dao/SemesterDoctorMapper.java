package adinnet.dao;

import com.adinnet.repository.SemesterDoctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by adinnet on 2018/9/20.
 */
public interface SemesterDoctorMapper extends JpaRepository<SemesterDoctor, Integer>, JpaSpecificationExecutor<SemesterDoctor> {
    /**
     * 查询本学期
     * @return
     */
    @Query("from SemesterDoctor s where s.semesterId = :semesterId and s.doctorId = :doctorId")
    public SemesterDoctor queryIsSignUp(@Param(value = "semesterId") Integer semesterId, @Param(value = "doctorId") Integer doctorId);
}
